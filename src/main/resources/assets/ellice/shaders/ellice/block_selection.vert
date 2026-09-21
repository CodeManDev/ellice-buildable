#version 330 core
layout(location=0) in vec3 aPosition;
layout(location=1) in vec3 aNormal;
uniform mat4 uView, uProjection;
uniform vec3 uOrigin;
uniform float uPadding;
out vec3 vLocal, vRelative;
flat out vec3 vNormal;
void main() {
    vLocal=aPosition;vNormal=aNormal;
    vRelative=uOrigin+aPosition+aNormal*uPadding;
    gl_Position=uProjection*uView*vec4(vRelative,1.0);
}
