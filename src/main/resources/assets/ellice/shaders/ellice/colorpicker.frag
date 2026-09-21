#version 330 core

#include <common/rounded_clip.glsl>

in vec2 vUV;

uniform int uMode;       
uniform float uHue;      
uniform float uSat;      
uniform float uBri;      
uniform vec4 uRect;      
uniform float uRadius;   
uniform float uOpacity;  
                         

out vec4 fragColor;

const float PI  = 3.14159265;
const float TAU = 6.28318530;

vec3 hsb2rgb(float h, float s, float b) {
    vec3 c = clamp(abs(mod(h * 6.0 + vec3(0.0, 4.0, 2.0), 6.0) - 3.0) - 1.0, 0.0, 1.0);
    return b * mix(vec3(1.0), c, s);
}

float roundedBoxSDF(vec2 p, vec2 b, float r) {
    vec2 q = abs(p) - b + min(r, min(b.x, b.y));
    return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - min(r, min(b.x, b.y));
}


float sdSegment(vec2 p, vec2 a, vec2 b) {
    vec2 pa = p - a, ba = b - a;
    float h = clamp(dot(pa, ba) / dot(ba, ba), 0.0, 1.0);
    return length(pa - ba * h);
}


float cross2(vec2 a, vec2 b) { return a.x * b.y - a.y * b.x; }

void main() {
    vec3 rgb = vec3(0.0);
    float mask = 0.0;
    float size = min(uRect.z, uRect.w);

    if (uMode == 3) {
        
        
        
        vec2 c = (vUV - 0.5) * uRect.zw;  
        float dist = length(c);

        float outerR = size * 0.47;
        float innerR = size * 0.39;
        float aa = 1.2;

        
        float ringOuter = dist - outerR;
        float ringInner = innerR - dist;
        float ringMask = (1.0 - smoothstep(-aa, aa, ringOuter)) * (1.0 - smoothstep(-aa, aa, ringInner));

        if (ringMask > 0.001) {
            float angle = fract(atan(c.y, c.x) / TAU + 0.25);
            rgb = hsb2rgb(angle, 1.0, 1.0);
            mask = ringMask;
        }

        
        float triR = size * 0.35;
        float ha = uHue * TAU - PI * 0.5;
        vec2 v0 = vec2(cos(ha),           sin(ha))           * triR;  
        vec2 v1 = vec2(cos(ha + TAU/3.0), sin(ha + TAU/3.0)) * triR;  
        vec2 v2 = vec2(cos(ha - TAU/3.0), sin(ha - TAU/3.0)) * triR;  

        
        float d00 = dot(v1 - v0, v1 - v0);
        float d01 = dot(v1 - v0, v2 - v0);
        float d11 = dot(v2 - v0, v2 - v0);
        float d20 = dot(c - v0, v1 - v0);
        float d21 = dot(c - v0, v2 - v0);
        float denom = d00 * d11 - d01 * d01;

        if (abs(denom) > 0.001) {
            float bary1 = (d11 * d20 - d01 * d21) / denom;
            float bary2 = (d00 * d21 - d01 * d20) / denom;
            float bary0 = 1.0 - bary1 - bary2;

            
            float sd = min(min(
                sdSegment(c, v0, v1),
                sdSegment(c, v1, v2)),
                sdSegment(c, v2, v0));

            
            float inside = (bary0 >= 0.0 && bary1 >= 0.0 && bary2 >= 0.0) ? 1.0 : 0.0;
            float triMask = inside * smoothstep(0.0, aa, sd);
            
            if (bary0 >= -0.02 && bary1 >= -0.02 && bary2 >= -0.02) {
                triMask = smoothstep(-aa, aa, sd * (inside > 0.0 ? 1.0 : -1.0));
            }

            if (triMask > 0.001) {
                
                float b0 = max(bary0, 0.0);
                float b1 = max(bary1, 0.0);
                float b2 = max(bary2, 0.0);
                float sum = b0 + b1 + b2;
                b0 /= sum; b1 /= sum; b2 /= sum;

                vec3 pureColor = hsb2rgb(uHue, 1.0, 1.0);
                rgb = b0 * pureColor + b1 * vec3(1.0) + b2 * vec3(0.0);
                mask = max(mask, triMask);
            }
        }

    } else if (uMode == 2) {
        
        
        
        vec2 center = vUV - 0.5;
        float aspect = uRect.z / uRect.w;
        center.x *= aspect;
        float dist = length(center) * 2.0;
        float angle = atan(center.y, center.x) / TAU + 0.5;

        rgb = hsb2rgb(angle, min(dist, 1.0), uHue);

        float radius = size * 0.5;
        float circleDist = length((vUV - 0.5) * uRect.zw) - radius + 1.0;
        float aa = fwidth(circleDist);
        mask = 1.0 - smoothstep(-aa, aa, circleDist);

    } else if (uMode == 0) {
        
        rgb = hsb2rgb(uHue, vUV.x, 1.0 - vUV.y);
        vec2 localPos = vUV * uRect.zw;
        vec2 halfSize = uRect.zw * 0.5;
        vec2 p = localPos - halfSize;
        float d = roundedBoxSDF(p, halfSize, uRadius);
        float aa = fwidth(d);
        mask = 1.0 - smoothstep(-aa, aa, d);

    } else if (uMode == 4) {
        
        
        
        
        vec2 localPos = vUV * uRect.zw;
        vec2 halfSize = uRect.zw * 0.5;
        vec2 p = localPos - halfSize;
        float d = roundedBoxSDF(p, halfSize, uRadius);
        float aa = fwidth(d);
        float shapeMask = 1.0 - smoothstep(-aa, aa, d);

        float aGrad = clamp(vUV.x, 0.0, 1.0);
        vec3 pickedRGB = hsb2rgb(uHue, uSat, uBri);

        
        
        
        vec2 checkUV = floor(localPos / 4.0);
        float check = mod(checkUV.x + checkUV.y, 2.0);
        vec3 checkerBG = mix(vec3(0.62), vec3(0.45), check);

        rgb = mix(checkerBG, pickedRGB, aGrad);
        mask = shapeMask;

    } else {
        
        rgb = hsb2rgb(vUV.x, 1.0, 1.0);
        vec2 localPos = vUV * uRect.zw;
        vec2 halfSize = uRect.zw * 0.5;
        vec2 p = localPos - halfSize;
        float d = roundedBoxSDF(p, halfSize, uRadius);
        float aa = fwidth(d);
        mask = 1.0 - smoothstep(-aa, aa, d);
    }

    float a = mask * uOpacity;
    fragColor = vec4(rgb * a, a) * elliceRoundedClipMask();
}
