#version 330 core








uniform mat4 uInvViewProjection;
uniform vec3 uCameraOffset;
uniform vec3 uSunDirection;
uniform float uDaylight;
uniform float uEdgeFog;
uniform vec3 uFogColor;
uniform float uTime;
uniform vec2 uResolution;
in vec2 vUv;
out vec4 fragColor;

float hash13(vec3 value) {
    vec3 p3 = fract(value * 0.1031);
    p3 += dot(p3, p3.zyx + 31.32);
    return fract((p3.x + p3.y) * p3.z);
}

float hash21c(vec2 value) {
    vec3 p3 = fract(vec3(value.xyx) * 0.1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

float vnoise(vec2 p) {
    vec2 cell = floor(p), frac = fract(p);
    vec2 u = frac * frac * (3.0 - 2.0 * frac);
    return mix(mix(hash21c(cell), hash21c(cell + vec2(1, 0)), u.x),
               mix(hash21c(cell + vec2(0, 1)), hash21c(cell + vec2(1, 1)), u.x), u.y);
}

float fbm(vec2 p) {
    float value = 0.0, amplitude = 0.5;
    for (int i = 0; i < 4; i++) {
        value += amplitude * vnoise(p);
        p = p * 2.03 + vec2(17.3, 9.1);
        amplitude *= 0.5;
    }
    return value;
}

void main() {
    vec4 unprojected = uInvViewProjection * vec4(vUv * 2.0 - 1.0, 1.0, 1.0);
    vec3 ray = normalize(unprojected.xyz / max(abs(unprojected.w), 1e-5) - uCameraOffset);
    float day = clamp(uDaylight, 0.0, 1.0);
    vec3 sun = normalize(uSunDirection);
    float sunAmount = max(dot(ray, sun), 0.0);

    
    vec3 zenith = mix(vec3(0.008, 0.014, 0.038), vec3(0.16, 0.38, 0.72), day);
    vec3 horizon = mix(vec3(0.045, 0.062, 0.105), vec3(0.60, 0.72, 0.85), day);

    
    float lowSun = clamp(1.0 - abs(sun.y) * 2.2, 0.0, 1.0);
    horizon += vec3(1.0, 0.42, 0.18) * pow(sunAmount, 3.0) * lowSun * (0.25 + 0.75 * day);

    float up = clamp(ray.y, -1.0, 1.0);
    vec3 sky = mix(horizon, zenith, pow(clamp(up, 0.0, 1.0), 0.55));
    
    sky = mix(sky, horizon * 0.32, clamp(-up * 3.0, 0.0, 1.0));

    
    
    vec3 cell = floor(ray * 220.0);
    float seed = hash13(cell);
    float star = (step(0.992, seed) * 0.35 + step(0.9985, seed) * 0.65)
        * (1.0 - day) * clamp(up * 2.0, 0.0, 1.0);
    vec3 starColor = mix(vec3(0.70, 0.80, 1.0), vec3(1.0, 0.85, 0.70), hash13(cell + 3.0));
    float twinkle = 0.75 + 0.25 * sin(uTime * 2.0 + hash13(cell + 7.0) * 6.2831);
    sky += starColor * star * twinkle;

    
    vec3 discColor = mix(vec3(0.82, 0.88, 1.0), vec3(1.0, 0.92, 0.78), day);
    float disc = smoothstep(0.99935, 0.99965, sunAmount);
    if (disc > 0.001) {
        
        float maria = fbm(ray.xy * 160.0 + 3.7) * 0.38 * (1.0 - day);
        disc *= 1.0 - maria;
    }
    float halo = pow(sunAmount, 600.0) * 0.9 + pow(sunAmount, 24.0) * 0.22;
    sky += discColor * disc * (0.35 + 0.65 * day);
    sky += discColor * halo * (0.25 + 0.75 * day);

    
    if (ray.y > 0.015) {
        vec2 cuv = ray.xz / max(ray.y, 0.12) * 1.6
            + vec2(uTime * 0.008, uTime * 0.003);
        float density = fbm(cuv);
        float cover = 0.46;
        float clouds = smoothstep(cover, cover + 0.28, density);
        if (clouds > 0.001) {
            vec3 cloudCol = mix(vec3(0.05, 0.06, 0.10), vec3(1.02, 1.0, 0.98), day);
            
            cloudCol += vec3(1.0, 0.5, 0.25) * pow(sunAmount, 5.0) * 0.55 * (0.3 + 0.7 * day);
            cloudCol *= 0.72 + 0.55 * smoothstep(cover, cover + 0.5, density);
            float horizonFade = smoothstep(0.015, 0.22, ray.y);
            float cloudAlpha = clouds * horizonFade * mix(0.55, 1.0, day);
            sky = mix(sky, cloudCol, cloudAlpha);
        }
    }

    
    float hLen = length(ray.xz);
    if (hLen > 1e-3 && day > 0.001) {
        vec2 sunH = sun.xz / max(length(sun.xz), 1e-3);
        float azimuth = max(dot(ray.xz / hLen, sunH), 0.0);
        float flare = pow(azimuth, 60.0) * exp(-abs(ray.y - sun.y) * 22.0);
        sky += vec3(1.0, 0.6, 0.3) * flare * 0.45 * day;
    }

    
    sky = mix(sky, uFogColor, uEdgeFog * (1.0 - smoothstep(-0.03, 0.03, ray.y)));

    
    sky += (hash13(vec3(vUv * uResolution, 1.7)) - 0.5) * (1.5 / 255.0);

    fragColor = vec4(sky, 1.0);
}
