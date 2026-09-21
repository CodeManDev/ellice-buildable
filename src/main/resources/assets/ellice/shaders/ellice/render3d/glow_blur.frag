#version 330 core

uniform sampler2D uSourceTexture;
uniform vec2 uSourceResolution;
uniform vec2 uDirection;
uniform float uRadius;

in vec2 vTexCoord;
out vec4 fragColor;

const int MAX_RADIUS = 48;

void main() {
    vec2 resolution = max(uSourceResolution, vec2(1.0));
    float radius = clamp(uRadius, 0.0, float(MAX_RADIUS));
    if (radius < 0.5 || dot(uDirection, uDirection) < 0.5) {
        fragColor = texture(uSourceTexture, vTexCoord);
        return;
    }

    float support = floor(radius + 0.5);
    float sigma = max(support / 3.0, 0.5);
    float inverseTwoSigmaSquared = 0.5 / (sigma * sigma);
    vec2 texelDirection = uDirection / resolution;
    vec4 result = texture(uSourceTexture, vTexCoord);
    float totalWeight = 1.0;

    
    
    for (int i = 1; i <= MAX_RADIUS; i += 2) {
        if (float(i) > support) break;
        float firstOffset = float(i);
        float firstWeight = exp(-firstOffset * firstOffset * inverseTwoSigmaSquared);
        float secondOffset = firstOffset + 1.0;
        float secondWeight = secondOffset <= support
            ? exp(-secondOffset * secondOffset * inverseTwoSigmaSquared)
            : 0.0;
        float pairWeight = firstWeight + secondWeight;
        float sampleOffset = (firstOffset * firstWeight
            + secondOffset * secondWeight) / pairWeight;
        vec2 offset = texelDirection * sampleOffset;
        result += (texture(uSourceTexture, vTexCoord + offset)
            + texture(uSourceTexture, vTexCoord - offset)) * pairWeight;
        totalWeight += 2.0 * pairWeight;
    }
    fragColor = result / totalWeight;
}
