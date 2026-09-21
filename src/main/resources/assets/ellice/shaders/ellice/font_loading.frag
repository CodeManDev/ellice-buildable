#version 330 core

in vec2 vUV;

uniform vec4 uRect;
uniform float uRadius;
uniform float uTime;
uniform float uProgress;
uniform float uOpacity;
uniform float uScale;
uniform vec3 uAccent;

out vec4 fragColor;

const float TAU = 6.28318530718;

float angularDistance(float from, float to) {
    return mod(to - from + TAU, TAU);
}

void main() {
    vec2 size = max(uRect.zw, vec2(1.0));
    vec2 p = (vUV - 0.5) * size;
    float radius = uRadius > 0.0
        ? min(uRadius, min(size.x, size.y) * 0.5 - 1.0)
        : min(size.x, size.y) * 0.34;
    float thickness = max(1.35 * max(uScale, 1.0), radius * 0.13);
    float ringDistance = abs(length(p) - radius) - thickness * 0.5;
    float aa = max(fwidth(ringDistance), 0.7);
    float ring = 1.0 - smoothstep(-aa, aa, ringDistance);

    float angle = mod(atan(p.y, p.x) + TAU, TAU);
    float alpha;
    if (uProgress >= 0.0) {
        float swept = clamp(uProgress, 0.0, 1.0) * TAU;
        float fromTop = angularDistance(TAU * 0.75, angle);
        float angularAA = aa / max(radius, 1.0);
        alpha = 1.0 - smoothstep(swept - angularAA, swept + angularAA, fromTop);
    } else {
        float head = mod(uTime * 4.4, TAU);
        float behind = angularDistance(angle, head);
        float arc = TAU * 0.72;
        alpha = clamp(1.0 - behind / arc, 0.0, 1.0);
        alpha *= 1.0 - smoothstep(arc - 0.22, arc, behind);
        alpha = pow(alpha, 0.72);
    }

    float backgroundAlpha = ring * 0.13 * clamp(uOpacity, 0.0, 1.0);
    float foregroundAlpha = ring * alpha * clamp(uOpacity, 0.0, 1.0);
    vec3 foreground = mix(uAccent * 0.82, min(vec3(1.0), uAccent * 1.12 + 0.10), alpha);
    vec3 rgb = uAccent * backgroundAlpha + foreground * foregroundAlpha;
    float outAlpha = backgroundAlpha + foregroundAlpha * (1.0 - backgroundAlpha);
    fragColor = vec4(rgb, outAlpha);
}
