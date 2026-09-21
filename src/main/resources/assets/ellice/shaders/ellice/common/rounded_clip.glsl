

const int ELLICE_MAX_ROUNDED_CLIPS = 8;

uniform vec2 uResolution;
uniform int uClipCount;
uniform vec4 uClipRects[ELLICE_MAX_ROUNDED_CLIPS];
uniform vec4 uClipRadii[ELLICE_MAX_ROUNDED_CLIPS];

float elliceClipCornerRadius(vec2 point, vec4 radii) {
    if (point.x < 0.0) {
        return point.y < 0.0 ? radii.x : radii.w;
    }
    return point.y < 0.0 ? radii.y : radii.z;
}

float elliceRoundedClipDistance(vec2 point, vec2 halfSize, vec4 radii) {
    vec4 clamped = clamp(radii, 0.0, min(halfSize.x, halfSize.y));
    float radius = elliceClipCornerRadius(point, clamped);
    vec2 q = abs(point) - halfSize + vec2(radius);
    return length(max(q, 0.0)) + min(max(q.x, q.y), 0.0) - radius;
}

float elliceRoundedClipMask() {
    if (uClipCount <= 0) return 1.0;

    
    vec2 fragment = vec2(gl_FragCoord.x, uResolution.y - gl_FragCoord.y);
    float mask = 1.0;
    for (int index = 0; index < ELLICE_MAX_ROUNDED_CLIPS; index++) {
        if (index >= uClipCount) break;
        vec4 rect = uClipRects[index];
        vec2 halfSize = rect.zw * 0.5;
        float distance = elliceRoundedClipDistance(
            fragment - (rect.xy + halfSize), halfSize, uClipRadii[index]);
        float antialias = max(0.5, fwidth(distance));
        mask *= 1.0 - smoothstep(-antialias, antialias, distance);
    }
    return mask;
}
