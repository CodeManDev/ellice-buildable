#version 330 core
#include <render3d/common/esp_targets.glsl>

uniform sampler2D uSceneColor;
uniform sampler2D uSceneBlur;
uniform sampler2D uSharpFire;
uniform sampler2D uBlurFire;
uniform sampler2D uSceneDepth;
uniform sampler2D uPlayerMask;
uniform sampler2D uNearestSeed;
uniform vec2 uResolution;
uniform mat4 uInvProjection;
uniform float uTime;
uniform float uBloomStrength;
uniform float uCoreStrength;
uniform float uHeatPixels;
uniform float uBackdropBlur;
uniform float uOcclusionBias;
uniform float uPerPlayerColor;
uniform vec3 uLowColor;
uniform vec3 uMidColor;
uniform vec3 uHotColor;
uniform vec3 uOccludedColor;

in vec2 vTexCoord;
out vec4 fragColor;

const mat2 FLOW_ROTATION = mat2(0.80, 0.60, -0.60, 0.80);

float hash12(vec2 point) {
    vec3 p = fract(vec3(point.xyx) * 0.1031);
    p += dot(p, p.yzx + 33.33);
    return fract((p.x + p.y) * p.z);
}

float valueNoise(vec2 point) {
    vec2 cell = floor(point);
    vec2 local = fract(point);
    local = local * local * (3.0 - 2.0 * local);
    float a = hash12(cell);
    float b = hash12(cell + vec2(1.0, 0.0));
    float c = hash12(cell + vec2(0.0, 1.0));
    float d = hash12(cell + vec2(1.0, 1.0));
    return mix(mix(a, b, local.x), mix(c, d, local.x), local.y);
}

float flowNoise(vec2 point) {
    float result = 0.0;
    float amplitude = 0.55;
    for (int octave = 0; octave < 3; ++octave) {
        result += valueNoise(point) * amplitude;
        point = FLOW_ROTATION * point * 2.07 + vec2(9.13, 5.71);
        amplitude *= 0.47;
    }
    return result;
}




vec3 oklchToLinearSrgb(float lightness, float chroma, float hue) {
    float angle = hue * 6.28318530718;
    float a = chroma * cos(angle);
    float b = chroma * sin(angle);
    float l = lightness + 0.3963377774 * a + 0.2158037573 * b;
    float m = lightness - 0.1055613458 * a - 0.0638541728 * b;
    float s = lightness - 0.0894841775 * a - 1.2914855480 * b;
    l = l * l * l;
    m = m * m * m;
    s = s * s * s;
    return max(vec3(
        4.0767416621 * l - 3.3077115913 * m + 0.2309699292 * s,
       -1.2684380046 * l + 2.6097574011 * m - 0.3413193965 * s,
       -0.0041960863 * l - 0.7034186147 * m + 1.7076147010 * s),
        vec3(0.0));
}

void uuidPalette(float identity, out vec3 lowColor, out vec3 midColor,
                 out vec3 hotColor, out vec3 occludedColor) {
    float hue = hash12(vec2(identity * 4093.0 + 17.1,
        identity * 8191.0 + 53.7));
    lowColor = oklchToLinearSrgb(0.43, 0.135, fract(hue + 0.035));
    midColor = oklchToLinearSrgb(0.70, 0.185, hue);
    hotColor = oklchToLinearSrgb(0.92, 0.060, fract(hue - 0.025));
    occludedColor = oklchToLinearSrgb(0.55, 0.145,
        fract(hue + 0.065));
}

float viewDepth(vec2 uv, float depth) {
    vec4 clip = vec4(uv * 2.0 - 1.0, depth * 2.0 - 1.0, 1.0);
    vec4 view = uInvProjection * clip;
    float safeW = abs(view.w) < 0.00001
        ? (view.w < 0.0 ? -0.00001 : 0.00001)
        : view.w;
    return max(-view.z / safeW, 0.0);
}

float frontGate(vec2 uv, float fireRawDepth, float sceneRawDepth,
                float sceneViewDepth) {
    if (sceneRawDepth >= 0.999999) return 1.0;
    float fireViewDepth = viewDepth(uv, fireRawDepth);
    float bias = max(max(uOcclusionBias, 0.0), fireViewDepth * 0.0025);
    return smoothstep(-bias, 0.0, sceneViewDepth - fireViewDepth);
}

vec2 regatedWeights(vec2 uv, float sceneRawDepth, float sceneViewDepth,
                    vec4 packedFire) {
    float visibleWeight = max(packedFire.r, 0.0);
    float occludedWeight = max(packedFire.b, 0.0);
    float visibleGate = 0.0;
    float occludedGate = 0.0;

    if (visibleWeight > 0.00001) {
        float visibleRawDepth = 1.0
            - clamp(packedFire.g / visibleWeight, 0.0, 1.0);
        visibleGate = frontGate(uv, visibleRawDepth, sceneRawDepth,
            sceneViewDepth);
    }
    if (occludedWeight > 0.00001) {
        float occludedRawDepth = 1.0
            - clamp(packedFire.a / occludedWeight, 0.0, 1.0);
        occludedGate = 1.0 - frontGate(uv, occludedRawDepth, sceneRawDepth,
            sceneViewDepth);
    }
    return vec2(visibleWeight * visibleGate, occludedWeight * occludedGate);
}

vec3 firePalette(float heat, vec3 lowColor, vec3 midColor, vec3 hotColor) {
    vec3 lowToMid = mix(lowColor, midColor, smoothstep(0.02, 0.58, heat));
    return mix(lowToMid, hotColor, smoothstep(0.48, 1.0, heat));
}



float samplePlayerCoverageTent(vec2 uv, vec2 resolution) {
    vec2 texel = 1.0 / resolution;
    vec2 offsetX = vec2(texel.x * 0.45, 0.0);
    vec2 offsetY = vec2(0.0, texel.y * 0.45);
    float coverage = texture(uPlayerMask, uv).r * 0.50;
    coverage += texture(uPlayerMask, uv - offsetX).r * 0.125;
    coverage += texture(uPlayerMask, uv + offsetX).r * 0.125;
    coverage += texture(uPlayerMask, uv - offsetY).r * 0.125;
    coverage += texture(uPlayerMask, uv + offsetY).r * 0.125;
    return coverage;
}




vec2 nearestPlayerIdentity(ivec2 pixel) {
    ivec2 seedSize = textureSize(uNearestSeed, 0);
    ivec2 boundedPixel = clamp(pixel, ivec2(0), seedSize - ivec2(1));
    vec4 nearest = texelFetch(uNearestSeed, boundedPixel, 0);
    if (nearest.b <= 0.0) return vec2(0.0);

    vec2 sourceCenter = vec2(boundedPixel) + vec2(0.5) + nearest.rg;
    ivec2 maskSize = textureSize(uPlayerMask, 0);
    ivec2 sourcePixel = clamp(ivec2(floor(sourceCenter)),
        ivec2(0), maskSize - ivec2(1));
    vec2 mask = texelFetch(uPlayerMask, sourcePixel, 0).rg;
    if (mask.r <= 0.001) return vec2(0.0);
    float currentIdentity = clamp(mask.g / max(mask.r, 0.0001), 0.0, 1.0);
    float agreement = 1.0 - smoothstep(0.001, 0.003,
        abs(currentIdentity - nearest.a));
    return vec2(nearest.a, agreement);
}




vec3 toneMapHuePreserving(vec3 radiance) {
    vec3 positive = max(radiance, vec3(0.0));
    float peak = max(positive.r, max(positive.g, positive.b));
    if (peak <= 0.000001) return vec3(0.0);
    float exposedPeak = peak * 1.35;
    float mappedPeak = exposedPeak / (1.0 + exposedPeak);
    return positive * (mappedPeak / peak);
}

void main() {
    if (espForeground(ivec2(gl_FragCoord.xy))) {
        fragColor = texelFetch(uSceneColor, ivec2(gl_FragCoord.xy), 0);
        return;
    }
    vec2 resolution = max(uResolution, vec2(1.0));
    vec2 halfTexel = 0.5 / resolution;
    vec2 uv = clamp(vTexCoord, halfTexel, vec2(1.0) - halfTexel);
    vec4 scene = texture(uSceneColor, uv);
    ivec2 sharpSize = textureSize(uSharpFire, 0);
    ivec2 sharpPixel = clamp(ivec2(gl_FragCoord.xy),
        ivec2(0), sharpSize - ivec2(1));
    vec4 sharpPacked = texelFetch(uSharpFire, sharpPixel, 0);
    bool needsBlurredFire = uBloomStrength > 0.0
        || uBackdropBlur > 0.0;
    vec4 blurPacked = needsBlurredFire
        ? texture(uBlurFire, uv)
        : vec4(0.0);
    if (dot(max(sharpPacked, vec4(0.0)), vec4(1.0))
        + dot(max(blurPacked, vec4(0.0)), vec4(1.0)) <= 0.00002) {
        fragColor = scene;
        return;
    }

    
    
    
    float playerCoverage = samplePlayerCoverageTent(uv, resolution);
    
    
    
    float softInterior = smoothstep(0.04, 0.96,
        clamp(playerCoverage, 0.0, 1.0));
    float interiorCoreTransmission = mix(1.0, 0.72, softInterior);
    float interiorBloomTransmission = mix(1.0, 0.07, softInterior);

    vec3 lowColor = max(uLowColor, vec3(0.0));
    vec3 midColor = max(uMidColor, vec3(0.0));
    vec3 hotColor = max(uHotColor, vec3(0.0));
    vec3 resolvedOccludedColor = max(uOccludedColor, vec3(0.0));
    if (uPerPlayerColor > 0.5) {
        vec2 identity = nearestPlayerIdentity(sharpPixel);
        if (identity.y > 0.001) {
            vec3 playerLow;
            vec3 playerMid;
            vec3 playerHot;
            vec3 playerOccluded;
            uuidPalette(espTarget(identity.x).r, playerLow, playerMid,
                playerHot, playerOccluded);
            lowColor = mix(lowColor, playerLow, identity.y);
            midColor = mix(midColor, playerMid, identity.y);
            hotColor = mix(hotColor, playerHot, identity.y);
            resolvedOccludedColor = mix(resolvedOccludedColor,
                playerOccluded, identity.y);
        }
    }

    vec2 hitIdentity = nearestPlayerIdentity(sharpPixel);
    if (hitIdentity.y > 0.001) {
        lowColor = espDamage(lowColor, hitIdentity.x);
        midColor = espDamage(midColor, hitIdentity.x);
        hotColor = espDamage(hotColor, hitIdentity.x);
        resolvedOccludedColor = espDamage(resolvedOccludedColor, hitIdentity.x);
    }
    float rawSceneDepth = texture(uSceneDepth, uv).r;
    float sceneViewDepth = rawSceneDepth < 0.999999
        ? viewDepth(uv, rawSceneDepth)
        : 0.0;
    vec2 sharp = regatedWeights(uv, rawSceneDepth, sceneViewDepth, sharpPacked)
        * interiorCoreTransmission;
    vec2 bloom = regatedWeights(uv, rawSceneDepth, sceneViewDepth, blurPacked)
        * interiorBloomTransmission;
    float visibleCore = sharp.x;
    float occludedCore = sharp.y;
    float visibleBloom = bloom.x;
    float occludedBloom = bloom.y;

    float totalEnergy = visibleCore + occludedCore
        + visibleBloom * max(uBloomStrength, 0.0)
        + occludedBloom * max(uBloomStrength, 0.0);
    float localCoverage = 1.0 - exp(-max(totalEnergy, 0.0) * 1.45);
    if (localCoverage <= 0.00002) {
        fragColor = scene;
        return;
    }

    float interiorHeatTransmission = mix(1.0, 0.28, softInterior);
    float heatPixels = clamp(uHeatPixels, 0.0, 24.0) * localCoverage
        * interiorHeatTransmission;
    vec2 distortedUv = uv;
    if (heatPixels > 0.0) {
        float flowA = flowNoise(uv * vec2(9.0, 14.0)
            + vec2(0.0, -uTime * 0.72));
        float flowB = flowNoise(FLOW_ROTATION * uv * vec2(13.0, 8.0)
            + vec2(uTime * 0.37, -uTime * 0.49) + 11.7);
        vec2 heatVector = vec2(flowA - 0.5, 0.18 + flowB * 0.82);
        distortedUv = clamp(uv + heatVector * heatPixels / resolution,
            halfTexel, vec2(1.0) - halfTexel);
    }

    vec3 sharpBackdrop = texture(uSceneColor, distortedUv).rgb;
    float blurCoverage = 1.0 - exp(-max(
        visibleBloom + occludedBloom, 0.0) * 2.25);
    float interiorBackdropTransmission = mix(1.0, 0.22, softInterior);
    float backdropMix = clamp(uBackdropBlur, 0.0, 1.0) * blurCoverage
        * interiorBackdropTransmission;
    vec3 softBackdrop = sharpBackdrop;
    if (backdropMix > 0.0) {
        softBackdrop = texture(uSceneBlur, distortedUv).rgb;
    }
    vec3 backdrop = mix(sharpBackdrop, softBackdrop, backdropMix);
    backdrop *= 1.0 - 0.075 * blurCoverage;

    float coreStrength = max(uCoreStrength, 0.0) * 1.18;
    float bloomStrength = max(uBloomStrength, 0.0) * 1.12;
    float visibleHeat = 1.0 - exp(-visibleCore * coreStrength * 1.35);
    vec3 visibleColor = firePalette(visibleHeat,
        lowColor, midColor, hotColor);
    vec3 bloomColor = firePalette(
        clamp(visibleHeat * 0.62 + 0.12, 0.0, 1.0),
        lowColor, midColor, hotColor);
    vec3 visibleRadiance = visibleColor * visibleCore * coreStrength
        + bloomColor * visibleBloom * bloomStrength;

    vec3 occludedRadiance = resolvedOccludedColor
        * (occludedCore * coreStrength + occludedBloom * bloomStrength * 0.82);
    vec3 radiance = max(visibleRadiance + occludedRadiance, vec3(0.0));
    vec3 emission = toneMapHuePreserving(radiance);
    vec3 color = backdrop + emission * (vec3(1.0) - backdrop);

    fragColor = vec4(clamp(color, vec3(0.0), vec3(1.0)), scene.a);
}
