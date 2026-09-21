#version 330 core

uniform sampler2D uSeedField;
uniform int uJumpStep;
uniform vec2 uRegionMin;
uniform vec2 uRegionMax;

in vec2 vTexCoord;
out vec4 fragColor;

void considerSeed(ivec2 samplePixel, vec2 currentCenter,
                  inout vec4 best, inout float bestDistanceSquared) {
    vec4 candidate = texelFetch(uSeedField, samplePixel, 0);
    if (candidate.b <= 0.0) return;

    vec2 sampleCenter = vec2(samplePixel) + vec2(0.5);
    vec2 candidateOffset = sampleCenter + candidate.rg - currentCenter;
    float candidateDistanceSquared = dot(candidateOffset, candidateOffset);
    if (candidateDistanceSquared >= bestDistanceSquared) return;

    bestDistanceSquared = candidateDistanceSquared;
    
    
    best = vec4(candidateOffset, candidate.ba);
}

void main() {
    ivec2 resolution = textureSize(uSeedField, 0);
    ivec2 regionMin = max(ivec2(uRegionMin), ivec2(0));
    ivec2 regionMax = min(ivec2(uRegionMax), resolution - ivec2(1));
    ivec2 currentPixel = clamp(ivec2(gl_FragCoord.xy),
        regionMin, regionMax);
    vec2 currentCenter = vec2(currentPixel) + vec2(0.5);
    int jumpStep = max(uJumpStep, 1);

    vec4 best = vec4(0.0);
    float bestDistanceSquared = 1.0e20;
    for (int y = -1; y <= 1; ++y) {
        for (int x = -1; x <= 1; ++x) {
            ivec2 samplePixel = clamp(currentPixel
                + ivec2(x, y) * jumpStep, regionMin, regionMax);
            considerSeed(samplePixel, currentCenter,
                best, bestDistanceSquared);
        }
    }
    fragColor = best;
}
