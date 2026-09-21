#version 330 core




in vec2 vUv;
uniform vec4 uColor;
out vec4 fragColor;
void main() {
    vec2 centered = vUv * 2.0 - 1.0;
    float dist = length(centered);
    float aa = max(fwidth(dist), 0.006);
    
    float disc = 1.0 - smoothstep(0.45, 1.0, dist + aa * 0.5);
    float alpha = disc * uColor.a;
    if (alpha < 0.004) discard;
    fragColor = vec4(uColor.rgb, alpha);
}
