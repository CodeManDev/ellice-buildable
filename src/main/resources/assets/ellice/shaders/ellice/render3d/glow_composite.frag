#version 330 core

uniform sampler2D uGlowTexture;
uniform sampler2D uDepthTexture;
uniform mat4 uInvProjection;
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

void main() {
    vec4 packedGlow = texture(uGlowTexture, vTexCoord);
    float weight = packedGlow.r + packedGlow.g + packedGlow.b;
    if (weight <= 0.00001) discard;

    float glowRawDepth = 1.0 - clamp(packedGlow.a / weight, 0.0, 1.0);
    float rawSceneDepth = texture(uDepthTexture, vTexCoord).r;
    float visibility = 1.0;
    if (rawSceneDepth < 0.999999) {
        float glowViewDepth = viewDepth(vTexCoord, glowRawDepth);
        float sceneViewDepth = viewDepth(vTexCoord, rawSceneDepth);
        float bias = max(max(uOcclusionBias, 0.0), glowViewDepth * 0.0025);
        visibility = smoothstep(-bias, 0.0, sceneViewDepth - glowViewDepth);
    }

    fragColor = vec4(packedGlow.rgb * visibility, 0.0);
}
