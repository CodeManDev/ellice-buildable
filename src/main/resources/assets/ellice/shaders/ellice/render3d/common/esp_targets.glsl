#pragma once
uniform sampler2D uTargetData;
uniform vec4 uDamageColor;


vec2 espTarget(float encodedId) {
    return texelFetch(uTargetData, ivec2(clamp(int(round(encodedId * 255.0)), 0, 255), 0), 0).rg;
}
vec3 espDamage(vec3 color, float encodedId) {
    float amount = espTarget(encodedId).g * uDamageColor.a;
    return mix(color, uDamageColor.rgb, amount);
}


uniform sampler2D uForegroundDepth;
uniform int uForegroundActive;
bool espForeground(ivec2 pixel) {
    return uForegroundActive != 0 && texelFetch(uForegroundDepth, pixel, 0).r < 0.999999;
}

uniform sampler2D uEspModelDepth;
uniform sampler2D uEspSceneDepth;
float espViewDepth(ivec2 pixel, float raw, mat4 inverseProjection) {
    vec2 uv = (vec2(pixel) + 0.5) / vec2(textureSize(uEspSceneDepth, 0));
    vec4 view = inverseProjection * vec4(uv * 2.0 - 1.0, raw * 2.0 - 1.0, 1.0);
    return max(-view.z / max(abs(view.w), 0.00001), 0.0);
}
float espFrontGate(ivec2 sourcePixel, ivec2 scenePixel, mat4 inverseProjection) {
    float sceneRaw = texelFetch(uEspSceneDepth, scenePixel, 0).r;
    if (sceneRaw >= 0.999999) return 1.0;
    float sourceDepth = espViewDepth(sourcePixel, texelFetch(uEspModelDepth, sourcePixel, 0).r, inverseProjection);
    float sceneDepth = espViewDepth(scenePixel, sceneRaw, inverseProjection);
    return smoothstep(-max(0.10, sourceDepth * 0.0025), 0.0, sceneDepth - sourceDepth);
}
vec2 espVisibility(ivec2 sourcePixel, ivec2 fragmentPixel, mat4 inverseProjection) {
    
    
    float sourceFront = espFrontGate(sourcePixel, sourcePixel, inverseProjection);
    float fragmentFront = espFrontGate(sourcePixel, fragmentPixel, inverseProjection);
    return vec2(sourceFront * fragmentFront, (1.0 - sourceFront) * (1.0 - fragmentFront));
}
