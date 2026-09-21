
uniform sampler2D uShadowMap;
uniform mat4 uShadowMatrix;
uniform float uShadowStrength;
uniform vec3 uGradeTint;
uniform vec3 uCameraOffset;
uniform vec3 uFogColor;
uniform vec3 uFogRange;

uniform float uShadowBias;
uniform int uShadowWide;



uniform float uShadowSimple;
uniform sampler2D uCoverage;
uniform vec3 uCoverageOrigin;
uniform float uEdgeFog;



float shadowTap(vec2 tap, vec3 receiver, vec2 gradient, float bias) {
    float depth = receiver.z + dot(gradient, tap - receiver.xy) - bias;
    return step(depth, texture(uShadowMap, tap).r);
}
float shadowCompare(vec2 uv, vec3 receiver, vec2 gradient, float bias) {
    vec2 size = vec2(textureSize(uShadowMap, 0));
    vec2 pixel = uv * size - 0.5;
    vec2 base = (floor(pixel) + 0.5) / size;
    vec2 fraction = fract(pixel);
    vec2 texel = 1.0 / size;
    float a = shadowTap(base, receiver, gradient, bias);
    float b = shadowTap(base + vec2(texel.x, 0), receiver, gradient, bias);
    float c = shadowTap(base + vec2(0, texel.y), receiver, gradient, bias);
    float d = shadowTap(base + texel, receiver, gradient, bias);
    return mix(mix(a,b,fraction.x), mix(c,d,fraction.x), fraction.y);
}
vec3 mapShadow(vec3 position) {
    vec4 clip = uShadowMatrix * vec4(position, 1.0);
    vec3 uv = clip.xyz / clip.w * 0.5 + 0.5;
    
    
    vec3 dx = dFdx(uv), dy = dFdy(uv);
    float determinant = dx.x * dy.y - dx.y * dy.x;
    vec2 gradient = vec2(0.0);
    if (abs(determinant) > 1e-12)
        gradient = vec2(dx.z * dy.y - dx.y * dy.z, dx.x * dy.z - dx.z * dy.x) / determinant;
    if (clip.w <= 0.0 || uShadowStrength <= 0.0) return vec3(1.0);
    if (any(lessThanEqual(uv, vec3(0.0))) || any(greaterThanEqual(uv, vec3(1.0)))) return vec3(1.0);
    vec2 texel = 1.0 / vec2(textureSize(uShadowMap, 0));
    float bias = max(uShadowBias, 2.0 / 16777216.0);
    
    
    
    
    float blocker = 0.0;
    int blockers = 0;
    for (int i = 0; i < 4; i++) {
        vec2 corner = uv.xy + vec2(i == 0 || i == 3 ? 1.0 : -1.0, i < 2 ? 1.0 : -1.0) * texel * 2.0;
        float depth = texture(uShadowMap, corner).r;
        if (depth < uv.z - bias) { blocker += depth; blockers++; }
    }
    
    
    
    
    float penumbraMax = uShadowWide != 0 ? 16.0 : 4.0;
    float penumbra = 1.0;
    if (blockers > 0) penumbra = clamp((uv.z - blocker / float(blockers)) * 1200.0, 1.0, penumbraMax);
    
    
    float lit = 0.0;
    float taps = 0.0;
    for (int i = 0; i < 8; i++) {
        if (uShadowWide == 0 && i >= 4) break;
        float fi = float(i);
        float count = uShadowWide != 0 ? 8.0 : 4.0;
        float radius = sqrt((fi + 0.5) / count) * penumbra;
        float angle = fi * 2.399963 + 0.7;
        vec2 tap = uv.xy + vec2(cos(angle), sin(angle)) * radius * texel;
        if (uShadowSimple > 0.5) lit += step(uv.z - bias, texture(uShadowMap, tap).r);
        else lit += shadowCompare(tap, uv, gradient, bias);
        taps += 1.0;
    }
    float edge = min(min(uv.x, 1.0-uv.x), min(uv.y, 1.0-uv.y));
    
    
    
    float lightR = length((uv.xy - 0.5) * 2.0);
    float rangeFade = 1.0 - smoothstep(0.55, 0.95, lightR);
    
    
    
    float eyeDist = length(position - uCameraOffset);
    float eyeFade = 1.0 - smoothstep(150.0, 400.0, eyeDist);
    float shadow = (1.0 - lit / max(taps, 1.0)) * uShadowStrength * smoothstep(0.0, 0.04, edge) * rangeFade * eyeFade;
    
    return mix(vec3(1.0), vec3(0.60, 0.66, 0.76), shadow);
}
vec3 mapGrade(vec3 color) {
    float luma = dot(color, vec3(0.299, 0.587, 0.114));
    return ((mix(vec3(luma), color, 1.07) - 0.5) * 1.05 + 0.5) * uGradeTint;
}
vec3 mapFog(vec3 color, vec3 position) {
    float amount = smoothstep(uFogRange.x, uFogRange.y, length(position - uCameraOffset)) * uFogRange.z;
    if (uEdgeFog > 0.0) {
        vec2 uv = (position.xz - uCoverageOrigin.xy) / uCoverageOrigin.z;
        vec4 coverage = texture(uCoverage, clamp(uv, 0.0, 1.0));
        float distanceToGap = coverage.r * 64.0;
        if (any(lessThan(uv, vec2(0.0))) || any(greaterThan(uv, vec2(1.0)))) distanceToGap = 0.0;
        
        
        
        
        float edgeAmount = (1.0 - smoothstep(8.0, 40.0, distanceToGap)) * (1.0 - coverage.a);
        amount = max(amount, edgeAmount * uEdgeFog);
    }
    return mix(color, uFogColor, amount);
}
