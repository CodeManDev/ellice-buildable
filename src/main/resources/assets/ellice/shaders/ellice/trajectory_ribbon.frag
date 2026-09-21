#version 330 core

in float vSide;
in float vProgress;
in float vAlpha;

uniform vec4 uStartColor;
uniform vec4 uEndColor;
uniform float uTime;
uniform float uAlpha;
uniform float uGlow;

out vec4 fragColor;

void main() {
    float edge = smoothstep(1.0, 0.02, abs(vSide));
    float center = 0.78 + 0.22 * smoothstep(0.72, 0.0, abs(vSide));
    float tail = smoothstep(0.0, 0.10, vProgress) * (1.0 - smoothstep(0.96, 1.0, vProgress) * 0.10);

    vec4 color = mix(uStartColor, uEndColor, smoothstep(0.05, 1.0, vProgress));

    float alpha = color.a * uAlpha * vAlpha * edge * center * tail;
    fragColor = vec4(color.rgb, alpha);
}
