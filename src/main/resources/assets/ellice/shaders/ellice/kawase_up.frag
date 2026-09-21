#version 330 core

in vec2 vTexCoord;

uniform sampler2D uTexture;
uniform vec2 uHalfPixel;   

out vec4 fragColor;


void main() {
    vec4 sum  = texture(uTexture, vTexCoord + vec2(-uHalfPixel.x * 2.0, 0.0));
    sum      += texture(uTexture, vTexCoord + vec2(-uHalfPixel.x, uHalfPixel.y)) * 2.0;
    sum      += texture(uTexture, vTexCoord + vec2(0.0, uHalfPixel.y * 2.0));
    sum      += texture(uTexture, vTexCoord + vec2(uHalfPixel.x, uHalfPixel.y)) * 2.0;
    sum      += texture(uTexture, vTexCoord + vec2(uHalfPixel.x * 2.0, 0.0));
    sum      += texture(uTexture, vTexCoord + vec2(uHalfPixel.x, -uHalfPixel.y)) * 2.0;
    sum      += texture(uTexture, vTexCoord + vec2(0.0, -uHalfPixel.y * 2.0));
    sum      += texture(uTexture, vTexCoord + vec2(-uHalfPixel.x, -uHalfPixel.y)) * 2.0;
    fragColor = sum / 12.0;
}
