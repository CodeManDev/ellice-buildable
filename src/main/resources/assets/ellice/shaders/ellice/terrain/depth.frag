#version 330 core


uniform sampler2D uAtlas;
uniform float uAlphaCutoff;
uniform int uUseAtlas;
in vec2 vUv;
void main() {
    if (uUseAtlas == 1 && texture(uAtlas, vUv).a < uAlphaCutoff) discard;
}
