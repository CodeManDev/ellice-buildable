#version 330 core

uniform sampler2D uModelMask;
uniform sampler2D uModelDepth;
uniform sampler2D uSceneDepth;
uniform mat4 uInvProjection;
uniform float uOcclusionBias;
uniform float uVisibleEnabled;
uniform float uOccludedEnabled;

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

float frontGate(vec2 uv, float modelRawDepth, float sceneRawDepth) {
    if (sceneRawDepth >= 0.999999) return 1.0;
    float modelViewDepth = viewDepth(uv, modelRawDepth);
    float sceneViewDepth = viewDepth(uv, sceneRawDepth);
    float bias = max(max(uOcclusionBias, 0.00001),
        modelViewDepth * 0.0025);
    return smoothstep(-bias, 0.0, sceneViewDepth - modelViewDepth);
}

float contourCoverage(ivec2 pixel, ivec2 size, float coverage) {
    if (coverage <= 0.001) return 0.0;
    float neighbourCoverage = 1.0;
    for (int y = -1; y <= 1; y++) {
        for (int x = -1; x <= 1; x++) {
            ivec2 neighbour = pixel + ivec2(x, y);
            float sampleCoverage = 0.0;
            if (all(greaterThanEqual(neighbour, ivec2(0))) && all(lessThan(neighbour, size)))
                sampleCoverage = texelFetch(uModelMask, neighbour, 0).r;
            neighbourCoverage = min(neighbourCoverage, sampleCoverage);
        }
    }
    return coverage * (1.0 - neighbourCoverage);
}

void main() {
    ivec2 sourceSize = textureSize(uModelMask, 0);
    ivec2 basePixel = ivec2(gl_FragCoord.xy) * 2;
    float visibleEnable = clamp(uVisibleEnabled, 0.0, 1.0);
    float occludedEnable = clamp(uOccludedEnabled, 0.0, 1.0);
    vec4 packed = vec4(0.0);
    float validSamples = 0.0;

    
    for (int y = 0; y < 2; y++) {
        for (int x = 0; x < 2; x++) {
            ivec2 pixel = basePixel + ivec2(x, y);
            if (any(greaterThanEqual(pixel, sourceSize))) continue;
            validSamples += 1.0;

            float coverage = clamp(
                texelFetch(uModelMask, pixel, 0).r, 0.0, 1.0);
            
            
            coverage = 4.0 * contourCoverage(pixel, sourceSize, coverage);
            float modelRawDepth = texelFetch(uModelDepth, pixel, 0).r;
            float sceneRawDepth = texelFetch(uSceneDepth, pixel, 0).r;
            vec2 uv = (vec2(pixel) + vec2(0.5)) / vec2(sourceSize);
            float visibility = frontGate(
                uv, modelRawDepth, sceneRawDepth);
            float proximity = clamp(1.0 - modelRawDepth, 0.0, 1.0);
            float visibleWeight = coverage * visibility * visibleEnable;
            float occludedWeight = coverage * (1.0 - visibility)
                * occludedEnable;

            
            packed += vec4(visibleWeight, visibleWeight * proximity,
                occludedWeight, occludedWeight * proximity);
        }
    }

    fragColor = packed / max(validSamples, 1.0);
}
