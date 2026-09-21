#version 330 core

uniform sampler2D uHighTexture;
uniform sampler2D uLowTexture;
uniform vec2 uLowTexel;
uniform float uOffset;
uniform float uScatter;

in vec2 vTexCoord;
out vec4 fragColor;

void main() {
    vec2 tap = uLowTexel * max(uOffset, 0.0);
    vec4 sum = texture(uLowTexture,
        vTexCoord + vec2(-tap.x * 2.0, 0.0));
    sum += texture(uLowTexture,
        vTexCoord + vec2(-tap.x, tap.y)) * 2.0;
    sum += texture(uLowTexture,
        vTexCoord + vec2(0.0, tap.y * 2.0));
    sum += texture(uLowTexture,
        vTexCoord + vec2(tap.x, tap.y)) * 2.0;
    sum += texture(uLowTexture,
        vTexCoord + vec2(tap.x * 2.0, 0.0));
    sum += texture(uLowTexture,
        vTexCoord + vec2(tap.x, -tap.y)) * 2.0;
    sum += texture(uLowTexture,
        vTexCoord + vec2(0.0, -tap.y * 2.0));
    sum += texture(uLowTexture,
        vTexCoord + vec2(-tap.x, -tap.y)) * 2.0;

    vec4 high = texture(uHighTexture, vTexCoord);
    vec4 tent = sum / 12.0;
    float scatter = clamp(uScatter, 0.0, 1.0);
    
    fragColor = mix(high, tent, scatter);
}
