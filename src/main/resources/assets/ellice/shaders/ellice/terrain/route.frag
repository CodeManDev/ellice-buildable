#version 330 core
in vec2 vUv;
uniform float uTime, uProgress, uReveal;
uniform vec4 uRouteColor, uRouteEdge;
out vec4 fragColor;
void main() {
    float side = abs(vUv.x);
    float aa = max(fwidth(side), .015);
    float core = 1.0 - smoothstep(.65, .75, side);
    vec3 color = mix(uRouteEdge.rgb, uRouteColor.rgb, core);
    float phase = fract((vUv.y - uProgress) / 4.0 - uTime * .45);
    float chevron = 1.0 - smoothstep(.045, .065, abs(phase - .48 - side * .24));
    color = mix(color, vec3(.95, .98, 1), chevron * core * .8);
    if (vUv.y < uProgress - .2) color = mix(color, vec3(.46, .55, .68), .7);
    float alpha = (1.0 - smoothstep(1.0 - aa, 1.0, side)) * smoothstep(0.0, 1.0, uReveal);
    if (alpha < .005) discard;
    fragColor = vec4(color, alpha);
}
