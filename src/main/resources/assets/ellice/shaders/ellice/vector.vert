#version 330 core

layout(location = 0) in vec2 aPos;
layout(location = 1) in float aPathDist;

uniform vec2 uResolution;
uniform vec4 uTransform;      
uniform float uRotation;      
uniform vec2 uViewBoxCenter;  

out float vPathDist;

void main() {
    
    vec2 centered = aPos - uViewBoxCenter;

    
    float c = cos(uRotation), s = sin(uRotation);
    vec2 rotated = vec2(centered.x * c - centered.y * s,
                        centered.x * s + centered.y * c);

    
    vec2 pixel = rotated * uTransform.zw + uTransform.xy;

    gl_Position = vec4(
        pixel.x / uResolution.x *  2.0 - 1.0,
        pixel.y / uResolution.y * -2.0 + 1.0,
        0.0, 1.0
    );

    vPathDist = aPathDist;
}
