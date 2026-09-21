#version 330 core

in vec2 vTexCoord;

uniform sampler2D uTexture;
uniform float uOpacity;
uniform vec2 uSize;
uniform vec4 uRadius;
uniform float uEdgeSoftness;
uniform vec4 uTint;
uniform vec4 uUvRect;
uniform vec2 uTexelSize;
uniform float uBlurRadius;
uniform float uSaturation;
uniform float uBrightness;
uniform float uContrast;
uniform float uGrayscale;
uniform int uMaskShape;
uniform int uMaskInvert;

out vec4 fragColor;

float cornerRadius(vec2 p, vec4 r) {
    if (p.x < 0.0) {
        return p.y < 0.0 ? r.x : r.w;
    }
    return p.y < 0.0 ? r.y : r.z;
}

float roundedBox(vec2 p, vec2 b, vec4 radius) {
    vec4 r = clamp(radius, 0.0, min(b.x, b.y));
    float cr = cornerRadius(p, r);
    vec2 q = abs(p) - b + vec2(min(cr, min(b.x, b.y)));
    return length(max(q, 0.0)) + min(max(q.x, q.y), 0.0) - min(cr, min(b.x, b.y));
}

vec4 sampleLayer(vec2 uv) {
    vec2 clampedUv = clamp(uv, min(uUvRect.xy, uUvRect.zw), max(uUvRect.xy, uUvRect.zw));
    return texture(uTexture, clampedUv);
}

vec4 blurredSample(vec2 uv) {
    float r = clamp(uBlurRadius, 0.0, 32.0);
    if (r < 0.5) return sampleLayer(uv);

    vec2 o = uTexelSize * r;
    vec4 c = sampleLayer(uv) * 0.227027;
    c += sampleLayer(uv + vec2( o.x, 0.0)) * 0.1216216;
    c += sampleLayer(uv + vec2(-o.x, 0.0)) * 0.1216216;
    c += sampleLayer(uv + vec2(0.0,  o.y)) * 0.1216216;
    c += sampleLayer(uv + vec2(0.0, -o.y)) * 0.1216216;
    c += sampleLayer(uv + vec2( o.x,  o.y)) * 0.0766216;
    c += sampleLayer(uv + vec2(-o.x,  o.y)) * 0.0766216;
    c += sampleLayer(uv + vec2( o.x, -o.y)) * 0.0766216;
    c += sampleLayer(uv + vec2(-o.x, -o.y)) * 0.0766216;
    return c;
}

void main() {
    vec2 uv = mix(uUvRect.xy, uUvRect.zw, vTexCoord);
    vec4 tex = blurredSample(uv);

    float mask = 1.0;
    if (uMaskShape == 1) {
        vec2 p = (vTexCoord - 0.5) * uSize;
        vec2 r = max(uSize * 0.5, vec2(0.001));
        float dist = (length(p / r) - 1.0) * min(r.x, r.y);
        float softness = max(0.7, uEdgeSoftness);
        mask = 1.0 - smoothstep(-softness, softness, dist);
    } else if (uMaskShape == 2) {
        vec2 p = (vTexCoord - 0.5) * uSize;
        float radius = min(uSize.x, uSize.y) * 0.5;
        float dist = length(p) - radius;
        float softness = max(0.7, uEdgeSoftness);
        mask = 1.0 - smoothstep(-softness, softness, dist);
    } else if (max(max(uRadius.x, uRadius.y), max(uRadius.z, uRadius.w)) > 0.0) {
        vec2 pixel = vTexCoord * uSize;
        float dist = roundedBox(pixel - uSize * 0.5, uSize * 0.5, uRadius);
        float softness = max(0.7, uEdgeSoftness);
        mask = 1.0 - smoothstep(-softness, softness, dist);
    }
    if (uMaskInvert == 1) mask = 1.0 - mask;

    float a = tex.a * uOpacity * mask;
    vec3 rgb = tex.a > 0.0001 ? tex.rgb / tex.a : vec3(0.0);

    float gray = dot(rgb, vec3(0.299, 0.587, 0.114));
    rgb = mix(vec3(gray), rgb, max(0.0, uSaturation));
    rgb = mix(rgb, vec3(gray), clamp(uGrayscale, 0.0, 1.0));
    rgb = (rgb - 0.5) * max(0.0, uContrast) + 0.5 + uBrightness;
    if (uTint.a > 0.0) {
        rgb = mix(rgb, uTint.rgb, uTint.a);
    }

    rgb = clamp(rgb, 0.0, 1.0);
    fragColor = vec4(rgb * a, a);
}
