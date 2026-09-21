#version 330 core
in vec2 vTexCoord;
uniform sampler2D uTexture;
uniform float uThreshold;
out vec4 fragColor;

void main() {
    vec3 color = texture(uTexture, vTexCoord).rgb;
    float brightness = dot(color, vec3(0.2126, 0.7152, 0.0722));
    float contribution = max(0.0, brightness - uThreshold);
    fragColor = vec4(color * contribution, 1.0);
}
