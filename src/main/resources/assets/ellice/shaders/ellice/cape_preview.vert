#version 330 core
layout(location=0) in vec3 aPosition;
layout(location=1) in vec3 aNormal;
layout(location=2) in vec2 aUv;
uniform mat4 uMvp;
uniform mat4 uModel;
out vec2 vUv;
out vec3 vNormal;
void main(){vUv=aUv;vNormal=mat3(uModel)*aNormal;gl_Position=uMvp*vec4(aPosition,1.0);}
