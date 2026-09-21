#version 330 core

in vec2 vTexCoord;

uniform sampler2D uTexture;
uniform vec2 uHalfPixel;   

out vec4 fragColor;


void main() {
    vec4 sum = texture(uTexture, vTexCoord) * 4.0;
    sum += texture(uTexture, vTexCoord - uHalfPixel);
    sum += texture(uTexture, vTexCoord + uHalfPixel);
    sum += texture(uTexture, vTexCoord + vec2(uHalfPixel.x, -uHalfPixel.y));
    sum += texture(uTexture, vTexCoord + vec2(-uHalfPixel.x, uHalfPixel.y));
    fragColor = sum / 8.0;
}
