#version 330 core

layout(location = 0) in vec2 aPos;

uniform vec4 uRect;        
uniform vec2 uResolution;
uniform float uRotation;   

out vec2 vTexCoord;

void main() {
    vec2 centered = aPos - 0.5;
    if (uRotation != 0.0) {
        float r = radians(uRotation);
        float c = cos(r);
        float s = sin(r);
        centered = vec2(c * centered.x - s * centered.y,
                        s * centered.x + c * centered.y);
    }
    vec2 pixel = uRect.xy + (centered + 0.5) * uRect.zw;
    gl_Position = vec4(
        pixel.x / uResolution.x *  2.0 - 1.0,
        pixel.y / uResolution.y * -2.0 + 1.0,
        0.0, 1.0
    );
    vTexCoord = vec2(aPos.x, aPos.y);
}
