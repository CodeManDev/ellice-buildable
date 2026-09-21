#version 330 core

uniform sampler2D uSourceTexture;
uniform vec2 uSourceResolution;
uniform vec2 uDirection;
uniform float uRadius;
uniform float uGaussianBase;

in vec2 vTexCoord;
out vec4 fragColor;

const int MAX_RADIUS = 48;

void main() {
    vec2 resolution = max(uSourceResolution, vec2(1.0));
    vec2 halfTexel = 0.5 / resolution;
    float radius = clamp(uRadius, 0.0, float(MAX_RADIUS));
    if (radius < 0.5 || dot(uDirection, uDirection) < 0.5) {
        fragColor = texture(uSourceTexture,
            clamp(vTexCoord, halfTexel, vec2(1.0) - halfTexel));
        return;
    }

    float support = floor(radius + 0.5);
    
    
    
    
    float gaussianBase = clamp(uGaussianBase, 0.0, 1.0);
    float ratioStep = gaussianBase * gaussianBase;
    float nextWeight = gaussianBase;
    float nextRatio = gaussianBase * ratioStep;
    vec2 texelDirection = uDirection / resolution;
    vec4 result = texture(uSourceTexture,
        clamp(vTexCoord, halfTexel, vec2(1.0) - halfTexel));
    float totalWeight = 1.0;

    
    
    for (int i = 1; i <= MAX_RADIUS; i += 2) {
        if (float(i) > support) break;
        float firstOffset = float(i);
        float firstWeight = nextWeight;
        float secondOffset = firstOffset + 1.0;
        float secondWeight = secondOffset <= support
            ? firstWeight * nextRatio
            : 0.0;
        float pairWeight = firstWeight + secondWeight;
        float sampleOffset = (firstOffset * firstWeight
            + secondOffset * secondWeight) / max(pairWeight, 0.000001);
        vec2 offset = texelDirection * sampleOffset;
        vec2 positiveUv = clamp(vTexCoord + offset, halfTexel, vec2(1.0) - halfTexel);
        vec2 negativeUv = clamp(vTexCoord - offset, halfTexel, vec2(1.0) - halfTexel);
        result += (texture(uSourceTexture, positiveUv)
            + texture(uSourceTexture, negativeUv)) * pairWeight;
        totalWeight += 2.0 * pairWeight;

        
        
        float afterSecondRatio = nextRatio * ratioStep;
        nextWeight = (firstWeight * nextRatio) * afterSecondRatio;
        nextRatio = afterSecondRatio * ratioStep;
    }
    fragColor = result / max(totalWeight, 0.000001);
}
