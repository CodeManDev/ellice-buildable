#version 330 core

layout(location = 0) in vec3 aWorldPos;

uniform mat4 uView;
uniform mat4 uProjection;
uniform vec3 uCameraPos;

void main() {
    gl_Position = uProjection * uView * vec4(aWorldPos - uCameraPos, 1.0);
}
