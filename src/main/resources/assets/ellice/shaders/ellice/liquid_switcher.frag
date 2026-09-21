#version 330 core

#include <common/rounded_clip.glsl>

in vec2 vUV;

uniform vec4 uRect;
uniform vec2 uSize;
uniform vec4 uRadius;
uniform vec4 uActive;  
uniform vec4 uHover;   
uniform vec4 uAccent;

out vec4 fragColor;

float roundedBox(vec2 p, vec2 b, float r) {
    vec2 q = abs(p) - b + vec2(r);
    return length(max(q, 0.0)) + min(max(q.x, q.y), 0.0) - r;
}

float capsuleDrop(vec2 px, float x, float w, float trackH, float dropH, float grow) {
    float dh = max(1.0, dropH + grow * 2.0);
    float r = dh * 0.5;
    vec2 center = vec2(x + w * 0.5, trackH * 0.5);
    vec2 halfSize = vec2(max(w * 0.5 + grow, r), r);
    return roundedBox(px - center, halfSize, r);
}

float metaball(float d, float softness) {
    return exp(-max(d, 0.0) * max(d, 0.0) / max(softness * softness, 0.001));
}

vec4 over(vec4 top, vec4 bottom) {
    return top + bottom * (1.0 - top.a);
}

void main() {
    vec2 px = vUV * uSize;
    float opacity = clamp(uActive.w, 0.0, 1.0);
    float h = uSize.y;
    float requestedR = max(max(uRadius.x, uRadius.y), max(uRadius.z, uRadius.w));
    float trackR = requestedR > 0.0 ? clamp(requestedR, 0.0, h * 0.5) : h * 0.5;
    float aa = max(0.75, fwidth(px.x) + fwidth(px.y));

    float trackD = roundedBox(px - uSize * 0.5, uSize * 0.5, trackR);
    float trackMask = 1.0 - smoothstep(-aa, aa, trackD);
    if (trackMask <= 0.001) {
        fragColor = vec4(0.0);
        return;
    }

    float yT = clamp(px.y / max(h, 1.0), 0.0, 1.0);
    vec3 accent = uAccent.rgb;
    vec3 topGlass = mix(vec3(0.98, 0.99, 1.0), accent, 0.025);
    vec3 botGlass = mix(vec3(0.72, 0.77, 0.84), accent, 0.035);
    vec3 trackRgb = mix(topGlass, botGlass, yT);
    float trackAlpha = (0.24 + 0.10 * (1.0 - yT)) * opacity * trackMask;
    vec4 result = vec4(trackRgb * trackAlpha, trackAlpha);

    float activeX = uActive.x;
    float activeW = max(1.0, uActive.y);
    float hoverMix = clamp(uHover.z, 0.0, 1.0);
    float hoverX = uHover.x;
    float hoverW = max(1.0, uHover.y);
    float transition = clamp(uActive.z, 0.0, 1.0);
    float pulse = sin(transition * 3.14159265);

    float pad = max(3.0, h * 0.13);
    float stretch = pulse * min(9.0, activeW * 0.08);
    float dropH = max(1.0, h - pad * 2.0 + pulse * 1.4);
    float activeDropX = activeX + pad - stretch * 0.5;
    float activeDropW = max(1.0, activeW - pad * 2.0 + stretch);
    float hoverDropX = hoverX + pad;
    float hoverDropW = max(1.0, hoverW - pad * 2.0);

    float activeD = capsuleDrop(px, activeDropX, activeDropW, h, dropH, 0.0);
    float activeMask = 1.0 - smoothstep(-aa, aa, activeD);

    float hoverD = capsuleDrop(px, hoverDropX, hoverDropW, h, dropH, 0.0);
    float hoverMask = (1.0 - smoothstep(-aa, aa, hoverD)) * hoverMix * 0.46;

    float activeCenter = activeDropX + activeDropW * 0.5;
    float hoverCenter = hoverDropX + hoverDropW * 0.5;
    float bridgeLo = min(activeCenter, hoverCenter);
    float bridgeHi = max(activeCenter, hoverCenter);
    float bridgeSpan = max(bridgeHi - bridgeLo, 1.0);
    float bridgeY = abs(px.y - h * 0.5) / max(dropH * 0.5, 1.0);
    float between = smoothstep(bridgeLo - 2.0, bridgeLo + 3.0, px.x)
        * (1.0 - smoothstep(bridgeHi - 3.0, bridgeHi + 2.0, px.x));
    float bridge = between
        * exp(-bridgeY * bridgeY * 5.8)
        * hoverMix
        * smoothstep(8.0, 26.0, bridgeSpan);

    float field = max(max(activeMask, hoverMask), bridge * 0.58);
    float fieldSoft = max(metaball(activeD, h * 0.23), metaball(hoverD, h * 0.23) * hoverMix);
    field = clamp(max(field, fieldSoft * 0.24), 0.0, 1.0);

    vec2 blobCenter = vec2(activeCenter, h * 0.5);
    float blobDist = length((px - blobCenter) / vec2(max(activeDropW * 0.5, 1.0), max(dropH * 0.5, 1.0)));
    float lens = (1.0 - smoothstep(0.0, 1.12, blobDist)) * field;
    float upperCatch = pow(1.0 - yT, 5.0) * field;

    vec3 blobTop = mix(vec3(1.0), accent, 0.055);
    vec3 blobBot = mix(vec3(0.83, 0.90, 0.98), accent, 0.095);
    vec3 blobRgb = mix(blobTop, blobBot, yT);
    blobRgb += vec3(1.0) * upperCatch * 0.055;
    blobRgb += accent * (lens * 0.026 + pulse * field * 0.018);
    float blobAlpha = field * (0.56 + pulse * 0.045) * opacity;
    result = over(vec4(blobRgb * blobAlpha, blobAlpha), result);

    float hoverGlow = hoverMix * (1.0 - smoothstep(0.0, 12.0, abs(hoverD)));
    result.rgb += accent * hoverGlow * 0.018 * opacity;

    float topLine = pow(1.0 - yT, 8.0) * trackMask * opacity * 0.075;
    result = over(vec4(vec3(1.0) * topLine, topLine), result);

    fragColor = result * elliceRoundedClipMask();
}
