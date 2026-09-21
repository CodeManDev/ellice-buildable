#version 330 core

layout(location = 0) in vec2 aPos;

uniform vec4 uRect;        
uniform vec2 uResolution;

out vec2 vUV;

void main() {
    vec2 pixel = uRect.xy + aPos * uRect.zw;
    gl_Position = vec4(
        pixel.x / uResolution.x *  2.0 - 1.0,
        pixel.y / uResolution.y * -2.0 + 1.0,
        0.0, 1.0
    );
    vUV = aPos;
}
