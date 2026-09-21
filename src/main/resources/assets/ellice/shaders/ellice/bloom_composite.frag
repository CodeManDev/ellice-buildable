#version 330 core
in vec2 vTexCoord;
uniform sampler2D uBloom;
uniform float uIntensity;
out vec4 fragColor;

void main() {
    vec3 bloom = texture(uBloom, vTexCoord).rgb * uIntensity;
    
    fragColor = vec4(bloom, 1.0);
}
