#version 330 core

uniform vec4 uGlowColor;
uniform float uGlowStrength;

out vec4 fragColor;

void main() {
    vec3 positiveColor = max(uGlowColor.rgb, vec3(0.0));
    float channelSum = positiveColor.r + positiveColor.g + positiveColor.b;
    float weight = max(uGlowStrength, 0.0) * clamp(uGlowColor.a, 0.0, 1.0);
    if (channelSum <= 0.000001 || weight <= 0.000001) {
        fragColor = vec4(0.0);
        return;
    }

    vec3 chroma = positiveColor / channelSum;
    
    
    
    fragColor = vec4(chroma * weight, (1.0 - gl_FragCoord.z) * weight);
}
