#version 330 core
in vec2 vUv;
uniform vec4 uColor;
out vec4 fragColor;
void main() {
    float radius = length(vUv * 2.0 - 1.0);
    float aa = max(fwidth(radius), 0.008);
    float ring = 1.0 - smoothstep(0.025, 0.025 + aa, abs(radius - 0.55));
    float fill = 1.0 - smoothstep(.52, .55, radius);
    float alpha = ring * .55 + fill * .12;
    if (alpha < 0.003) discard;
    fragColor = vec4(uColor.rgb, alpha * uColor.a);
}
