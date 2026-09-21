#version 330 core

layout(location = 0) in vec3 aWorldPos;
layout(location = 1) in vec3 aNormal;
layout(location = 2) in vec2 aUv;
layout(location = 3) in vec2 aFx;

uniform mat4 uView;
uniform mat4 uProjection;
uniform vec3 uCameraPos;

out vec3 vNormal;
out vec2 vUv;
out vec2 vFx;
out vec3 vViewPos;

void main() {
    vec3 local = aWorldPos - uCameraPos;
    vNormal = normalize(aNormal);
    vUv = aUv;
    vFx = aFx;
    vViewPos = local;
    gl_Position = uProjection * uView * vec4(local, 1.0);
}
