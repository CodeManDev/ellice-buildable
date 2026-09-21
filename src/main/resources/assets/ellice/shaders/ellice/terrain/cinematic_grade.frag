#version 330 core







#include <render3d/common/math.glsl>

uniform sampler2D uColorTexture;
uniform vec2 uResolution;
uniform float uTime;
uniform float uExposure;
uniform float uContrast;
uniform float uSaturation;
uniform float uVignette;
uniform float uChromatic;
uniform float uBloom;
uniform float uBloomRadius;
uniform float uBloomThreshold;
uniform float uHalation;
uniform float uGrain;
uniform float uWarmth;
uniform float uLetterbox;
uniform sampler2D uDepthTexture;
uniform int uDepthAvailable;
uniform float uNear;
uniform float uFar;
uniform float uSsao;
uniform float uSsaoRadius;
uniform float uDof;
uniform float uFocusDist;
uniform float uFxaa;

in vec2 vTexCoord;
out vec4 fragColor;

float gradeHash(vec2 value) {
    vec3 p3 = fract(vec3(value.xyx) * 0.1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

vec3 brightPixel(vec2 uv) {
    vec3 value = texture(uColorTexture, clamp(uv, 0.0, 1.0)).rgb;
    float luma = elliceLuminance(value);
    return value * smoothstep(uBloomThreshold, min(1.001, uBloomThreshold + 0.22), luma);
}

vec3 highlightGlow(vec2 uv, vec2 radius) {
    
    
    vec3 sum = brightPixel(uv) * 0.12;
    for (int i = 0; i < 8; i++) {
        float angle = float(i) * ELLICE_PI * 0.25;
        vec2 offset = vec2(cos(angle), sin(angle)) * radius;
        sum += brightPixel(uv + offset) * 0.075;
        sum += brightPixel(uv + offset * 2.4) * 0.035;
    }
    return sum;
}

float linearizeDepth(float d) {
    return (uNear * uFar) / max(uFar - d * (uFar - uNear), 1e-5);
}




float cavityAo(vec2 uv) {
    float rawCenter = texture(uDepthTexture, uv).r;
    if (rawCenter >= 0.9999) return 1.0;
    float center = linearizeDepth(rawCenter);
    vec2 px = 1.0 / uResolution;
    float occ = 0.0;
    for (int i = 0; i < 8; i++) {
        float fi = float(i);
        float angle = fi * 0.785398 + 0.392699;
        float ring = 2.0 + float(i % 3) * 2.5;
        vec2 tap = uv + vec2(cos(angle), sin(angle)) * ring * px;
        float raw = texture(uDepthTexture, clamp(tap, 0.0, 1.0)).r;
        if (raw >= 0.9999) continue;
        occ += clamp((center - linearizeDepth(raw)) / uSsaoRadius, 0.0, 1.0);
    }
    return 1.0 - clamp(occ / 8.0, 0.0, 1.0) * elliceSaturate(uSsao);
}







float tapCoc(vec2 tapUv, float scaleY) {
    float raw = texture(uDepthTexture, tapUv).r;
    float depth = raw >= 0.9999 ? uFar : linearizeDepth(raw);
    float rel = abs(depth - uFocusDist) / max(uFocusDist, 1e-3);
    
    
    
    return clamp((rel - 0.45) / 0.55 * uDof * 16.0, 0.0, 12.0) * scaleY;
}
vec3 depthOfField(vec2 uv, vec3 sharp) {
    float scaleY = uResolution.y / 1080.0;
    float coc = tapCoc(uv, scaleY);
    if (coc < 0.5) return sharp;
    vec2 px = vec2(1.0) / uResolution;
    float rotation = gradeHash(gl_FragCoord.xy * 0.731) * 6.283185;
    vec3 acc = sharp;
    float wsum = 1.0;
    for (int i = 0; i < 48; i++) {
        float fi = float(i);
        float radius = sqrt((fi + 0.5) / 48.0) * coc;
        float angle = fi * 2.399963 + rotation;
        vec2 tapUv = clamp(uv + vec2(cos(angle), sin(angle)) * radius * px, 0.0, 1.0);
        vec3 tapColor = texture(uColorTexture, tapUv).rgb;
        float weight = clamp(tapCoc(tapUv, scaleY) / max(coc, 1e-3), 0.0, 1.0);
        float energy = 1.0 + pow(elliceLuminance(tapColor), 6.0) * 3.0;
        acc += tapColor * (weight * energy);
        wsum += weight * energy;
    }
    return acc / wsum;
}




vec3 applyFxaa(vec2 uv, vec3 center) {
    vec2 px = vec2(1.0) / uResolution;
    float lumaC = elliceLuminance(center);
    float lumaN = elliceLuminance(texture(uColorTexture, uv + vec2(0.0, -1.0) * px).rgb);
    float lumaS = elliceLuminance(texture(uColorTexture, uv + vec2(0.0, 1.0) * px).rgb);
    float lumaW = elliceLuminance(texture(uColorTexture, uv + vec2(-1.0, 0.0) * px).rgb);
    float lumaE = elliceLuminance(texture(uColorTexture, uv + vec2(1.0, 0.0) * px).rgb);
    float contrast = max(max(lumaN, lumaS), max(lumaW, lumaE)) - min(min(lumaN, lumaS), min(lumaW, lumaE));
    
    
    float edgeWeight = smoothstep(0.06, 0.14, contrast);
    if (edgeWeight <= 0.0) return center;
    float silhouette = 1.0;
    if (uDepthAvailable != 0) {
        float dc = linearizeDepth(texture(uDepthTexture, uv).r);
        float step = abs(dc - linearizeDepth(texture(uDepthTexture, uv + vec2(0.0, -1.0) * px).r));
        step = max(step, abs(dc - linearizeDepth(texture(uDepthTexture, uv + vec2(0.0, 1.0) * px).r)));
        step = max(step, abs(dc - linearizeDepth(texture(uDepthTexture, uv + vec2(-1.0, 0.0) * px).r)));
        step = max(step, abs(dc - linearizeDepth(texture(uDepthTexture, uv + vec2(1.0, 0.0) * px).r)));
        silhouette = smoothstep(0.4, 1.2, step);
        if (silhouette <= 0.0) return center;
    }
    vec2 blendUv = uv;
    if (abs(lumaN + lumaS - 2.0 * lumaC) > abs(lumaW + lumaE - 2.0 * lumaC) * 2.0)
        blendUv += vec2(0.0, (lumaN > lumaS ? -0.5 : 0.5)) * px;
    else
        blendUv += vec2((lumaW > lumaE ? -0.5 : 0.5), 0.0) * px;
    vec3 blended = texture(uColorTexture, clamp(blendUv, 0.0, 1.0)).rgb;
    return mix(center, blended, elliceSaturate(uFxaa) * 0.5 * edgeWeight * silhouette);
}

void main() {
    vec2 centered = vTexCoord - 0.5;
    float radial = dot(centered, centered);
    vec2 chromaOffset = centered * max(uChromatic, 0.0) * (0.35 + radial * 2.5);

    vec4 original = texture(uColorTexture, vTexCoord);
    vec3 color;
    color.r = texture(uColorTexture, clamp(vTexCoord + chromaOffset, 0.0, 1.0)).r;
    color.g = original.g;
    color.b = texture(uColorTexture, clamp(vTexCoord - chromaOffset, 0.0, 1.0)).b;

    if (uBloom > 0.0001 || uHalation > 0.0001) {
        vec2 radius = vec2(max(uBloomRadius, 1.0) * uResolution.y / 1080.0) / max(uResolution, vec2(1.0));
        vec3 glow = highlightGlow(vTexCoord, radius);
        color += glow * uBloom * 0.85;
        float rim = max(0.0, elliceLuminance(glow) - elliceLuminance(brightPixel(vTexCoord)) * 0.65);
        color += vec3(1.0, 0.24, 0.075) * rim * uHalation * 0.65;
    }

    color *= exp2(clamp(uExposure, -3.0, 3.0));
    color = elliceAces(color);

    
    float luma = elliceLuminance(color);
    float highlights = smoothstep(0.18, 0.82, luma);
    float warmth = elliceSaturate(uWarmth);
    color *= mix(vec3(1.0), vec3(1.10, 1.0, 0.86), highlights * warmth);
    color += vec3(-0.015, 0.008, 0.020) * (1.0 - highlights) * warmth;

    color = (color - 0.5) * max(uContrast, 0.0) + 0.5;
    luma = elliceLuminance(color);
    color = mix(vec3(luma), color, max(uSaturation, 0.0));

    float vignetteShape = 1.0 - smoothstep(0.18, 0.82, radial);
    color *= mix(1.0, vignetteShape, elliceSaturate(uVignette));

    
    if (uDepthAvailable != 0 && uSsao > 0.0001) color *= cavityAo(vTexCoord);

    
    if (uDepthAvailable != 0 && uDof > 0.0001) color.rgb = depthOfField(vTexCoord, color.rgb);
    if (uFxaa > 0.0001) color.rgb = applyFxaa(vTexCoord, color.rgb);

    float grain = gradeHash(gl_FragCoord.xy + fract(uTime) * 173.0) - 0.5;
    color += grain * max(uGrain, 0.0);

    
    if (uLetterbox > 0.0005
        && (vTexCoord.y < uLetterbox || vTexCoord.y > 1.0 - uLetterbox)) {
        fragColor = vec4(0.0, 0.0, 0.0, 1.0);
        return;
    }

    fragColor = vec4(color, original.a);
}
