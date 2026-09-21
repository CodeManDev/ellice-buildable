#version 330 core

uniform vec4 uTopColor;
uniform vec4 uBottomColor;
uniform vec4 uEdgeColor;
uniform float uTime;
uniform float uOpacity;
uniform float uEnergy;
uniform int uMaterial;

in vec3 vNormal;
in vec2 vUv;
in vec2 vFx;
in vec3 vViewPos;

out vec4 fragColor;

float lineMask(float value, float width) {
    float centered = abs(fract(value) - 0.5);
    return 1.0 - smoothstep(0.0, width, centered);
}

void main() {
    vec3 normal = normalize(vNormal);
    vec3 viewDir = normalize(-vViewPos);
    vec3 lightDir = normalize(vec3(-0.35, 0.72, -0.55));
    float light = 0.42 + 0.58 * abs(dot(normal, lightDir));
    float fresnel = pow(1.0 - clamp(abs(dot(normal, viewDir)), 0.0, 1.0), 2.4);

    vec3 base = mix(uTopColor.rgb, uBottomColor.rgb, smoothstep(0.0, 1.0, vUv.y));
    float alpha = mix(uTopColor.a, uBottomColor.a, vUv.y) * uOpacity;

    float energy = clamp(uEnergy, 0.0, 2.0);
    float weaveA = lineMask(vUv.x * 7.0 + vUv.y * 1.35 - uTime * (0.18 + energy * 0.08), 0.050);
    float weaveB = lineMask((1.0 - vUv.x) * 5.0 + vUv.y * 3.1 + uTime * (0.10 + energy * 0.06), 0.044);
    float pulse = 0.5 + 0.5 * sin(uTime * (1.8 + energy) + vUv.y * 8.0);
    float edge = smoothstep(0.42, 1.0, vFx.x);
    float rootFade = smoothstep(0.0, 0.10, 1.0 - vFx.y);

    vec3 glow = uEdgeColor.rgb * (edge * 0.45 + fresnel * 0.55);
    vec3 color = base * light + glow;

    if (uMaterial == 1) {
        vec3 aurora = 0.5 + 0.5 * cos(vec3(0.0, 2.1, 4.2) + uTime * 0.75 + vUv.y * 5.5 + vUv.x * 2.0);
        color = mix(color, aurora, 0.34 + 0.18 * pulse);
        alpha *= 0.86 + 0.14 * pulse;
    } else if (uMaterial == 2) {
        float scan = lineMask(vUv.y * 26.0 - uTime * 1.9, 0.065);
        float circuit = max(weaveA, weaveB) * (0.55 + 0.45 * pulse);
        color = mix(base * 0.62, uEdgeColor.rgb, max(scan * 0.32, circuit * 0.62) + fresnel * 0.34);
        alpha *= 0.48 + 0.28 * scan + 0.22 * circuit + 0.18 * fresnel;
    } else {
        float thread = max(weaveA * 0.34, weaveB * 0.26) * energy;
        color += uEdgeColor.rgb * thread;
        alpha *= 0.90 + 0.10 * pulse;
    }

    alpha *= rootFade;
    fragColor = vec4(color, clamp(alpha, 0.0, 1.0));
}
