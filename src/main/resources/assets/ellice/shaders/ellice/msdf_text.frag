#version 330 core

#include <common/rounded_clip.glsl>

in vec2 vTexCoord;
in vec4 vColor;
in vec2 vCellMin;
in vec2 vCellMax;

uniform sampler2D uAtlas;
uniform float uPxRange;


uniform float uOutlineWidth;
uniform vec4 uOutlineColor;


uniform vec4 uShadow;       
uniform vec4 uShadowColor;




uniform float uEdgeSoftness;

out vec4 fragColor;

const float BASE_EDGE_SOFTNESS = 0.48;

float median(float r, float g, float b) {
    return max(min(r, g), min(max(r, g), b));
}

float screenPxRange() {
    vec2 unitRange = vec2(uPxRange) / vec2(textureSize(uAtlas, 0));
    vec2 screenTexSize = vec2(1.0) / fwidth(vTexCoord);
    return max(0.5 * dot(unitRange, screenTexSize), 1.0);
}

float sampleSDF(vec2 uv) {
    vec3 msd = texture(uAtlas, uv).rgb;
    return median(msd.r, msd.g, msd.b);
}

float contourAlpha(float dist, float extraSoft) {
    
    
    
    float soft = BASE_EDGE_SOFTNESS + max(extraSoft, 0.0);
    return smoothstep(-soft, soft, dist);
}

void main() {
    float spr = screenPxRange();
    
    
    
    
    
    float halfRange = spr * 0.5;
    
    
    float softTotal = min(BASE_EDGE_SOFTNESS + max(uEdgeSoftness, 0.0), halfRange);
    float extraSoft = softTotal - BASE_EDGE_SOFTNESS; 
    float sd = sampleSDF(vTexCoord);
    float dist = spr * (sd - 0.5);
    float fillAlpha = contourAlpha(dist, extraSoft);

    
    vec4 result;
    if (uOutlineWidth > 0.0) {
        
        
        float ow = min(uOutlineWidth, max(halfRange - softTotal, 0.0));
        float outerAlpha = contourAlpha(dist + ow, extraSoft);
        float fa = vColor.a * fillAlpha;
        float oa = uOutlineColor.a * max(outerAlpha - fillAlpha, 0.0);
        vec3 color = vColor.rgb * fa + uOutlineColor.rgb * oa * (1.0 - fa);
        float alpha = fa + oa * (1.0 - fa);
        result = vec4(color, alpha);
    } else {
        float a = vColor.a * fillAlpha;
        result = vec4(vColor.rgb * a, a);
    }

    
    if (uShadow.z > 0.0) {
        
        vec2 uvPerPx = fwidth(vTexCoord);
        vec2 shadowUV = vTexCoord - uShadow.xy * uvPerPx;
        
        
        
        
        shadowUV = clamp(shadowUV, vCellMin, vCellMax);

        float shadowSd = sampleSDF(shadowUV);
        float shadowDist = spr * (shadowSd - 0.5);
        
        
        
        
        
        
        
        
        float shadowWidth = min(uShadow.z, halfRange * 2.0);
        float shadowAlpha = smoothstep(-0.5, 0.5, shadowDist / shadowWidth);

        float sa = uShadowColor.a * shadowAlpha;
        vec4 shadow = vec4(uShadowColor.rgb * sa, sa);

        
        result = result + shadow * (1.0 - result.a);
    }

    fragColor = result * elliceRoundedClipMask();
}
