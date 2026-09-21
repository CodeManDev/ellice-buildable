#version 330 core
uniform sampler2D uBaseColorMap;
uniform int uHasBaseColorMap;
uniform vec4 uBaseColor;
uniform float uAlphaCutoff;
in vec2 vUv;
void main() {
    float alpha = uBaseColor.a;
    if (uHasBaseColorMap != 0) alpha *= texture(uBaseColorMap, vUv).a;
    if (alpha < uAlphaCutoff) discard;
}
