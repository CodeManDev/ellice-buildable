#version 330 core
#include <render3d/common/esp_targets.glsl>

uniform sampler2D uBloomTexture;
uniform sampler2D uNearestSeed;
uniform sampler2D uModelMask;
uniform sampler2D uModelDepth;
uniform sampler2D uSceneDepth;
uniform mat4 uInvProjection;
uniform float uOcclusionBias;
uniform float uVisibleEnabled;
uniform float uOccludedEnabled;
uniform vec4 uVisibleColor;
uniform vec4 uOccludedColor;
uniform float uStrength;
uniform float uCoreOpacity;

in vec2 vTexCoord;
out vec4 fragColor;

float viewDepth(vec2 uv, float rawDepth) {
    vec4 clip = vec4(uv * 2.0 - 1.0, rawDepth * 2.0 - 1.0, 1.0);
    vec4 view = uInvProjection * clip;
    float safeW = abs(view.w) < 0.00001
        ? (view.w < 0.0 ? -0.00001 : 0.00001)
        : view.w;
    return max(-view.z / safeW, 0.0);
}

float frontGate(vec2 uv, float effectRawDepth, float sceneRawDepth,
                float sceneViewDepth) {
    if (sceneRawDepth >= 0.999999) return 1.0;
    float effectViewDepth = viewDepth(uv, effectRawDepth);
    float bias = max(max(uOcclusionBias, 0.00001),
        effectViewDepth * 0.0025);
    return smoothstep(-bias, 0.0, sceneViewDepth - effectViewDepth);
}

vec2 regatedWeights(vec2 uv, float sceneRawDepth, float sceneViewDepth,
                    vec4 packed) {
    float visibleWeight = max(packed.r, 0.0);
    float occludedWeight = max(packed.b, 0.0);
    float visibleGate = 0.0;
    float occludedGate = 0.0;

    if (visibleWeight > 0.0) {
        float visibleProximity = clamp(
            packed.g / visibleWeight, 0.0, 1.0);
        visibleGate = frontGate(uv, 1.0 - visibleProximity,
            sceneRawDepth, sceneViewDepth);
    }
    if (occludedWeight > 0.0) {
        float occludedProximity = clamp(
            packed.a / occludedWeight, 0.0, 1.0);
        occludedGate = 1.0 - frontGate(uv, 1.0 - occludedProximity,
            sceneRawDepth, sceneViewDepth);
    }

    return vec2(visibleWeight * visibleGate
            * clamp(uVisibleEnabled, 0.0, 1.0),
        occludedWeight * occludedGate
            * clamp(uOccludedEnabled, 0.0, 1.0));
}

void main() {
    ivec2 nativeSize = textureSize(uSceneDepth, 0);
    ivec2 nativePixel = clamp(ivec2(gl_FragCoord.xy), ivec2(0),
        nativeSize - ivec2(1));
    vec2 nativeUv = (vec2(nativePixel) + vec2(0.5)) / vec2(nativeSize);
    if (espForeground(nativePixel) || texelFetch(uModelMask, nativePixel, 0).r > 0.001) {
        
        fragColor = vec4(0.0);
        return;
    }
    float sceneRawDepth = texelFetch(uSceneDepth, nativePixel, 0).r;
    float sceneViewDepth = sceneRawDepth < 0.999999
        ? viewDepth(nativeUv, sceneRawDepth)
        : 0.0;

    vec4 packedBloom = texture(uBloomTexture, nativeUv);
    vec2 bloomWeights = regatedWeights(nativeUv, sceneRawDepth,
        sceneViewDepth, packedBloom);
    vec4 visibleColor = clamp(uVisibleColor, 0.0, 1.0);
    vec4 occludedColor = clamp(uOccludedColor, 0.0, 1.0);
    if (uDamageColor.a > 0.0) {
        vec4 seed = texelFetch(uNearestSeed, nativePixel, 0);
        if (seed.b > 0.0) {
            visibleColor.rgb = espDamage(visibleColor.rgb, seed.a);
            occludedColor.rgb = espDamage(occludedColor.rgb, seed.a);
        }
    }
    float strength = max(uStrength, 0.0);
    vec3 haloRgb = visibleColor.rgb
            * (bloomWeights.x * visibleColor.a * strength)
        + occludedColor.rgb
            * (bloomWeights.y * occludedColor.a * strength);

    
    float closestSquared = 100.0;
    ivec2 sourcePixel = nativePixel;
    if (uCoreOpacity > 0.0) for (int y = -2; y <= 2; y++) {
        for (int x = -2; x <= 2; x++) {
            ivec2 neighbour = nativePixel + ivec2(x, y);
            if (any(lessThan(neighbour, ivec2(0))) || any(greaterThanEqual(neighbour, nativeSize))) continue;
            float squared = float(x * x + y * y);
            if (squared < closestSquared && texelFetch(uModelMask, neighbour, 0).r > 0.001) {
                closestSquared = squared;
                sourcePixel = neighbour;
            }
        }
    }
    float modelCoverage = 1.0 - smoothstep(0.5, 2.25, sqrt(closestSquared));
    float modelRawDepth = texelFetch(uModelDepth, sourcePixel, 0).r;
    float modelVisibleGate = frontGate(nativeUv, modelRawDepth,
        sceneRawDepth, sceneViewDepth);
    vec2 coreVisibility = espVisibility(sourcePixel, nativePixel, uInvProjection);
    float coreOpacity = clamp(uCoreOpacity, 0.0, 1.0);
    float visibleCoreAlpha = modelCoverage * coreVisibility.x
        * clamp(uVisibleEnabled, 0.0, 1.0)
        * visibleColor.a * coreOpacity;
    float occludedCoreAlpha = modelCoverage * coreVisibility.y
        * clamp(uOccludedEnabled, 0.0, 1.0)
        * occludedColor.a * coreOpacity;
    vec3 corePremultiplied = visibleColor.rgb * visibleCoreAlpha
        + occludedColor.rgb * occludedCoreAlpha;
    float coreAlpha = clamp(visibleCoreAlpha + occludedCoreAlpha, 0.0, 1.0);

    
    
    vec4 haloLayer = vec4(haloRgb, 0.0);
    vec4 coreLayer = vec4(corePremultiplied, coreAlpha);
    fragColor = haloLayer + coreLayer;
}
