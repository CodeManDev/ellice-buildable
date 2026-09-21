#version 330 core
#include <render3d/common/esp_targets.glsl>

uniform sampler2D uNearestSeed;
uniform sampler2D uPlayerMask;
uniform sampler2D uSceneDepth;
uniform mat4 uInvProjection;
uniform vec2 uResolution;
uniform vec2 uRegionMin;
uniform vec2 uRegionMax;
uniform vec2 uVisibility;
uniform vec4 uColorA;
uniform vec4 uColorB;
uniform vec4 uOccludedColor;
uniform float uTime;
uniform float uWidth;
uniform float uComplexity;
uniform float uIntensity;

in vec2 vTexCoord;
out vec4 fragColor;

vec2 complexMultiply(vec2 a, vec2 b) {
    return vec2(a.x * b.x - a.y * b.y, a.x * b.y + a.y * b.x);
}
vec2 rotation(float phase) { return vec2(cos(phase), sin(phase)); }
float viewDepth(vec2 uv, float raw) {
    vec4 view = uInvProjection * vec4(uv * 2.0 - 1.0, raw * 2.0 - 1.0, 1.0);
    return max(-view.z / (abs(view.w) < 0.00001 ? 0.00001 : view.w), 0.0);
}

void main() {
    ivec2 size = textureSize(uPlayerMask, 0);
    ivec2 pixel = clamp(ivec2(gl_FragCoord.xy), ivec2(0), size - ivec2(1));
    vec2 center = vec2(pixel) + 0.5;
    vec4 seed = texelFetch(uNearestSeed, pixel, 0);
    if (espForeground(pixel) || texelFetch(uPlayerMask, pixel, 0).r > 0.001 || seed.b <= 0.0) discard;
    float distanceToContour = max(length(seed.rg) - 0.5, 0.0);
    if (distanceToContour > uWidth + 2.0) discard;
    vec2 sourceCenter = center + seed.rg;
    if (any(lessThan(sourceCenter, uRegionMin + 0.5)) || any(greaterThan(sourceCenter, uRegionMax + 0.5))) discard;
    ivec2 sourcePixel = clamp(ivec2(floor(sourceCenter)), ivec2(0), size - ivec2(1));
    vec2 mask = texelFetch(uPlayerMask, sourcePixel, 0).rg;
    if (mask.r <= 0.001) discard;
    float identity = clamp(mask.g / max(mask.r, 0.0001), 0.0, 1.0);
    float agreement = 1.0 - smoothstep(0.001, 0.003, abs(identity - seed.a));
    if (agreement <= 0.001) discard;

    
    
    vec2 regionSize = max(uRegionMax - uRegionMin, vec2(1.0));
    vec2 z = (center - (uRegionMin + uRegionMax) * 0.5) / max(regionSize.y, 1.0);
    z *= 4.0 * uComplexity;
    float t = uTime;
    float phaseSeed = espTarget(identity).r * 6.2831853;
    z = complexMultiply(z, rotation(t * 0.19 + phaseSeed));
    vec2 z2 = complexMultiply(z, z);
    vec2 cubic = complexMultiply(z2, z) - complexMultiply(z, rotation(-t * 0.37)) * 0.72;
    cubic += rotation(t * 0.43 + phaseSeed) * 0.35;
    vec2 warped = cubic + 0.24 * vec2(sin(cubic.y * 1.7 - t), sin(cubic.x * 1.3 + t * 0.8));
    float phase = atan(warped.y, warped.x);
    float logAmplitude = log(1.0 + dot(warped, warped));
    float d = distanceToContour / max(uWidth, 1.0);

    
    float waveA = 0.5 + 0.5 * cos(3.0 * phase - 1.8 * logAmplitude - 1.2 * t + 11.0 * d);
    float waveB = 0.5 + 0.5 * cos(2.0 * phase + 1.3 * logAmplitude + 0.9 * t - 17.0 * d);
    float knots = pow(waveA, 7.0) * (0.25 + 0.75 * pow(waveB, 3.0));
    float braid = pow(waveB, 10.0) * 0.36;
    float envelope = exp(-3.5 * d * d) * (1.0 - smoothstep(0.72, 1.0, d));
    float rim = exp(-distanceToContour * distanceToContour / 1.4);
    float energy = (knots * 1.65 + braid + rim * 0.35 + 0.035) * envelope;
    float blendPhase = 0.5 + 0.5 * sin(phase + logAmplitude * 0.65 - t * 0.45);
    vec4 visibleColor = mix(uColorA, uColorB, blendPhase);
    visibleColor.rgb = mix(visibleColor.rgb, vec3(0.91, 0.97, 1.0), pow(knots, 3.0) * 0.45);

    vec2 uv = center / uResolution;
    vec2 visibilityWeights = espVisibility(sourcePixel, pixel, uInvProjection);
    float front = visibilityWeights.x * uVisibility.x * visibleColor.a;
    float back = visibilityWeights.y * uVisibility.y * uOccludedColor.a;
    vec3 emission = (espDamage(visibleColor.rgb, identity) * front + espDamage(uOccludedColor.rgb, identity) * back) * energy * uIntensity * agreement;
    float alpha = clamp((front + back) * energy * 0.42 * uIntensity * agreement, 0.0, 0.9);
    fragColor = vec4(emission, alpha);
}
