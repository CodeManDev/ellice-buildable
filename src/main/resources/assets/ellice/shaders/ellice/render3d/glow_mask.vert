#version 330 core

layout(location = 0) in vec3 aPosition;

invariant gl_Position;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProjection;

void main() {
    vec4 world = uModel * vec4(aPosition, 1.0);
    vec4 viewPosition = uView * world;
    gl_Position = uProjection * viewPosition;
}
