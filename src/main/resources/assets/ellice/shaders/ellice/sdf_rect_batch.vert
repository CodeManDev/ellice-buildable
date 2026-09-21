#version 430 core

layout(location = 0) in vec2 aPos;

struct RectData {
    vec4 rect;         
    vec4 color;        
    vec4 radius;       
    vec4 shadow;       
    vec4 shadowColor;  
    vec4 borderInfo;   
    vec4 borderColor;  
    vec4 extraInfo;    
    vec4 gradientEnd;  
    vec4 borderColor2; 
};

layout(std430, binding = 0) readonly buffer RectBuffer {
    RectData rects[];
};

uniform vec2 uResolution;
uniform int uRectOffset;

out vec2 vLocalPos;
flat out int vInstanceID;

void main() {
    int idx = gl_InstanceID + uRectOffset;
    RectData r = rects[idx];

    float shadowExtent = r.shadow.z + length(r.shadow.xy) + r.shadow.w;
    float PAD = max(2.0, max(shadowExtent, r.extraInfo.x) + 2.0);

    vec2 padded = r.rect.zw + PAD * 2.0;
    vec2 pixel  = (r.rect.xy - PAD) + aPos * padded;

    gl_Position = vec4(
        pixel.x / uResolution.x *  2.0 - 1.0,
        pixel.y / uResolution.y * -2.0 + 1.0,
        0.0, 1.0
    );

    vLocalPos   = aPos * padded - PAD;
    vInstanceID = idx;
}
