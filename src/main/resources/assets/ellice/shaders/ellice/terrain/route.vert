#version 330 core
layout(location=0) in vec3 aPosition;
layout(location=1) in vec3 aNormal;
layout(location=2) in vec2 aUv;
layout(location=3) in vec4 aTangent;
uniform mat4 uModel, uView, uProjection;
uniform float uWidth;
out vec2 vUv;
void main() {
    vec3 position = aPosition + aTangent.xyz * aUv.x * (uWidth - 0.125);
    gl_Position = uProjection * uView * uModel * vec4(position, 1.0);
    vUv = vec2(sign(aUv.x), aUv.y);
}
