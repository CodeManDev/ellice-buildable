#version 330 core

in vec2 vTexCoord;
in vec4 vColor;
in float vBlur;

uniform sampler2D uAtlas;
uniform float uPxRange;

out vec4 fragColor;

float median(float r, float g, float b) {
    return max(min(r, g), min(max(r, g), b));
}

float screenPxRange() {
    vec2 unitRange = vec2(uPxRange) / vec2(textureSize(uAtlas, 0));
    vec2 screenTexSize = vec2(1.0) / fwidth(vTexCoord);
    return max(0.5 * dot(unitRange, screenTexSize), 1.0);
}

void main() {
    float spr = screenPxRange();

    
    spr = mix(spr, max(spr * 0.08, 1.0), vBlur);

    vec3 msd = texture(uAtlas, vTexCoord).rgb;
    float sd = median(msd.r, msd.g, msd.b);
    float dist = spr * (sd - 0.5);
    float fillAlpha = clamp(dist + 0.5, 0.0, 1.0);

    
    float opacity = mix(1.0, 0.12, vBlur * vBlur);

    float a = vColor.a * fillAlpha * opacity;
    fragColor = vec4(vColor.rgb * a, a);
}
