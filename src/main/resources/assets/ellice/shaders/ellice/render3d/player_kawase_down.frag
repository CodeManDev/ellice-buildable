#version 330 core

uniform sampler2D uSourceTexture;
uniform vec2 uSourceTexel;
uniform float uOffset;

in vec2 vTexCoord;
out vec4 fragColor;

void main() {
    vec2 halfOffset = uSourceTexel * (0.5 * max(uOffset, 0.0));
    vec4 sum = texture(uSourceTexture, vTexCoord) * 4.0;
    sum += texture(uSourceTexture, vTexCoord - halfOffset);
    sum += texture(uSourceTexture, vTexCoord + halfOffset);
    sum += texture(uSourceTexture,
        vTexCoord + vec2(halfOffset.x, -halfOffset.y));
    sum += texture(uSourceTexture,
        vTexCoord + vec2(-halfOffset.x, halfOffset.y));
    fragColor = sum * 0.125;
}
