#version 330 core

#include <common/rounded_clip.glsl>











in vec2 vUV;

uniform vec4 uRect;      
uniform vec4 uRadius;    
uniform float uOpacity;
uniform float uTime;
uniform vec3 uColor0, uColor1, uColor2, uColor3, uColor4;

out vec4 fragColor;

float roundedBoxSDF(vec2 p, vec2 b, vec4 r) {
    r.xy = (p.x > 0.0) ? r.yz : r.xw;
    r.x  = (p.y > 0.0) ? r.y  : r.x;
    vec2 q = abs(p) - b + min(r.x, min(b.x, b.y));
    return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - min(r.x, min(b.x, b.y));
}

void main() {
    vec2 halfSize = uRect.zw * 0.5;
    vec2 pixel = vUV * uRect.zw;
    float dist = roundedBoxSDF(pixel - halfSize, halfSize, uRadius);
    float aa = fwidth(dist);
    float mask = 1.0 - smoothstep(-aa, aa, dist);
    if (mask <= 0.0) discard;

    
    float t = uTime * 0.20;

    vec2 p0 = vec2(0.20 + 0.06*sin(t*0.7),       0.30 + 0.08*cos(t*0.9 + 1.0));
    vec2 p1 = vec2(0.80 + 0.05*cos(t*0.6 + 2.0), 0.25 + 0.07*sin(t*0.8 + 0.5));
    vec2 p2 = vec2(0.50 + 0.09*sin(t*1.1 + 1.5), 0.65 + 0.09*cos(t*1.3 + 2.5));
    vec2 p3 = vec2(0.15 + 0.05*cos(t*0.7 + 3.0), 0.75 + 0.07*sin(t*0.9 + 1.2));
    vec2 p4 = vec2(0.85 + 0.04*sin(t*1.0 + 0.8), 0.60 + 0.08*cos(t*0.85 + 3.5));

    
    
    float s2 = 0.18;
    float w0 = exp(-dot(vUV - p0, vUV - p0) / s2);
    float w1 = exp(-dot(vUV - p1, vUV - p1) / s2);
    float w2 = exp(-dot(vUV - p2, vUV - p2) / s2);
    float w3 = exp(-dot(vUV - p3, vUV - p3) / s2);
    float w4 = exp(-dot(vUV - p4, vUV - p4) / s2);
    float wSum = w0 + w1 + w2 + w3 + w4 + 0.0001;

    vec3 color = (uColor0*w0 + uColor1*w1 + uColor2*w2 + uColor3*w3 + uColor4*w4) / wSum;

    float a = uOpacity * mask;
    fragColor = vec4(color * a, a) * elliceRoundedClipMask();
}
