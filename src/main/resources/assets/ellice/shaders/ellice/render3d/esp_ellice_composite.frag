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

float viewDepth(vec2 uv, float raw) {
    vec4 view = uInvProjection * vec4(uv * 2.0 - 1.0, raw * 2.0 - 1.0, 1.0);
    return max(-view.z / max(abs(view.w), 0.00001), 0.0);
}
float hash(vec2 p) { return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453); }
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
    float phase = espTarget(identity).r * 6.2831853;
    float d = distanceToContour / uWidth;

    
    
    vec2 p = (center - (uRegionMin + uRegionMax) * 0.5) / max(uRegionMax.y - uRegionMin.y, 1.0);
    vec2 lattice = mat2(1.0, 0.0, -0.5773503, 1.1547005) * p * (5.0 + uComplexity * 5.0);
    vec2 cell = floor(lattice), local = fract(lattice);
    float flip = step(1.0, local.x + local.y);
    vec3 bary = flip < 0.5 ? vec3(local, 1.0 - local.x - local.y) : vec3(1.0 - local, local.x + local.y - 1.0);
    float facet = hash(cell + vec2(flip * 19.0, phase));
    vec2 contourNormal = -seed.rg / max(length(seed.rg), 0.0001);
    vec3 normal = normalize(vec3(contourNormal * 0.46 + vec2(cos(facet * 6.283), sin(facet * 6.283)) * 0.35, 0.8));
    vec3 light = normalize(vec3(cos(uTime * 0.73 + phase), sin(uTime * 0.51 + 0.6), 0.9));
    float incidence = clamp(dot(normal, light), 0.0, 1.0);
    float specular = pow(max(dot(normal, normalize(light + vec3(0.0, 0.0, 1.0))), 0.0), 36.0);

    
    float opticalPath = (0.40 + facet * 0.28 + d * 0.19) * (0.65 + incidence * 0.35);
    vec3 film = 0.5 + 0.5 * cos(6.2831853 * opticalPath / vec3(0.650, 0.510, 0.475));
    vec4 palette = mix(uColorA, uColorB, clamp(film.r * 0.65 + film.b * 0.35, 0.0, 1.0));
    vec3 color = palette.rgb * (0.70 + dot(film, vec3(0.2126, 0.7152, 0.0722)) * 0.30);
    color = mix(color, vec3(0.96, 0.99, 1.0), specular * 0.78);
    float facetEdge = exp(-min(bary.x, min(bary.y, bary.z)) * 65.0);
    float rim = exp(-distanceToContour * distanceToContour / 1.8);
    float envelope = exp(-2.4 * d * d) * (1.0 - smoothstep(0.78, 1.0, d));
    float energy = (0.32 + incidence * 0.48 + specular * 0.60 + facetEdge * 0.19 + rim * 0.35) * envelope;

    vec2 visibilityWeights = espVisibility(sourcePixel, pixel, uInvProjection);
    float front = visibilityWeights.x * uVisibility.x * palette.a;
    float back = visibilityWeights.y * uVisibility.y * uOccludedColor.a;
    vec3 rgb = espDamage(color, identity) * front + espDamage(uOccludedColor.rgb, identity) * back;
    float alpha = clamp((front + back) * energy * uIntensity * agreement, 0.0, 0.96);
    fragColor = vec4(rgb / max(front + back, 0.00001) * alpha, alpha);
}
