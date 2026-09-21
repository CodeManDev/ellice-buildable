#version 330 core

#include <common/rounded_clip.glsl>

in vec2 vLocalPos;

uniform vec4 uRect;
uniform float uRadius;
uniform vec3 uTail;         
uniform float uSmoothK;
uniform vec4 uColor;
uniform vec4 uGradientEnd;
uniform vec4 uShadow;       
uniform vec4 uShadowColor;
uniform vec2 uBorder;       
uniform vec4 uBorderColor;
uniform float uInnerHighlight;
uniform float uInnerHighlightSize;
uniform float uOpacity;
uniform float uEdgeSoftness;
uniform sampler2D uBlurTexture;
uniform int uHasBlur;
uniform float uVibrancy;

out vec4 fragColor;

float roundedBox(vec2 p, vec2 halfSize, float radius) {
    float r = min(max(radius, 0.0), min(halfSize.x, halfSize.y));
    vec2 q = abs(p) - halfSize + vec2(r);
    return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - r;
}

float triangle(vec2 p, vec2 a, vec2 b, vec2 c) {
    vec2 e0 = b - a;
    vec2 e1 = c - b;
    vec2 e2 = a - c;
    vec2 v0 = p - a;
    vec2 v1 = p - b;
    vec2 v2 = p - c;
    vec2 q0 = v0 - e0 * clamp(dot(v0, e0) / max(dot(e0, e0), 0.0001), 0.0, 1.0);
    vec2 q1 = v1 - e1 * clamp(dot(v1, e1) / max(dot(e1, e1), 0.0001), 0.0, 1.0);
    vec2 q2 = v2 - e2 * clamp(dot(v2, e2) / max(dot(e2, e2), 0.0001), 0.0, 1.0);
    float orientation = sign(e0.x * e2.y - e0.y * e2.x);
    vec2 d = min(
        min(vec2(dot(q0, q0), orientation * (v0.x * e0.y - v0.y * e0.x)),
            vec2(dot(q1, q1), orientation * (v1.x * e1.y - v1.y * e1.x))),
        vec2(dot(q2, q2), orientation * (v2.x * e2.y - v2.y * e2.x))
    );
    return -sqrt(max(d.x, 0.0)) * sign(d.y);
}

float smoothUnion(float a, float b, float k) {
    float safeK = max(k, 0.001);
    float h = max(safeK - abs(a - b), 0.0) / safeK;
    return min(a, b) - h * h * safeK * 0.25;
}

float bubbleDistance(vec2 local) {
    vec2 halfSize = uRect.zw * 0.5;
    float body = roundedBox(local - halfSize, halfSize, uRadius);
    if (uTail.y <= 0.0 || uTail.z <= 0.0) return body;
    float halfTail = uTail.y * 0.5;
    float baseY = uRect.w - max(1.0, uSmoothK * 0.35);
    float tail = triangle(local,
        vec2(uTail.x - halfTail, baseY),
        vec2(uTail.x + halfTail, baseY),
        vec2(uTail.x, uRect.w + uTail.z));
    return smoothUnion(body, tail, uSmoothK);
}

vec4 over(vec4 top, vec4 bottom) {
    return top + bottom * (1.0 - top.a);
}

void main() {
    float distance = bubbleDistance(vLocalPos);
    float aa = max(fwidth(distance), max(0.65, uEdgeSoftness));
    float mask = 1.0 - smoothstep(-aa, aa, distance);

    vec4 result = vec4(0.0);
    if (uShadow.z > 0.0 && uShadowColor.a > 0.0) {
        float shadowDistance = bubbleDistance(vLocalPos - uShadow.xy) - uShadow.w;
        float shadowMask = 1.0 - smoothstep(-uShadow.z, uShadow.z, shadowDistance);
        float shadowAlpha = shadowMask * uShadowColor.a * uOpacity;
        result = vec4(uShadowColor.rgb * shadowAlpha, shadowAlpha);
    }

    float gradientT = clamp(vLocalPos.y / max(uRect.w, 1.0), 0.0, 1.0);
    vec4 fill = uGradientEnd.a > 0.001 ? mix(uColor, uGradientEnd, gradientT) : uColor;
    vec3 fillRgb = fill.rgb;
    float fillAlpha;
    if (uHasBlur != 0) {
        vec2 screenUv = clamp(gl_FragCoord.xy / uResolution, vec2(0.001), vec2(0.999));
        vec3 blurred = texture(uBlurTexture, screenUv).rgb;
        float luminance = dot(blurred, vec3(0.2126, 0.7152, 0.0722));
        blurred = mix(vec3(luminance), blurred, uVibrancy);
        fillRgb = mix(blurred, fill.rgb, fill.a);
        fillAlpha = mask * uOpacity;
    } else {
        fillAlpha = mask * fill.a * uOpacity;
    }
    result = over(vec4(fillRgb * fillAlpha, fillAlpha), result);

    if (uInnerHighlight > 0.001) {
        float highlightSize = uInnerHighlightSize > 0.5
            ? uInnerHighlightSize
            : max(uRect.w * 0.42, 8.0);
        float fade = pow(1.0 - clamp(vLocalPos.y / highlightSize, 0.0, 1.0), 1.6);
        float highlightAlpha = uInnerHighlight * fade * mask * uOpacity;
        result = over(vec4(vec3(highlightAlpha), highlightAlpha), result);
    }

    if (uBorder.x > 0.0 && uBorderColor.a > 0.0) {
        float borderDistance = abs(distance) - uBorder.x * 0.5;
        float borderMask = 1.0 - smoothstep(-aa, aa, borderDistance);
        float borderAlpha = borderMask * uBorderColor.a * uOpacity;
        result = over(vec4(uBorderColor.rgb * borderAlpha, borderAlpha), result);
    }

    fragColor = result * elliceRoundedClipMask();
}
