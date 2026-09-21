#version 330 core
in vec2 vTexCoord;
uniform float uIntensity;
out vec4 fragColor;

void main() {
    vec2 uv = vTexCoord - 0.5;
    float dist = length(uv) * 2.0;
    float v = smoothstep(0.5, 1.4, dist) * uIntensity;
    
    fragColor = vec4(0.0, 0.0, 0.0, v);
}
