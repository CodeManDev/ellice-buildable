#version 330 core

in vec2 vUV;

uniform sampler2D uAlbumArt;
uniform float uTime;
uniform float uCornerRadius;
uniform float uOpacity;
uniform vec3 uTintColor;

out vec4 fragColor;

float roundedBox(vec2 p, vec2 b, float r) {
    vec2 q = abs(p) - b + vec2(min(r, min(b.x, b.y)));
    return length(max(q, 0.0)) + min(max(q.x, q.y), 0.0) - min(r, min(b.x, b.y));
}

void main() {
    float mask = 1.0 - smoothstep(-0.002, 0.002,
        roundedBox(vUV - 0.5, vec2(0.5), uCornerRadius));

    
    vec2 uv = vUV;
    float t = uTime;
    uv += 0.018 * vec2(
        sin(t * 0.3 + uv.y * 3.0) + 0.5 * sin(t * 0.55 + uv.y * 1.5),
        cos(t * 0.35 + uv.x * 3.0) + 0.5 * cos(t * 0.2  + uv.x * 1.5)
    );

    
    uv = (uv - 0.5) * 0.5 + 0.5;

    
    
    vec3 color = vec3(0.0);
    float blurR = 0.15;
    const int TAPS = 32;
    for (int i = 0; i < TAPS; i++) {
        float angle = float(i) * 2.39996323;
        float r = sqrt(float(i + 1) / float(TAPS)) * blurR;
        r *= 1.0 + 0.1 * sin(t * 0.45 + float(i) * 0.4);
        vec2 off = vec2(cos(angle), sin(angle)) * r;
        color += texture(uAlbumArt, clamp(uv + off, 0.0, 1.0)).rgb;
    }
    color /= float(TAPS);

    
    float lum = dot(color, vec3(0.299, 0.587, 0.114));
    color = mix(vec3(lum), color, 1.8);

    
    color *= 0.65;

    
    color *= 1.0 + 0.03 * sin(t * 0.7);

    
    float vig = 1.0 - 0.3 * dot(vUV - 0.5, vUV - 0.5) * 4.0;
    color *= max(vig, 0.0);

    
    float grain = fract(sin(dot(vUV * 300.0 + t * 0.1, vec2(12.9898, 78.233))) * 43758.5453);
    color += (grain - 0.5) * 0.008;

    float alpha = mask * uOpacity;
    fragColor = vec4(color * alpha, alpha);
}
