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

uniform int uMaterial;
uniform float uTime;
uniform vec4 uMouse;           
uniform vec4 uMaterialParams;  
uniform vec4 uMaterialState;   
uniform vec4 uMaterialColor;

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

float hash(vec2 p) {
    p = fract(p * vec2(123.34, 456.21));
    p += dot(p, p + 45.32);
    return fract(p.x * p.y);
}

float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    vec2 u = f * f * (3.0 - 2.0 * f);
    return mix(mix(hash(i), hash(i + vec2(1.0, 0.0)), u.x),
               mix(hash(i + vec2(0.0, 1.0)), hash(i + vec2(1.0, 1.0)), u.x), u.y);
}

float fbm(vec2 p) {
    float v = 0.0;
    float a = 0.5;
    for (int i = 0; i < 4; i++) {
        v += noise(p) * a;
        p = p * 2.03 + vec2(17.1, 9.2);
        a *= 0.5;
    }
    return v;
}

vec4 sampleLayer(vec2 uv) {
    vec2 clampedUv = clamp(uv, min(uUvRect.xy, uUvRect.zw), max(uUvRect.xy, uUvRect.zw));
    return texture(uTexture, clampedUv);
}

vec2 localToUv(vec2 local) {
    return mix(uUvRect.xy, uUvRect.zw, clamp(local, 0.0, 1.0));
}

vec4 sampleLayerLocal(vec2 local) {
    return sampleLayer(localToUv(local));
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

float surfaceMask() {
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
    return mask;
}

float edgeField() {
    vec2 px = vTexCoord * uSize;
    float d = min(min(px.x, uSize.x - px.x), min(px.y, uSize.y - px.y));
    return 1.0 - smoothstep(0.0, 5.0 + uMaterialState.x * 10.0, d);
}

float scanGrid(vec2 px, float t) {
    float scan = pow(0.5 + 0.5 * sin(px.y * 1.72 + t * 44.0), 16.0);
    vec2 grid = abs(fract(px / 22.0) - 0.5);
    float line = 1.0 - smoothstep(0.465, 0.5, max(grid.x, grid.y));
    return scan * 0.65 + line * 0.35;
}

vec3 unpremul(vec4 c) {
    return c.a > 0.0001 ? c.rgb / c.a : vec3(0.0);
}

vec3 chroma(vec2 local, vec2 dir, float amount) {
    vec2 off = dir * amount / max(uSize, vec2(1.0));
    float r = unpremul(sampleLayerLocal(local + off)).r;
    float g = unpremul(sampleLayerLocal(local)).g;
    float b = unpremul(sampleLayerLocal(local - off)).b;
    return vec3(r, g, b);
}

void main() {
    float intensity = max(0.0, uMaterialParams.x);
    float scale = max(0.001, uMaterialParams.y);
    float t = uTime * uMaterialParams.z;
    float distortion = max(0.0, uMaterialParams.w);
    float glow = max(0.0, uMaterialState.x);
    float hover = max(uMaterialState.y, uMouse.w * 0.35);
    float press = max(uMaterialState.z, uMouse.z * uMouse.w);
    vec3 accent = uMaterialColor.rgb;

    vec2 local = vTexCoord;
    vec2 px = local * uSize;
    vec2 centered = local - 0.5;
    float edge = edgeField();
    vec2 mouse = uMouse.xy / max(uSize, vec2(1.0));
    float mouseDist = length((local - mouse) * vec2(uSize.x / max(uSize.y, 1.0), 1.0));
    float mouseLight = uMouse.w * exp(-mouseDist * 7.0);

    if (uMaterial == 2) {
        float n1 = fbm(local * (7.0 * scale) + vec2(t * 0.17, -t * 0.11));
        float n2 = fbm(local * (8.5 * scale) + vec2(-t * 0.12, t * 0.19));
        vec2 wave = vec2(n1 - 0.5, n2 - 0.5);
        local += wave * 0.018 * distortion * intensity;
    } else if (uMaterial == 4) {
        float r = length(centered);
        float a = atan(centered.y, centered.x);
        float swirl = (1.0 - smoothstep(0.0, 0.72, r)) * distortion * 0.75;
        a += swirl * sin(t * 0.8 + r * 9.0);
        local = 0.5 + vec2(cos(a), sin(a)) * r;
    } else if (uMaterial == 5) {
        float n = fbm(local * (5.0 * scale) + t * 0.13);
        local += (vec2(n, fbm(local.yx * (5.0 * scale) - t * 0.11)) - 0.5)
            * 0.008 * distortion * intensity;
    }

    vec4 tex = blurredSample(localToUv(local));
    float mask = surfaceMask();
    float a = tex.a * uOpacity * mask;

    vec3 rgb = unpremul(tex);
    float gray = dot(rgb, vec3(0.299, 0.587, 0.114));
    rgb = mix(vec3(gray), rgb, max(0.0, uSaturation));
    rgb = mix(rgb, vec3(gray), clamp(uGrayscale, 0.0, 1.0));
    rgb = (rgb - 0.5) * max(0.0, uContrast) + 0.5 + uBrightness;
    if (uTint.a > 0.0) {
        rgb = mix(rgb, uTint.rgb, uTint.a);
    }

    float extraAlpha = 0.0;
    if (uMaterial == 1) {
        float lines = scanGrid(px, t);
        float sheen = pow(max(0.0, 1.0 - abs(centered.x + centered.y - sin(t * 0.4) * 0.45)), 14.0);
        rgb += accent * (lines * 0.035 + edge * (0.10 + glow * 0.18) + sheen * 0.10 + mouseLight * 0.12)
            * intensity;
        rgb += vec3(0.10, 0.04, 0.18) * hover * intensity;
        extraAlpha = edge * glow * 0.16 + sheen * 0.035;
    } else if (uMaterial == 2) {
        float n = fbm(local * (4.0 * scale) + vec2(t * 0.08, t * 0.05));
        vec3 oil = 0.5 + 0.5 * cos(vec3(0.0, 2.1, 4.2) + n * 6.283 + t * 0.7);
        rgb = mix(rgb, rgb + oil * accent, (0.08 + hover * 0.07) * intensity);
        rgb += accent * edge * (0.08 + glow * 0.16) * intensity;
        extraAlpha = edge * glow * 0.12;
    } else if (uMaterial == 3) {
        float path = edge * (0.65 + 0.35 * sin(t * 6.0 + px.x * 0.09 + px.y * 0.07));
        float sparks = pow(fbm(local * (28.0 * scale) + vec2(t * 1.5, -t * 1.2)), 9.0);
        float energy = max(path, sparks * edge);
        rgb += accent * energy * (0.85 + glow) * intensity;
        rgb += vec3(1.0) * energy * 0.18 * intensity;
        extraAlpha = energy * (0.25 + glow * 0.35) * intensity;
    } else if (uMaterial == 4) {
        float r = length(centered);
        float ring = sin(r * 42.0 - t * 5.0);
        vec3 portal = 0.5 + 0.5 * cos(vec3(0.0, 2.4, 4.8) + ring + t);
        float core = smoothstep(0.72, 0.0, r);
        rgb = mix(rgb, portal * accent, core * 0.25 * intensity);
        rgb += portal * edge * (0.18 + glow * 0.22) * intensity;
        extraAlpha = core * 0.04 * intensity + edge * glow * 0.14;
    } else if (uMaterial == 5) {
        vec2 dir = normalize(centered + vec2(0.001));
        float amount = (2.5 + edge * 7.0 + mouseLight * 5.0) * distortion * intensity;
        vec3 split = chroma(local, dir, amount);
        float ellice = 0.5 + 0.5 * sin((local.x + local.y) * 16.0 + t * 1.4);
        rgb = mix(rgb, split, clamp(0.22 * intensity + edge * 0.35, 0.0, 0.85));
        rgb += accent * ellice * edge * (0.10 + glow * 0.15) * intensity;
        extraAlpha = edge * glow * 0.10;
    }

    rgb += accent * mouseLight * (0.04 + 0.06 * press) * intensity;
    a = max(a, extraAlpha * uOpacity * mask);

    rgb = clamp(rgb, 0.0, 1.0);
    fragColor = vec4(rgb * a, a);
}
