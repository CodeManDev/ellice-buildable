#version 330 core

#include <common/rounded_clip.glsl>

in vec2 vUV;
out vec4 fragColor;
uniform vec4 uRect;
uniform vec4 uPoints[40]; 
uniform int uCount;
uniform vec4 uColor;
uniform float uOpacity;
uniform float uMetricScale;

float segmentDistance(vec2 p, vec2 a, vec2 b) {
    vec2 ab = b - a;
    return length(p - a - ab * clamp(dot(p - a, ab) / max(dot(ab, ab), 0.0001), 0.0, 1.0));
}

void main() {
    float scale = max(0.01, uMetricScale);
    vec2 padding = vec2(5.0 * scale);
    vec2 size = max(vec2(1.0), uRect.zw - padding * 2.0);
    vec2 p = vUV * uRect.zw - padding;
    float distanceToLine = 100000.0;
    float distanceToPoint = 100000.0;
    float fill = 0.0;
    for (int i = 0; i < 40; i++) {
        if (i >= uCount) break;
        vec4 samplePoint = uPoints[i];
        if (samplePoint.z < 0.5) continue;
        vec2 b = vec2(samplePoint.x, 1.0 - samplePoint.y) * size;
        if (i == uCount - 1 || samplePoint.w < 0.5) distanceToPoint = min(distanceToPoint, length(p - b));
        if (i == 0 || samplePoint.w < 0.5) continue;
        vec4 prior = uPoints[i - 1];
        vec2 a = vec2(prior.x, 1.0 - prior.y) * size;
        distanceToLine = min(distanceToLine, segmentDistance(p, a, b));
        if (p.x >= a.x && p.x <= b.x && b.x > a.x) {
            float lineY = mix(a.y, b.y, (p.x - a.x) / (b.x - a.x));
            fill = max(fill, smoothstep(lineY - scale, lineY + scale, p.y)
                * (1.0 - clamp(p.y / size.y, 0.0, 1.0)) * 0.19);
        }
    }
    float gridDistance = abs(fract(p.y / size.y * 4.0 + 0.5) - 0.5) * size.y / 4.0;
    float grid = (1.0 - smoothstep(0.0, scale, gridDistance)) * 0.08;
    if (p.x < 0.0 || p.x > size.x || p.y < 0.0 || p.y > size.y) grid = 0.0;
    float stroke = 1.0 - smoothstep(0.65 * scale, 1.65 * scale, distanceToLine);
    float dotAlpha = 1.0 - smoothstep(2.0 * scale, 3.0 * scale, distanceToPoint);
    float glow = exp(-distanceToLine / (3.0 * scale)) * 0.12;
    float alpha = max(grid, max(fill + glow, max(stroke, dotAlpha))) * uOpacity * uColor.a;
    alpha *= elliceRoundedClipMask();
    fragColor = vec4(uColor.rgb * alpha, alpha);
}
