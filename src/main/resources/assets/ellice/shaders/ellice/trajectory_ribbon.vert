#version 330 core

layout(location = 0) in vec3 aWorldPos;
layout(location = 1) in vec3 aOtherWorldPos;
layout(location = 2) in float aSide;
layout(location = 3) in float aProgress;
layout(location = 4) in float aAlpha;

uniform mat4 uView;
uniform mat4 uProjection;
uniform vec3 uCameraPos;
uniform vec2 uResolution;
uniform float uWidthPx;

out float vSide;
out float vProgress;
out float vAlpha;

vec4 projectPoint(vec3 worldPos) {
    return uProjection * uView * vec4(worldPos - uCameraPos, 1.0);
}

void main() {
    vec4 here = projectPoint(aWorldPos);
    vec4 other = projectPoint(aOtherWorldPos);

    vec2 hereNdc = here.xy / max(here.w, 0.0001);
    vec2 otherNdc = other.xy / max(other.w, 0.0001);
    vec2 dir = (otherNdc - hereNdc) * uResolution;

    if (dot(dir, dir) < 0.0001) {
        dir = vec2(1.0, 0.0);
    } else {
        dir = normalize(dir);
    }

    vec2 normal = vec2(-dir.y, dir.x);
    vec2 ndcOffset = normal * aSide * uWidthPx / uResolution * 2.0;
    here.xy += ndcOffset * here.w;

    gl_Position = here;
    vSide = aSide;
    vProgress = aProgress;
    vAlpha = aAlpha;
}
