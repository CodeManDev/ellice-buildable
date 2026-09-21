#version 330 core

layout(location = 0) in vec2 aPos;
layout(location = 1) in vec2 aTexCoord;
layout(location = 2) in vec4 aColor;

uniform vec2 uResolution;
uniform float uFocusX;      
uniform float uFocusWidth;  
uniform float uTime;

out vec2 vTexCoord;
out vec4 vColor;
out float vBlur;

void main() {
    vec2 pos = aPos;

    float dx = abs(pos.x - uFocusX);
    float blur = smoothstep(uFocusWidth * 0.5, uFocusWidth * 3.0, dx);

    
    pos.y += blur * 0.6 * sin(pos.x * 0.04 + uTime * 1.2);
    
    pos.x += blur * 0.3 * cos(pos.x * 0.05 + uTime * 0.9 + 1.0);

    gl_Position = vec4(
        pos.x / uResolution.x *  2.0 - 1.0,
        pos.y / uResolution.y * -2.0 + 1.0,
        0.0, 1.0
    );
    vTexCoord = aTexCoord;
    vColor = aColor;
    vBlur = blur;
}
