#version 330 core

#include <common/rounded_clip.glsl>

in vec2 vTexCoord;

uniform sampler2D uBackdrop;
uniform sampler2D uSharpBackdrop;
uniform vec2 uSize;
uniform vec4 uRadius;
uniform float uOpacity;
uniform float uEdgeSoftness;
uniform float uBlurRadius;
uniform float uSaturation;
uniform float uBrightness;
uniform float uContrast;
uniform float uRefraction;
uniform float uNoise;
uniform float uChromatic;
uniform float uBorderWidth;
uniform float uTime;
uniform vec4 uMouse;       
uniform vec4 uEdgeWobble;  
uniform vec4 uRubber;      
uniform vec4 uTint;
uniform vec4 uTint2;
uniform vec4 uBorderColor;
uniform vec4 uBorderColor2;
uniform vec4 uHighlightColor;

out vec4 fragColor;

float cornerRadius(vec2 p, vec4 r) {
    if (p.x < 0.0) {
        return p.y < 0.0 ? r.x : r.w;
    }
    return p.y < 0.0 ? r.y : r.z;
}

float roundedBox(vec2 p, vec2 b, vec4 radius) {
    vec4 r = clamp(radius, 0.0, min(b.x, b.y));
    float cr = cornerRadius(p, r);
    vec2 q = abs(p) - b + vec2(min(cr, min(b.x, b.y)));
    return length(max(q, 0.0)) + min(max(q.x, q.y), 0.0) - min(cr, min(b.x, b.y));
}

float hash12(vec2 p) {
    vec3 p3 = fract(vec3(p.xyx) * 0.1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

vec3 adjustColor(vec3 rgb) {
    float gray = dot(rgb, vec3(0.2126, 0.7152, 0.0722));
    rgb = mix(vec3(gray), rgb, max(0.0, uSaturation));
    rgb = (rgb - 0.5) * max(0.0, uContrast) + 0.5 + uBrightness;
    return rgb;
}

float compactGlassFactor() {
    float minSize = max(1.0, min(uSize.x, uSize.y));
    float maxSize = max(uSize.x, uSize.y);
    float minRadius = min(min(uRadius.x, uRadius.y), min(uRadius.z, uRadius.w));
    float small = 1.0 - smoothstep(52.0, 150.0, minSize);
    float pillRound = smoothstep(0.34, 0.48, minRadius / minSize);
    float pillWide = smoothstep(1.55, 2.65, maxSize / minSize);
    return clamp(max(small, pillRound * pillWide), 0.0, 1.0);
}

vec3 sampleRawBackdrop(vec2 uv) {
    return texture(uSharpBackdrop, clamp(uv, vec2(0.001), vec2(0.999))).rgb;
}

vec3 sampleBackdrop(vec2 uv, float radius) {
    float compactSurface = compactGlassFactor();
    float r = clamp(radius * mix(0.24, 0.34, compactSurface), 0.0, 22.0);
    if (r < 0.5) return texture(uBackdrop, uv).rgb;

    vec2 o = r / uResolution;
    vec3 c = texture(uBackdrop, uv).rgb * 0.227027;
    c += texture(uBackdrop, uv + vec2( o.x, 0.0)).rgb * 0.1216216;
    c += texture(uBackdrop, uv + vec2(-o.x, 0.0)).rgb * 0.1216216;
    c += texture(uBackdrop, uv + vec2(0.0,  o.y)).rgb * 0.1216216;
    c += texture(uBackdrop, uv + vec2(0.0, -o.y)).rgb * 0.1216216;
    c += texture(uBackdrop, uv + vec2( o.x,  o.y)).rgb * 0.0766216;
    c += texture(uBackdrop, uv + vec2(-o.x,  o.y)).rgb * 0.0766216;
    c += texture(uBackdrop, uv + vec2( o.x, -o.y)).rgb * 0.0766216;
    c += texture(uBackdrop, uv + vec2(-o.x, -o.y)).rgb * 0.0766216;
    return c;
}

float luminance(vec3 rgb) {
    return dot(rgb, vec3(0.2126, 0.7152, 0.0722));
}

vec3 backdropBevelResponse(vec2 uv, vec2 normal, vec2 tangent, float edgeBand) {
    float highlightGain = clamp(0.18 + uHighlightColor.a * 2.8 + uRefraction * 0.018, 0.0, 1.15);
    if (edgeBand <= 0.001 || highlightGain <= 0.001) return vec3(0.0);

    float reachPx = clamp(2.2 + uBlurRadius * 0.12 + uRefraction * 0.55, 2.4, 22.0);
    vec2 n = normal * (reachPx / uResolution);
    vec2 t = tangent * (reachPx * 0.70 / uResolution);

    vec3 outsideProbe = sampleRawBackdrop(uv + n) * 0.46
        + sampleRawBackdrop(uv + n * 1.75 + t * 0.55) * 0.27
        + sampleRawBackdrop(uv + n * 1.75 - t * 0.55) * 0.27;
    vec3 insideProbe = sampleRawBackdrop(uv - n * 0.75) * 0.50
        + sampleRawBackdrop(uv - n * 0.35 + t * 0.75) * 0.25
        + sampleRawBackdrop(uv - n * 0.35 - t * 0.75) * 0.25;

    vec3 tangentProbeA = sampleRawBackdrop(uv + t);
    vec3 tangentProbeB = sampleRawBackdrop(uv - t);
    float lumOut = luminance(outsideProbe);
    float lumIn = luminance(insideProbe);
    float grad = lumOut - lumIn;
    float contrast = smoothstep(0.006, 0.115, abs(grad));
    float hot = smoothstep(0.35, 0.88, max(lumOut, lumIn));
    float lit = max(grad, 0.0) * contrast * (0.75 + 0.90 * hot);
    float shadow = max(-grad, 0.0) * contrast * 0.18;

    float tangentContrast = smoothstep(0.024, 0.18, abs(luminance(tangentProbeA) - luminance(tangentProbeB)));
    vec3 outsideSoft = sampleBackdrop(uv + n, uBlurRadius) * 0.46
        + sampleBackdrop(uv + n * 1.75 + t * 0.55, uBlurRadius) * 0.27
        + sampleBackdrop(uv + n * 1.75 - t * 0.55, uBlurRadius) * 0.27;
    vec3 tangentSoftA = sampleBackdrop(uv + t, uBlurRadius);
    vec3 tangentSoftB = sampleBackdrop(uv - t, uBlurRadius);
    vec3 tangentSoft = luminance(tangentProbeA) > luminance(tangentProbeB) ? tangentSoftA : tangentSoftB;
    float glint = tangentContrast * hot * 0.16;

    float lumSoft = luminance(outsideSoft);
    vec3 softSpecular = mix(outsideSoft, outsideSoft / max(lumSoft, 0.001), 0.18);
    vec3 highlightColor = mix(uHighlightColor.rgb, softSpecular, 0.38);
    vec3 tangentColor = mix(uHighlightColor.rgb, tangentSoft, 0.32);
    vec3 delta = highlightColor * lit + tangentColor * glint;
    delta -= vec3(0.38, 0.40, 0.44) * shadow;
    float compactSurface = compactGlassFactor();
    float normalSheen = smoothstep(-0.15, 0.90, -normal.y) * 0.45
        + smoothstep(0.15, 0.85, abs(normal.x)) * 0.28;
    float bevelFloor = (0.018 + uHighlightColor.a * 0.050)
        * (0.65 + normalSheen)
        * (1.0 + compactSurface * 0.95);
    delta += uHighlightColor.rgb * pow(edgeBand, 1.80) * bevelFloor;
    return delta * edgeBand * highlightGain;
}

vec4 over(vec4 top, vec4 bottom) {
    return top + bottom * (1.0 - top.a);
}

float rectSdfAt(vec2 pixel) {
    return roundedBox(pixel - uSize * 0.5, uSize * 0.5, uRadius);
}

vec2 rectNormalAt(vec2 pixel) {
    float e = 1.0;
    vec2 g = vec2(
        rectSdfAt(pixel + vec2(e, 0.0)) - rectSdfAt(pixel - vec2(e, 0.0)),
        rectSdfAt(pixel + vec2(0.0, e)) - rectSdfAt(pixel - vec2(0.0, e))
    );
    if (dot(g, g) < 0.0001) {
        vec2 fallback = pixel - uSize * 0.5;
        return length(fallback) > 0.001 ? normalize(fallback) : vec2(0.0, -1.0);
    }
    return normalize(g);
}

vec2 rubberSideNormal(float side) {
    if (side < 0.5) return vec2(-1.0, 0.0);
    if (side < 1.5) return vec2( 1.0, 0.0);
    if (side < 2.5) return vec2(0.0, -1.0);
    return vec2(0.0, 1.0);
}

vec2 rubberSideTangent(float side) {
    return side < 1.5 ? vec2(0.0, 1.0) : vec2(1.0, 0.0);
}

float rubberProfile(float tangentDistance, float width) {
    float x = abs(tangentDistance) / max(width, 1.0);
    float core = exp(-x * x * 2.45);
    float shoulderX = (x - 1.05) / 0.55;
    float shoulder = exp(-shoulderX * shoulderX) * 0.075;
    return clamp(core - shoulder, -0.055, 1.0);
}

float rubberEdgeDisplacement(vec2 pixel, float baseDist, vec2 baseNormal) {
    float side = uRubber.x;
    float amount = uRubber.z;
    float width = max(uRubber.w, 1.0);
    if (side < -0.5 || abs(amount) <= 0.001) return 0.0;

    vec2 sideNormal = rubberSideNormal(side);
    vec2 sideTangent = rubberSideTangent(side);
    vec2 boundary = pixel - baseNormal * baseDist;
    float sideMask = smoothstep(0.38, 0.88, dot(baseNormal, sideNormal));
    float tangentDistance = dot(boundary, sideTangent) - uRubber.y;
    float edgeBand = 1.0 - smoothstep(width * 0.34 + 8.0, width * 0.58 + 24.0, abs(baseDist));
    return amount * rubberProfile(tangentDistance, width) * sideMask * edgeBand;
}

void main() {
    vec2 pixel = vTexCoord * uSize;
    vec2 p = pixel - uSize * 0.5;

    float dist = roundedBox(p, uSize * 0.5, uRadius);
    vec2 baseNormal = rectNormalAt(pixel);
    dist -= rubberEdgeDisplacement(pixel, dist, baseNormal);
    float edge = max(max(fwidth(dist), 0.65), uEdgeSoftness);
    float mask = 1.0 - smoothstep(-edge, edge, dist);
    if (mask <= 0.001) {
        fragColor = vec4(0.0);
        return;
    }

    vec2 uv = clamp(gl_FragCoord.xy / uResolution, vec2(0.001), vec2(0.999));
    vec2 normal = baseNormal;
    float rimWidth = max(2.75, uBorderWidth * 5.2 + edge * 2.15);
    float rimMask = 1.0 - smoothstep(0.0, rimWidth, abs(dist));
    float edgeLens = pow(rimMask, 2.25);
    vec2 local = vTexCoord - 0.5;
    float centerLens = 1.0 - smoothstep(0.0, 0.72, length(local * vec2(uSize.x / max(uSize.y, 1.0), 1.0)) * 1.35);
    float liquid = sin((vTexCoord.x * 3.1 + vTexCoord.y * 1.7) * 6.2831853)
        + sin((vTexCoord.x * -1.6 + vTexCoord.y * 2.4) * 6.2831853);
    vec2 tangent = vec2(-normal.y, normal.x);

    float refractPx = clamp(uRefraction, 0.0, 18.0) * (edgeLens * 0.72 + centerLens * 0.16);
    vec2 refractOffset = normal * (refractPx / uResolution);
    vec2 waveOffset = tangent * (liquid * clamp(uRefraction, 0.0, 18.0) * 0.055 * centerLens / uResolution);
    vec2 refractUv = clamp(uv + refractOffset + waveOffset, vec2(0.001), vec2(0.999));

    vec3 blurred = sampleBackdrop(refractUv, uBlurRadius);
    vec3 backdrop = blurred;

    vec3 rgb = adjustColor(backdrop);
    vec4 tint = uTint2.a > 0.001 ? mix(uTint, uTint2, clamp(vTexCoord.y, 0.0, 1.0)) : uTint;
    if (tint.a > 0.001) {
        rgb = mix(rgb, tint.rgb, tint.a);
    }

    float grainA = hash12(floor(gl_FragCoord.xy * 1.35));
    float grainB = hash12(floor(gl_FragCoord.yx * 2.10) + vec2(19.17, 7.31));
    float grain = (grainA * 0.62 + grainB * 0.38) - 0.5;
    rgb += grain * uNoise * (0.45 + tint.a * 0.35);
    float fringeSide = dot(normal, normalize(vec2(0.75, -0.55)));
    rgb += vec3(fringeSide, -abs(fringeSide) * 0.25, -fringeSide)
        * edgeLens * clamp(uChromatic / 2.0, 0.0, 1.0) * 0.007;
    rgb = clamp(rgb, 0.0, 1.0);

    float alpha = mask * clamp(uOpacity, 0.0, 1.0);
    vec4 result = vec4(rgb * alpha, alpha);

    float topSheen = pow(1.0 - clamp(vTexCoord.y, 0.0, 1.0), 2.4) * mask * uHighlightColor.a * 0.13;
    result = over(vec4(uHighlightColor.rgb * topSheen, topSheen), result);

    float lightFacing = smoothstep(0.0, 1.0, clamp(-normal.y * 0.5 + 0.5, 0.0, 1.0));
    vec3 bevel = backdropBevelResponse(uv, normal, tangent, pow(rimMask, 1.35) * mask);
    result.rgb = clamp(result.rgb + bevel * max(result.a, 0.35 * mask), 0.0, 1.0);

    float innerCaustic = (1.0 - smoothstep(0.0, rimWidth * 0.75, abs(dist + rimWidth * 0.50))) * mask;
    float caustic = (innerCaustic * (0.07 + lightFacing * 0.06) + pow(edgeLens, 2.6) * 0.025)
        * uHighlightColor.a * clamp(uRefraction / 8.0, 0.0, 1.0);
    result = over(vec4(uHighlightColor.rgb * caustic, caustic), result);

    if (uBorderWidth > 0.0 && uBorderColor.a > 0.001) {
        float bd = abs(dist) - uBorderWidth * 0.5;
        float borderMask = 1.0 - smoothstep(-edge, edge, bd);
        vec4 border = uBorderColor2.a > 0.001 ? mix(uBorderColor2, uBorderColor, lightFacing) : uBorderColor;
        vec3 borderRgb = mix(border.rgb, uHighlightColor.rgb, lightFacing * uHighlightColor.a * 0.45);
        float borderAlpha = borderMask * border.a * clamp(uOpacity, 0.0, 1.0) * 0.82;
        result = over(vec4(borderRgb * borderAlpha, borderAlpha), result);
    }

    fragColor = result * elliceRoundedClipMask();
}
