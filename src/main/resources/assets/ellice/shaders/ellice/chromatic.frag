#version 330 core
in vec2 vTexCoord;
uniform sampler2D uTexture;
uniform float uIntensity;
out vec4 fragColor;

void main() {
    vec2 dir = vTexCoord - 0.5;
    vec2 offset = dir * uIntensity;

    float r = texture(uTexture, vTexCoord + offset).r;
    float g = texture(uTexture, vTexCoord).g;
    float b = texture(uTexture, vTexCoord - offset).b;

    fragColor = vec4(r, g, b, 1.0);
}
