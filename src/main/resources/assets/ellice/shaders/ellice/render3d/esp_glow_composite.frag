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
uniform vec4 uVisibleColor;
uniform vec4 uOccludedColor;
uniform float uRadius;
uniform float uIntensity;
uniform float uHighlights;

in vec2 vTexCoord;
out vec4 fragColor;

float viewDepth(vec2 uv, float raw) {
    vec4 view = uInvProjection * vec4(uv * 2.0 - 1.0, raw * 2.0 - 1.0, 1.0);
    return max(-view.z / (abs(view.w) < 0.00001 ? 0.00001 : view.w), 0.0);
}

vec4 overLight(vec4 under, vec3 color, float coverage, float opacity) {
    float alpha = (1.0 - pow(1.0 - clamp(coverage, 0.0, 0.995), uIntensity)) * opacity;
    return vec4(color * alpha + under.rgb * (1.0 - alpha), alpha + under.a * (1.0 - alpha));
}
void main() {
    ivec2 size = textureSize(uPlayerMask, 0);
    ivec2 pixel = clamp(ivec2(gl_FragCoord.xy), ivec2(0), size - ivec2(1));
    vec2 center = vec2(pixel) + 0.5;
    vec4 seed = texelFetch(uNearestSeed, pixel, 0);
    float d = max(length(seed.rg) - 0.5, 0.0);
    
    float aa = clamp(fwidth(d), 0.65, 1.15);
    if (espForeground(pixel) || texelFetch(uPlayerMask, pixel, 0).r > 0.001 || seed.b <= 0.0 || d > uRadius + 1.0) discard;
    vec2 sourceCenter = center + seed.rg;
    if (any(lessThan(sourceCenter, uRegionMin + 0.5)) || any(greaterThan(sourceCenter, uRegionMax + 0.5))) discard;
    ivec2 sourcePixel = clamp(ivec2(floor(sourceCenter)), ivec2(0), size - ivec2(1));
    vec2 mask = texelFetch(uPlayerMask, sourcePixel, 0).rg;
    if (mask.r <= 0.001) discard;
    float identity = clamp(mask.g / max(mask.r, 0.0001), 0.0, 1.0);
    float agreement = 1.0 - smoothstep(0.001, 0.003, abs(identity - seed.a));

    vec2 visibilityWeights = espVisibility(sourcePixel, pixel, uInvProjection);
    float front = visibilityWeights.x * uVisibility.x * uVisibleColor.a;
    float back = visibilityWeights.y * uVisibility.y * uOccludedColor.a;
    float opacity = clamp((front + back) * agreement, 0.0, 1.0);
    vec3 tint = (uVisibleColor.rgb * front + uOccludedColor.rgb * back) / max(front + back, 0.00001);

    tint = espDamage(tint, identity);

    
    
    float cutoff = 1.0 - smoothstep(uRadius - 1.0, uRadius + 0.5, d);
    float skirt = exp(-2.3 * pow(d / uRadius, 2.0)) * cutoff;
    float halo = exp(-pow(d / max(uRadius * 0.40, 1.0), 2.0)) * cutoff;
    float edge = 1.0 - smoothstep(1.35 - aa * 0.5, 1.35 + aa * 0.5, d);
    float pearl = 1.0 - smoothstep(0.1, 0.65 + aa * 0.55, d);
    vec3 saturated = tint * (0.84 + 0.16 * tint);
    vec3 luminous = mix(tint, vec3(1.0), 0.82 * uHighlights);
    vec4 light = overLight(vec4(0.0), saturated, skirt * 0.16, opacity);
    light = overLight(light, tint, halo * 0.48, opacity);
    light = overLight(light, tint, edge * 0.88, opacity);
    light = overLight(light, luminous, pearl * 0.92, opacity);
    fragColor = light;
}
