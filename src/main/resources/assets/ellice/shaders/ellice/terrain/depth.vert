#version 330 core


layout(location = 0) in vec3 aPosition;
layout(location = 1) in vec4 aColor;
layout(location = 2) in vec2 aUv;
layout(location = 3) in vec2 aLight;
uniform mat4 uSunVP;
uniform vec3 uOrigin;
out vec2 vUv;
void main() {
    gl_Position = uSunVP * vec4(aPosition + uOrigin, 1.0);
    vUv = aUv;
}
