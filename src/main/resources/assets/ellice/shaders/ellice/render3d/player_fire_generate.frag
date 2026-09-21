#version 330 core
#include <render3d/common/esp_targets.glsl>

uniform sampler2D uNearestSeed;
uniform sampler2D uPlayerMask;
uniform sampler2D uSceneDepth;
uniform mat4 uInvProjection;
uniform vec2 uFieldResolution;
uniform vec2 uModelResolution;
uniform vec2 uRegionMin;
uniform vec2 uRegionMax;
uniform float uProjectionScaleY;
uniform float uTime;
uniform float uIntensity;
uniform float uFlameHeight;
uniform float uFlameWidth;
uniform float uVisibleOpacity;
uniform float uThroughOpacity;

in vec2 vTexCoord;
out vec4 fragColor;

const mat2 FLOW_ROTATION = mat2(0.80, 0.60, -0.60, 0.80);
const float MAX_SCREEN_FLOW = 320.0;

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

float fbm(vec2 point) {
    float result = 0.0;
    float amplitude = 0.55;
    for (int octave = 0; octave < 3; ++octave) {
        result += valueNoise(point) * amplitude;
        point = FLOW_ROTATION * point * 2.04 + vec2(11.31, 7.17);
        amplitude *= 0.48;
    }
    return result;
}




float emitterField(vec2 sourcePixel, float identity, float time) {
    vec2 point = sourcePixel * vec2(0.115, 0.085)
        + vec2(identity * 17.3 + time * 0.11,
            identity * 11.7 - time * 0.19);
    float island = valueNoise(point);
    float filament = valueNoise(FLOW_ROTATION * point * 2.35
        + vec2(9.1 + time * 0.07, 4.7 - time * 0.13));
    float phase = valueNoise(point * 0.37 + identity * 8.3) * 6.2831853;
    float pulse = 0.5 + 0.5 * sin(time * 1.43 + phase);
    float signal = island * 0.62 + filament * 0.28 + pulse * 0.10;
    float primaryEmitter = smoothstep(0.56, 0.73, signal);

    
    
    
    vec2 secondaryPoint = FLOW_ROTATION * point * 1.73
        + vec2(31.7 + identity * 7.1 - time * 0.08,
            18.9 + time * 0.14);
    float secondaryIsland = valueNoise(secondaryPoint);
    float secondaryFilament = valueNoise(
        FLOW_ROTATION * secondaryPoint * 1.91 + vec2(3.7, 12.1));
    float secondarySignal = secondaryIsland * 0.72
        + secondaryFilament * 0.28;
    float secondaryEmitter = smoothstep(0.63, 0.80, secondarySignal);
    return max(primaryEmitter, secondaryEmitter * 0.88);
}




float sparkLayer(vec2 fragmentPixel, float identity, float time,
                 float effectiveWidth, vec2 inertialDrift) {
    
    
    
    float cellSize = 8.5;
    float riseSpeed = 24.0;
    float salt = 31.7;
    vec2 flowPixel = fragmentPixel - vec2(0.0, time * riseSpeed);
    vec2 baseCell = floor(flowPixel / cellSize);
    float result = 0.0;
    for (int y = -1; y <= 1; ++y) {
        for (int x = -1; x <= 1; ++x) {
            vec2 cell = baseCell + vec2(x, y);
            vec2 keyedCell = cell + vec2(identity * 127.1 + salt,
                identity * 83.7 - salt * 0.37);
            float randomValue = hash12(keyedCell);
            float populationMix = hash12(keyedCell + vec2(13.1, 53.7));
            vec2 jitter = vec2(
                hash12(keyedCell + vec2(19.1, 7.7)),
                hash12(keyedCell + vec2(43.7, 29.3)));
            float curlAmount = mix(0.55, 2.35,
                hash12(keyedCell + vec2(71.9, 11.3)));
            float curlWidthFactor = 0.82 + effectiveWidth * 0.42;
            float lateralCurl = sin(time * mix(0.78, 1.36, randomValue)
                + cell.y * 0.61 + randomValue * 6.2831853)
                * curlAmount * curlWidthFactor;
            float verticalFlutter = sin(time * mix(0.91, 1.83,
                populationMix) + randomValue * 9.7)
                * mix(0.18, 1.15, populationMix);
            vec2 center = (cell + mix(vec2(0.22), vec2(0.78), jitter))
                * cellSize + vec2(lateralCurl,
                    time * riseSpeed + verticalFlutter);
            
            
            
            center += inertialDrift * mix(0.38, 1.0, randomValue);
            float radius = mix(0.34, 1.04,
                hash12(keyedCell + vec2(5.3, 61.1)))
                * mix(0.86, 1.08, populationMix);
            float distanceToSpark = length(fragmentPixel - center);
            
            
            float sparkAa = 0.52;
            float disc = 1.0 - smoothstep(radius - sparkAa,
                radius + sparkAa, distanceToSpark);
            float presence = smoothstep(0.64, 0.81, randomValue);
            float twinkle = mix(0.62, 1.0, 0.5 + 0.5 * sin(
                time * 2.1 + randomValue * 13.7 + salt));
            result = max(result, disc * presence * twinkle);
        }
    }
    return result;
}

float viewDepth(vec2 uv, float depth) {
    vec4 clip = vec4(uv * 2.0 - 1.0, depth * 2.0 - 1.0, 1.0);
    vec4 view = uInvProjection * clip;
    float safeW = abs(view.w) < 0.00001
        ? (view.w < 0.0 ? -0.00001 : 0.00001)
        : view.w;
    return max(-view.z / safeW, 0.0);
}

float sourceVisibility(vec2 sourceUv, float proximity,
                       float rawSceneDepth, float sceneViewDepth) {
    if (rawSceneDepth >= 0.999999) return 1.0;
    float sourceViewDepth = viewDepth(sourceUv, 1.0 - proximity);
    float bias = max(0.045, sourceViewDepth * 0.0025);
    return smoothstep(-bias, 0.0, sceneViewDepth - sourceViewDepth);
}

vec4 packDepthSeparated(float weight, float visibility, float proximity) {
    float visibleWeight = weight * visibility
        * clamp(uVisibleOpacity, 0.0, 1.0);
    float occludedWeight = weight * (1.0 - visibility)
        * clamp(uThroughOpacity, 0.0, 1.0);
    return vec4(visibleWeight, proximity * visibleWeight,
        occludedWeight, proximity * occludedWeight);
}

float pixelsPerBlock(float proximity, vec2 sourceUv) {
    float depth = viewDepth(sourceUv, 1.0 - proximity);
    return abs(uProjectionScaleY) * max(uFieldResolution.y, 1.0)
        / max(2.0 * depth, 0.0001);
}

vec2 samplePlayerMaskTent(vec2 uv) {
    vec2 texel = 1.0 / max(uModelResolution, vec2(1.0));
    vec2 offsetX = vec2(texel.x * 0.45, 0.0);
    vec2 offsetY = vec2(0.0, texel.y * 0.45);
    vec2 sampleValue = texture(uPlayerMask, uv).rg * 0.50;
    sampleValue += texture(uPlayerMask, uv - offsetX).rg * 0.125;
    sampleValue += texture(uPlayerMask, uv + offsetX).rg * 0.125;
    sampleValue += texture(uPlayerMask, uv - offsetY).rg * 0.125;
    sampleValue += texture(uPlayerMask, uv + offsetY).rg * 0.125;
    return sampleValue;
}




vec2 sourceScreenMotion(vec2 sourcePixel) {
    ivec2 resolution = textureSize(uPlayerMask, 0);
    ivec2 pixel = clamp(ivec2(floor(sourcePixel)),
        ivec2(0), resolution - ivec2(1));
    vec2 encodedMotion = texelFetch(uPlayerMask, pixel, 0).ba;
    return clamp((encodedMotion * 255.0 - 128.0)
        * (MAX_SCREEN_FLOW / 127.0),
        vec2(-MAX_SCREEN_FLOW), vec2(MAX_SCREEN_FLOW));
}




bool nearestSeedAt(vec2 queryPixel, out vec2 seedPixel,
                   out float proximity, out float identity) {
    ivec2 resolution = textureSize(uNearestSeed, 0);
    ivec2 regionMin = max(ivec2(uRegionMin), ivec2(0));
    ivec2 regionMax = min(ivec2(uRegionMax), resolution - ivec2(1));
    vec2 bounded = clamp(queryPixel, vec2(regionMin) + vec2(0.5),
        vec2(regionMax) + vec2(0.5));
    ivec2 lower = ivec2(floor(bounded - vec2(0.5)));
    float bestDistanceSquared = 1.0e20;
    bool found = false;
    seedPixel = vec2(0.0);
    proximity = 0.0;
    identity = 0.0;
    for (int y = 0; y <= 1; ++y) {
        for (int x = 0; x <= 1; ++x) {
            ivec2 samplePixel = clamp(lower + ivec2(x, y),
                regionMin, regionMax);
            vec4 packed = texelFetch(uNearestSeed, samplePixel, 0);
            if (packed.b <= 0.0) continue;
            vec2 candidate = vec2(samplePixel) + vec2(0.5) + packed.rg;
            vec2 delta = candidate - bounded;
            float distanceSquared = dot(delta, delta);
            if (distanceSquared >= bestDistanceSquared) continue;
            bestDistanceSquared = distanceSquared;
            seedPixel = candidate;
            proximity = packed.b;
            identity = espTarget(packed.a).r;
            found = true;
        }
    }
    return found;
}

void main() {
    vec2 fieldResolution = max(uFieldResolution, vec2(1.0));
    ivec2 fieldSize = textureSize(uNearestSeed, 0);
    ivec2 pixel = clamp(ivec2(gl_FragCoord.xy),
        ivec2(0), fieldSize - ivec2(1));
    vec2 fragmentPixel = vec2(pixel) + vec2(0.5);
    vec2 uv = fragmentPixel / fieldResolution;

    vec4 directSeed = texelFetch(uNearestSeed, pixel, 0);
    if (directSeed.b <= 0.0) discard;
    vec2 directSource = fragmentPixel + directSeed.rg;
    vec2 fromDirectSource = fragmentPixel - directSource;
    float boundaryDistance = max(length(fromDirectSource) - 0.5, 0.0);

    float playerCoverage = clamp(samplePlayerMaskTent(uv).r, 0.0, 1.0);
    float softInterior = smoothstep(0.04, 0.96, playerCoverage);
    float signedDistance = mix(boundaryDistance, -boundaryDistance,
        smoothstep(0.35, 0.65, playerCoverage));
    float interiorDistance = max(-signedDistance, 0.0);

    float identity = espTarget(directSeed.a).r;
    float time = uTime + identity * 23.71;
    float widthSetting = clamp((uFlameWidth - 0.10) / 1.15, 0.0, 1.0);
    float effectiveWidth = mix(0.35, 1.25, widthSetting);
    float scale = pixelsPerBlock(directSeed.b,
        directSource / fieldResolution);
    float minimumSideReach = mix(1.55, 2.0, widthSetting);
    float sideReach = clamp(scale * (0.012 + effectiveWidth * 0.035),
        minimumSideReach, 10.0);
    float upwardReach = clamp(scale * (0.08 + uFlameHeight * 0.34),
        5.0, 64.0);

    vec2 sourceMotion = sourceScreenMotion(directSource);
    float sourceSpeed = length(sourceMotion);
    float motionResponse = smoothstep(12.0, 180.0, sourceSpeed);
    
    
    vec2 trailVelocity = -sourceMotion * motionResponse;
    float conservativeReach = max(7.0, max(upwardReach * 1.30,
        sideReach * 4.0 + 8.0)) + sourceSpeed * 0.092 + 4.0;
    if (softInterior < 0.001 && boundaryDistance > conservativeReach) {
        discard;
    }

    vec2 sourceFlow = vec2(directSource.x * 0.050 + identity * 13.7,
        directSource.y * 0.031 - time * 0.86);
    float coarse = fbm(sourceFlow);
    float fine = fbm(FLOW_ROTATION * sourceFlow * 1.83
        + vec2(coarse * 2.2, -time * 0.37));

    
    
    
    float rimCenter = 0.18;
    float rimHalfWidth = mix(0.48, 0.72, widthSetting);
    float rimSdf = abs(signedDistance - rimCenter) - rimHalfWidth;
    float rimAa = max(fwidth(rimSdf), 0.48);
    float ignitionRim = 1.0
        - smoothstep(-rimAa, rimAa, rimSdf);
    float rimPulse = mix(0.76, 1.0,
        smoothstep(0.18, 0.82, coarse * 0.58 + fine * 0.42));
    float ignitionEnergy = ignitionRim * rimPulse
        * mix(0.36, 0.48, widthSetting);

    
    
    
    float footholdEmitter = emitterField(directSource, identity, time);
    vec2 baseTrail = trailVelocity * 0.004;
    vec2 baseDelta = fromDirectSource - baseTrail;
    float baseDistance = max(length(baseDelta) - 0.5, 0.0);
    float footholdReach = clamp(0.58 + sideReach * 0.16, 0.72, 2.10);
    float footholdSdf = baseDistance
        - footholdReach * mix(0.78, 1.08, fine);
    float footholdAa = max(fwidth(footholdSdf), 0.72);
    float footholdCoverage = (1.0
        - smoothstep(-footholdAa, footholdAa, footholdSdf))
        * footholdEmitter;

    
    
    
    float directUp = max(fromDirectSource.y, 0.0);
    float traceDown = min(directUp * mix(0.48, 0.76, coarse)
        + sideReach * mix(0.35, 1.35, fine), upwardReach * 0.72);
    float traceSide = (coarse - 0.5) * sideReach * 2.8
        + sin(time * 1.31 + directSource.x * 0.071
            + identity * 17.0) * sideReach * 0.62;
    float coreQueryHeight01 = clamp(directUp
        / max(upwardReach, 0.001), 0.0, 1.0);
    float coreQueryAge = mix(0.018, 0.052,
        smoothstep(0.02, 1.0, coreQueryHeight01));
    
    
    
    vec2 warpedQuery = fragmentPixel + vec2(traceSide, -traceDown)
        - trailVelocity * coreQueryAge;

    vec2 lickSource;
    float lickProximity;
    float lickIdentity;
    bool hasLickSource = nearestSeedAt(warpedQuery, lickSource,
        lickProximity, lickIdentity);
    vec2 rawLickDelta = fragmentPixel - lickSource;

    vec2 lickFlow = vec2(lickSource.x * 0.058 + lickIdentity * 19.1,
        lickSource.y * 0.021 - (uTime + lickIdentity * 23.71) * 0.79);
    float columnNoise = fbm(lickFlow);
    float curlNoise = fbm(FLOW_ROTATION * lickFlow * 2.11
        + vec2(7.3, -time * 0.43));
    float lickTime = uTime + lickIdentity * 23.71;
    float lickEmitter = emitterField(lickSource, lickIdentity, lickTime);
    float heightVariation = mix(0.20, 1.08,
        pow(clamp(columnNoise, 0.0, 1.0), 1.45));
    float lickHeight = upwardReach * heightVariation
        * mix(0.68, 1.0, lickEmitter);
    float rawHeight01 = clamp(max(rawLickDelta.y, 0.0)
        / max(lickHeight, 0.001), 0.0, 1.0);

    
    
    vec2 lickMotion = hasLickSource
        ? sourceScreenMotion(lickSource) : vec2(0.0);
    float lickMotionResponse = smoothstep(12.0, 180.0,
        length(lickMotion));
    vec2 coreTrailVelocity = -lickMotion * lickMotionResponse;
    float coreTrailAge = mix(0.010, 0.052,
        smoothstep(0.02, 1.0, rawHeight01));
    vec2 lickDelta = rawLickDelta - coreTrailVelocity * coreTrailAge;
    float lickLength = max(length(lickDelta), 0.0001);
    float topFacing = hasLickSource
        ? smoothstep(0.02, 0.72, lickDelta.y / lickLength)
        : 0.0;
    float height01 = clamp(max(lickDelta.y, 0.0)
        / max(lickHeight, 0.001), 0.0, 1.0);
    float rootWidth = clamp(0.42
        + sideReach * (0.14 + effectiveWidth * 0.22), 0.78, 6.0);
    float lickWidth = mix(rootWidth, rootWidth * 0.18,
        smoothstep(0.10, 1.0, height01));
    float lateralCurl = (curlNoise - 0.5)
        * (1.3 + max(lickDelta.y, 0.0) * 0.15)
        + sin(lickDelta.y * 0.17 - time * 2.05
            + lickIdentity * 29.0) * lickWidth * 0.31;
    float breakup = (0.5 - fine)
        * mix(0.8, 4.8, smoothstep(0.0, max(lickHeight, 0.001),
            max(lickDelta.y, 0.0)));
    float lickSdf = max(lickDelta.y - lickHeight,
        abs(lickDelta.x + lateralCurl) - lickWidth) + breakup;
    float lickAa = max(fwidth(lickSdf), 0.85);
    float lickCoverage = hasLickSource
        ? 1.0 - smoothstep(-lickAa, lickAa, lickSdf)
        : 0.0;
    float columnActivation = smoothstep(0.38, 0.72,
        columnNoise * 0.76 + curlNoise * 0.24);
    
    
    lickCoverage *= topFacing * lickEmitter
        * mix(0.52, 1.0, columnActivation)
        * smoothstep(-0.75, 1.25, lickDelta.y);

    
    
    
    float tallTraceDown = min(directUp * mix(0.62, 0.92, fine)
        + sideReach * mix(0.75, 1.75, coarse), upwardReach * 0.90);
    float tallTraceSide = (fine - 0.5) * sideReach * 4.1
        + sin(time * 0.93 + directSource.x * 0.053
            + identity * 31.0) * sideReach * 1.05;
    float tallQueryHeight01 = clamp(directUp
        / max(upwardReach, 0.001), 0.0, 1.0);
    float tallQueryAge = mix(0.002, 0.092,
        pow(smoothstep(0.02, 1.0, tallQueryHeight01), 0.78));
    vec2 tallQuery = fragmentPixel + vec2(tallTraceSide, -tallTraceDown)
        - trailVelocity * tallQueryAge;
    vec2 tallSource;
    float tallProximity;
    float tallIdentity;
    bool hasTallSource = nearestSeedAt(tallQuery, tallSource,
        tallProximity, tallIdentity);
    vec2 rawTallDelta = fragmentPixel - tallSource;
    float tallTime = uTime + tallIdentity * 23.71;
    vec2 tallFlow = vec2(tallSource.x * 0.041 + tallIdentity * 37.3,
        tallSource.y * 0.025 - tallTime * 0.64);
    float tallCoarse = fbm(tallFlow + vec2(17.3, 5.9));
    float tallFine = fbm(FLOW_ROTATION * tallFlow * 1.67
        + vec2(tallTime * 0.16, -11.7));
    float tallEmitter = emitterField(tallSource + vec2(43.7, 19.3),
        tallIdentity, tallTime + 2.91);
    float tallActivation = smoothstep(0.41, 0.75,
        tallCoarse * 0.61 + tallFine * 0.39);
    float tallHeight = upwardReach * mix(0.48, 1.30,
        pow(clamp(tallCoarse, 0.0, 1.0), 1.28))
        * mix(0.72, 1.0, tallEmitter);
    float rawTallHeight01 = clamp(max(rawTallDelta.y, 0.0)
        / max(tallHeight, 0.001), 0.0, 1.0);
    vec2 tallMotion = hasTallSource
        ? sourceScreenMotion(tallSource) : vec2(0.0);
    float tallMotionResponse = smoothstep(10.0, 165.0,
        length(tallMotion));
    vec2 tallTrailVelocity = -tallMotion * tallMotionResponse;
    float tallTrailAge = mix(0.002, 0.092,
        pow(smoothstep(0.02, 1.0, rawTallHeight01), 0.78));
    vec2 tallDelta = rawTallDelta - tallTrailVelocity * tallTrailAge;
    float tallLength = max(length(tallDelta), 0.0001);
    float tallTopFacing = hasTallSource
        ? smoothstep(-0.03, 0.64, tallDelta.y / tallLength)
        : 0.0;
    float tallHeight01 = clamp(max(tallDelta.y, 0.0)
        / max(tallHeight, 0.001), 0.0, 1.0);
    float tallRootWidth = clamp(0.52
        + sideReach * (0.17 + effectiveWidth * 0.16), 0.82, 5.4);
    float tallWidth = mix(tallRootWidth, tallRootWidth * 0.10,
        smoothstep(0.06, 1.0, tallHeight01));
    float tallCurl = (tallFine - 0.5)
        * (1.9 + max(tallDelta.y, 0.0) * 0.21)
        + sin(tallDelta.y * 0.125 - tallTime * 1.47
            + tallIdentity * 41.0) * tallWidth * 0.46;
    float tallBreakup = (0.52 - tallFine)
        * mix(1.0, 5.6, smoothstep(0.0, max(tallHeight, 0.001),
            max(tallDelta.y, 0.0)));
    float tallSdf = max(tallDelta.y - tallHeight,
        abs(tallDelta.x + tallCurl) - tallWidth) + tallBreakup;
    float tallAa = max(fwidth(tallSdf), 0.82);
    float tallCoverage = hasTallSource
        ? 1.0 - smoothstep(-tallAa, tallAa, tallSdf)
        : 0.0;
    tallCoverage *= tallTopFacing * tallEmitter
        * mix(0.42, 1.0, tallActivation)
        * smoothstep(-0.45, 1.35, tallDelta.y);

    float exteriorFootholdEnergy = footholdCoverage * 0.78;

    
    
    
    float lowWidthInterior = 1.0 - widthSetting;
    float innerReach = clamp(1.55 + sideReach * 0.70
        + lowWidthInterior * 1.45, 2.5, 7.0);
    float innerSdf = interiorDistance
        - innerReach * mix(0.72, 1.08, fine);
    float innerAa = max(fwidth(innerSdf), 0.78);
    float innerBand = 1.0 - smoothstep(-innerAa, innerAa, innerSdf);
    float lowWidthInteriorBoost = mix(1.28, 1.0, widthSetting);
    float interiorFootholdEnergy = innerBand * footholdEmitter
        * mix(0.40, 0.68, fine) * lowWidthInteriorBoost;
    float interiorLickEnergy = lickCoverage * innerBand
        * mix(0.82, 0.72, widthSetting);
    float interiorTallEnergy = tallCoverage * innerBand
        * mix(0.42, 0.34, widthSetting);

    
    
    
    
    vec2 sparkInertialDrift = trailVelocity * 0.060;
    
    
    
    vec2 sparkGateDelta = fromDirectSource - sparkInertialDrift * 1.08;
    float sparkGateLength = max(length(sparkGateDelta), 0.0001);
    float sparkBoundaryDistance = max(sparkGateLength - 0.5, 0.0);
    vec2 boundaryDirection = sparkGateDelta / sparkGateLength;
    float riseLimit = clamp(upwardReach * 1.12 + 6.0, 12.0, 72.0);
    float topMix = smoothstep(0.05, 0.70, boundaryDirection.y);
    float directionalReach = mix(sideReach * 4.0 + 8.0,
        riseLimit, topMix) + length(sparkInertialDrift) * 0.45;
    float bottomSuppression = smoothstep(-0.62, -0.08,
        boundaryDirection.y);
    float exteriorSparkGate = smoothstep(1.0, 2.6, sparkBoundaryDistance)
        * (1.0 - smoothstep(directionalReach * 0.72,
            directionalReach, sparkBoundaryDistance))
        * bottomSuppression;
    float sparkEmitter = mix(0.28, 1.0,
        smoothstep(0.05, 0.72, footholdEmitter));
    float sparkSurfaceGate = mix(exteriorSparkGate,
        innerBand * 0.40, softInterior);
    float sparkCheapGate = sparkEmitter * sparkSurfaceGate;
    float sparkEnergy = 0.0;
    if (sparkCheapGate > 0.001) {
        float sparkField = sparkLayer(fragmentPixel, identity, uTime,
            effectiveWidth, sparkInertialDrift);
        sparkEnergy = sparkField * sparkCheapGate * 1.18;
    }

    float footholdContribution = mix(exteriorFootholdEnergy,
        interiorFootholdEnergy, softInterior);
    float lickContribution = mix(lickCoverage,
        interiorLickEnergy, softInterior);
    float tallContribution = mix(tallCoverage,
        interiorTallEnergy, softInterior);
    float baseLayer = max(ignitionEnergy, footholdContribution);
    float coreLayer = lickContribution;
    float tallLayer = tallContribution;
    float intensityScale = sqrt(max(uIntensity, 0.0)) * 1.18;
    float baseWeight = clamp(baseLayer, 0.0, 1.0)
        * intensityScale * 0.78;
    float coreWeight = clamp(coreLayer, 0.0, 1.0)
        * intensityScale;
    float tallWeight = clamp(tallLayer, 0.0, 1.0)
        * intensityScale * 0.86;
    float sparkWeight = clamp(sparkEnergy, 0.0, 1.0) * intensityScale;
    if (baseWeight + coreWeight + tallWeight + sparkWeight <= 0.00001) {
        discard;
    }

    float rawSceneDepth = texture(uSceneDepth, uv).r;
    
    
    
    float sceneViewDepth = rawSceneDepth < 0.999999
        ? viewDepth(uv, rawSceneDepth)
        : 0.0;
    
    
    
    float baseVisibility = (baseWeight + sparkWeight) > 0.0
        ? sourceVisibility(directSource / fieldResolution,
            directSeed.b, rawSceneDepth, sceneViewDepth)
        : 0.0;
    float coreVisibility = hasLickSource && coreWeight > 0.0
        ? sourceVisibility(lickSource / fieldResolution,
            lickProximity, rawSceneDepth, sceneViewDepth)
        : 0.0;
    float tallVisibility = hasTallSource && tallWeight > 0.0
        ? sourceVisibility(tallSource / fieldResolution,
            tallProximity, rawSceneDepth, sceneViewDepth)
        : 0.0;

    
    
    
    fragColor = packDepthSeparated(baseWeight,
        baseVisibility, directSeed.b)
        + packDepthSeparated(coreWeight, coreVisibility, lickProximity)
        + packDepthSeparated(tallWeight, tallVisibility, tallProximity)
        + packDepthSeparated(sparkWeight, baseVisibility, directSeed.b);
}
