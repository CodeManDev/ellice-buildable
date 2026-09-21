#version 330 core

in vec2 vTexCoord;

uniform sampler2D uTexture;
uniform float uAmount;

out vec4 fragColor;

void main() {
    vec4 src = texture(uTexture, vTexCoord);
    float gray = dot(src.rgb, vec3(0.299, 0.587, 0.114));
    vec3 rgb = mix(src.rgb, vec3(gray), clamp(uAmount, 0.0, 1.0));
    fragColor = vec4(rgb, src.a);
}
