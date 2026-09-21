#version 330 core

layout(location = 0) in vec2 aPos;

uniform vec4 uRect;        
uniform vec2 uResolution;
uniform float uRotation;   
uniform float uVertexPadding;

out vec2 vTexCoord;

void main() {
    vec2 size = max(uRect.zw, vec2(1.0));
    float pad = max(0.0, uVertexPadding);
    vec2 paddedSize = size + vec2(pad * 2.0);
    vec2 local = aPos * paddedSize - vec2(pad);
    vec2 centered = local - size * 0.5;

    if (uRotation != 0.0) {
        float r = radians(uRotation);
        float c = cos(r);
        float s = sin(r);
        centered = vec2(c * centered.x - s * centered.y,
                        s * centered.x + c * centered.y);
    }

    vec2 pixel = uRect.xy + size * 0.5 + centered;
    gl_Position = vec4(
        pixel.x / uResolution.x *  2.0 - 1.0,
        pixel.y / uResolution.y * -2.0 + 1.0,
        0.0, 1.0
    );

    vTexCoord = local / size;
}
