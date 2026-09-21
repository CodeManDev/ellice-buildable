#version 330 core

layout(location = 0) in vec2 aPos;
layout(location = 1) in vec2 aTexCoord;
layout(location = 2) in vec4 aColor;

uniform vec2 uResolution;
uniform float uTime;
uniform float uWaveAmplitude;
uniform float uWaveFrequency;

out vec2 vTexCoord;
out vec4 vColor;

void main() {
    vec2 pos = aPos;

    if (uWaveAmplitude > 0.0) {
        float phase = pos.x * 0.015 + uTime * uWaveFrequency;
        pos.y += sin(phase) * uWaveAmplitude;
        pos.x += cos(phase * 1.3 + 0.7) * uWaveAmplitude * 0.15;
    }

    gl_Position = vec4(
        pos.x / uResolution.x *  2.0 - 1.0,
        pos.y / uResolution.y * -2.0 + 1.0,
        0.0, 1.0
    );
    vTexCoord = aTexCoord;
    vColor = aColor;
}
