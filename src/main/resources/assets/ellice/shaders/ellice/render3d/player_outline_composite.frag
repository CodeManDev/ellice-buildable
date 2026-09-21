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
uniform float uThickness;
uniform float uSoftness;
uniform float uOcclusionBias;

in vec2 vTexCoord;
out vec4 fragColor;

float viewDepth(vec2 uv, float depth) {
    vec4 clip = vec4(uv * 2.0 - 1.0, depth * 2.0 - 1.0, 1.0);
    vec4 view = uInvProjection * clip;
    float safeW = abs(view.w) < 0.00001
        ? (view.w < 0.0 ? -0.00001 : 0.00001)
        : view.w;
    return max(-view.z / safeW, 0.0);
}

float sourceVisibility(vec2 fragmentUv, vec2 sourceUv,
                       float sourceRawDepth, float sceneRawDepth) {
    if (sceneRawDepth >= 0.999999) return 1.0;
    float sourceViewDepth = viewDepth(sourceUv, sourceRawDepth);
    float sceneViewDepth = viewDepth(fragmentUv, sceneRawDepth);
    float bias = max(max(uOcclusionBias, 0.0),
        sourceViewDepth * 0.0025);
    return smoothstep(-bias, 0.0,
        sceneViewDepth - sourceViewDepth);
}

void main() {
    ivec2 resolution = textureSize(uNearestSeed, 0);
    ivec2 pixel = clamp(ivec2(gl_FragCoord.xy),
        ivec2(0), resolution - ivec2(1));
    vec2 fragmentCenter = vec2(pixel) + vec2(0.5);
    vec4 nearest = texelFetch(uNearestSeed, pixel, 0);
    float exteriorDistance = max(length(nearest.rg) - 0.5, 0.0);
    
    
    float derivativeAa = clamp(fwidth(exteriorDistance), 0.65, 1.25);

    
    
    if (espForeground(pixel) || texelFetch(uPlayerMask, pixel, 0).r > 0.001) discard;

    if (nearest.b <= 0.0) discard;
    vec2 sourceCenter = fragmentCenter + nearest.rg;
    if (any(lessThan(sourceCenter, uRegionMin + vec2(0.5)))
        || any(greaterThan(sourceCenter, uRegionMax + vec2(0.5)))) {
        discard;
    }

    
    
    
    ivec2 sourcePixel = clamp(ivec2(floor(sourceCenter)),
        ivec2(0), resolution - ivec2(1));
    vec2 sourceMask = texelFetch(uPlayerMask, sourcePixel, 0).rg;
    if (sourceMask.r <= 0.001) discard;
    float currentIdentity = clamp(
        sourceMask.g / max(sourceMask.r, 0.0001), 0.0, 1.0);
    float identityAgreement = 1.0 - smoothstep(0.001, 0.003,
        abs(currentIdentity - nearest.a));
    if (identityAgreement <= 0.001) discard;

    float innerEdge = max(uThickness - derivativeAa * 0.5, 0.0);
    float outerEdge = uThickness + max(uSoftness, 0.0) + derivativeAa * 0.5;
    float bandCoverage = 1.0 - smoothstep(innerEdge, outerEdge,
        exteriorDistance);
    bandCoverage *= identityAgreement;
    
    bandCoverage *= mix(1.0, 0.72, smoothstep(0.0, max(uThickness, 0.5), exteriorDistance));
    if (bandCoverage <= 0.0001) discard;

    vec2 safeResolution = max(uResolution, vec2(1.0));
    vec2 fragmentUv = fragmentCenter / safeResolution;
    vec2 sourceUv = sourceCenter / safeResolution;
    float sceneRawDepth = texelFetch(uSceneDepth, pixel, 0).r;
    float sourceRawDepth = 1.0 - clamp(nearest.b, 0.0, 1.0);
    float visibleGate = sourceVisibility(fragmentUv, sourceUv,
        sourceRawDepth, sceneRawDepth);

    vec2 visibilityWeights = espVisibility(sourcePixel, pixel, uInvProjection);
    float visibleAlpha = uVisibleColor.a * clamp(uVisibility.x, 0.0, 1.0)
        * visibilityWeights.x;
    float occludedAlpha = uOccludedColor.a * clamp(uVisibility.y, 0.0, 1.0)
        * visibilityWeights.y;
    float alpha = (visibleAlpha + occludedAlpha) * bandCoverage;
    if (alpha <= 0.0001) discard;

    vec3 premultiplied = (espDamage(uVisibleColor.rgb, currentIdentity) * visibleAlpha
        + espDamage(uOccludedColor.rgb, currentIdentity) * occludedAlpha) * bandCoverage;
    fragColor = vec4(premultiplied, alpha);
}
