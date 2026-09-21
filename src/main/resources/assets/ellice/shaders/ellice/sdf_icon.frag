#version 330 core

#include <common/rounded_clip.glsl>

in vec2 vTexCoord;

uniform sampler2D uTexture;
uniform sampler2D uMorphTexture;
uniform float uMorph;
uniform float uOpacity;
uniform float uPxRange;
uniform vec4 uTint;

out vec4 fragColor;

float screenPxRange() {
    vec2 unitRange = vec2(uPxRange) / vec2(textureSize(uTexture, 0));
    vec2 screenTexSize = vec2(1.0) / fwidth(vTexCoord);
    return max(0.5 * dot(unitRange, screenTexSize), 1.0);
}

void main() {
    float sd = mix(texture(uTexture, vTexCoord).r,
                   texture(uMorphTexture, vTexCoord).r, uMorph);
    float dist = screenPxRange() * (sd - 0.5);
    float alpha = clamp(dist + 0.5, 0.0, 1.0) * uOpacity * uTint.a;
    fragColor = vec4(uTint.rgb * alpha, alpha) * elliceRoundedClipMask();
}
