#version 330 core

layout(location = 0) in vec2 aPos;

uniform vec4 uRect;        
uniform vec2 uResolution;  
uniform vec4 uShadow;      
uniform float uEdgeSoftness;

out vec2 vLocalPos;

void main() {
    
    float shadowExtent = uShadow.z + length(uShadow.xy) + uShadow.w;
    float PAD = max(2.0, max(shadowExtent, uEdgeSoftness) + 2.0);

    vec2 padded = uRect.zw + PAD * 2.0;
    vec2 pixel  = (uRect.xy - PAD) + aPos * padded;

    gl_Position = vec4(
        pixel.x / uResolution.x *  2.0 - 1.0,
        pixel.y / uResolution.y * -2.0 + 1.0,
        0.0, 1.0
    );

    vLocalPos = aPos * padded - PAD;
}
