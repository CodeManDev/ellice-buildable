#version 330 core

#include <common/rounded_clip.glsl>

in vec2 vUV;
uniform vec4 uRect;
uniform vec4 uSamples[128];
uniform float uProgress;
uniform float uPreview;
uniform float uPlaying;
uniform float uLoaded;
uniform float uTime;
uniform float uOpacity;
uniform float uMetricScale;
uniform vec4 uAccent;
uniform vec4 uRemaining;
uniform vec4 uInk;
out vec4 fragColor;

float sampleAt(float coordinate) {
    int index = int(clamp(coordinate * 512.0, 0.0, 511.0));
    return uSamples[index / 4][index % 4];
}

float boxDistance(vec2 point, vec2 halfSize, float radius) {
    vec2 q = abs(point) - halfSize + radius;
    return length(max(q, 0.0)) + min(max(q.x, q.y), 0.0) - radius;
}

vec4 layer(vec3 color, float alpha) {
    float a = clamp(alpha, 0.0, 1.0);
    return vec4(color * a, a);
}

vec4 over(vec4 front, vec4 back) { return front + back * (1.0 - front.a); }

void main() {
    vec2 size = uRect.zw;
    vec2 point = vUV * size;
    float unit = max(uMetricScale, 0.01);
    float aa = max(0.65, unit * 0.3);
    float center = size.y * 0.5;
    float count = clamp(floor(size.x / (3.1 * unit)), 24.0, 256.0);
    float stepSize = size.x / count;
    float bar = floor(point.x / stepSize);
    float barCenter = (bar + 0.5) * stepSize;

    
    
    float amplitude = 0.0;
    for (int i = 0; i < 8; i++) {
        amplitude = max(amplitude, sampleAt((bar + (float(i) + 0.5) / 8.0) / count));
    }
    float halfHeight = max(0.8 * unit, pow(amplitude, 0.8) * (center - 8.0 * unit));
    float halfWidth = max(0.45 * unit, stepSize * 0.28);
    float radius = min(halfWidth, 1.2 * unit);
    float distanceToBar = boxDistance(vec2(point.x - barCenter, point.y - center),
        vec2(halfWidth, halfHeight), radius);
    float bars = 1.0 - smoothstep(-aa, aa, distanceToBar);
    float head = clamp(uProgress, 0.0, 1.0) * (size.x - unit);
    float played = (1.0 - smoothstep(head - unit, head + unit, point.x)) * uLoaded;
    vec3 accent = uAccent.rgb;
    vec3 ink = mix(uRemaining.rgb, accent, 0.65 + 0.35 * clamp(1.0 - abs(point.y - center) / max(center, 1.0), 0.0, 1.0));
    vec3 remaining = uRemaining.rgb;

    
    float gridX = abs(fract(vUV.x * 8.0 + 0.5) - 0.5) * size.x / 8.0;
    float grid = (1.0 - smoothstep(0.0, aa, gridX)) * 0.06 * uLoaded;
    float baseline = (1.0 - smoothstep(0.25 * unit, 0.7 * unit + aa, abs(point.y - center))) * 0.16;
    vec4 composed = layer(remaining, max(grid, baseline));
    float glow = exp(-max(distanceToBar, 0.0) / (2.5 * unit)) * (1.0 - bars);
    composed = over(layer(ink, glow * played * 0.13), composed);
    composed = over(layer(mix(remaining, ink, played), bars * mix(0.68, 1.0, uLoaded)), composed);

    float headDistance = abs(point.x - head);
    float pulse = 0.9 + 0.1 * sin(uTime * 3.0);
    float halo = exp(-headDistance / (6.0 * unit))
        * exp(-abs(point.y - center) / max(center * 0.7, 1.0));
    composed = over(layer(accent, halo * 0.20 * uPlaying * pulse), composed);
    float rail = (1.0 - smoothstep(0.5 * unit, 0.5 * unit + aa, headDistance))
        * smoothstep(0.0, 4.0 * unit, min(point.y, size.y - point.y)) * uLoaded;
    composed = over(layer(uInk.rgb, rail), composed);
    float cap = length(vec2(point.x - head, point.y - 3.0 * unit));
    composed = over(layer(accent, (1.0 - smoothstep(1.7 * unit, 1.7 * unit + aa, cap)) * uLoaded), composed);

    if (uPreview >= 0.0 && uLoaded > 0.5) {
        float previewX = clamp(uPreview, 0.0, 1.0) * (size.x - unit);
        float guide = (1.0 - smoothstep(0.3 * unit, 0.3 * unit + aa, abs(point.x - previewX)))
            * step(0.4, fract(point.y / (5.0 * unit)));
        composed = over(layer(uInk.rgb, guide * 0.7), composed);
    }
    fragColor = composed * clamp(uOpacity, 0.0, 1.0) * elliceRoundedClipMask();
}
