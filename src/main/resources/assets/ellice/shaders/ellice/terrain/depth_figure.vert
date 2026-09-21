#version 330 core


layout(location = 0) in vec3 aPosition;
layout(location = 1) in vec3 aNormal;
layout(location = 2) in vec2 aUv;
layout(location = 3) in vec4 aTangent;
uniform mat4 uSunVP;
uniform mat4 uModel;
out vec2 vUv;
void main() {
    gl_Position = uSunVP * uModel * vec4(aPosition, 1.0);
    vUv = aUv;
}
