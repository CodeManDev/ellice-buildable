#version 430 core

#include <common/rounded_clip.glsl>

in vec2 vLocalPos;
flat in int vInstanceID;

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

uniform sampler2D uBlurTexture;

out vec4 fragColor;

float roundedBoxSDF(vec2 p, vec2 b, vec4 r) {
    r.xy = (p.x > 0.0) ? r.yz : r.xw;
    r.x  = (p.y > 0.0) ? r.y  : r.x;
    vec2 q = abs(p) - b + min(r.x, min(b.x, b.y));
    return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - min(r.x, min(b.x, b.y));
}

vec4 over(vec4 top, vec4 bot) {
    return top + bot * (1.0 - top.a);
}




const float BAYER8[64] = float[64](
     0.0, 32.0,  8.0, 40.0,  2.0, 34.0, 10.0, 42.0,
    48.0, 16.0, 56.0, 24.0, 50.0, 18.0, 58.0, 26.0,
    12.0, 44.0,  4.0, 36.0, 14.0, 46.0,  6.0, 38.0,
    60.0, 28.0, 52.0, 20.0, 62.0, 30.0, 54.0, 22.0,
     3.0, 35.0, 11.0, 43.0,  1.0, 33.0,  9.0, 41.0,
    51.0, 19.0, 59.0, 27.0, 49.0, 17.0, 57.0, 25.0,
    15.0, 47.0,  7.0, 39.0, 13.0, 45.0,  5.0, 37.0,
    63.0, 31.0, 55.0, 23.0, 61.0, 29.0, 53.0, 21.0);
float ditherBias(vec2 fragCoord) {
    ivec2 ip = ivec2(fragCoord) % 8;
    return (BAYER8[ip.y * 8 + ip.x] + 0.5) / 64.0 - 0.5; 
}

float luminance(vec3 rgb) {
    return dot(rgb, vec3(0.2126, 0.7152, 0.0722));
}

vec3 sampleBevelSource(vec2 uv) {
    return texture(uBlurTexture, clamp(uv, vec2(0.001), vec2(0.999))).rgb;
}

vec3 blurBevelResponse(vec2 uv, vec2 normal, vec2 tangent, float edgeBand, float opacity) {
    if (edgeBand <= 0.001 || opacity <= 0.001) return vec3(0.0);

    float reachPx = 9.5;
    vec2 n = normal * (reachPx / uResolution);
    vec2 t = tangent * (reachPx * 0.72 / uResolution);

    vec3 outside = sampleBevelSource(uv + n) * 0.50
        + sampleBevelSource(uv + n * 1.65 + t * 0.55) * 0.25
        + sampleBevelSource(uv + n * 1.65 - t * 0.55) * 0.25;
    vec3 inside = sampleBevelSource(uv - n * 0.65) * 0.54
        + sampleBevelSource(uv - n * 0.25 + t * 0.70) * 0.23
        + sampleBevelSource(uv - n * 0.25 - t * 0.70) * 0.23;
    vec3 tangentA = sampleBevelSource(uv + t);
    vec3 tangentB = sampleBevelSource(uv - t);

    float lumOut = luminance(outside);
    float lumIn = luminance(inside);
    float grad = lumOut - lumIn;
    float contrast = smoothstep(0.006, 0.13, abs(grad));
    float hot = smoothstep(0.35, 0.90, max(lumOut, lumIn));
    float lit = max(grad, 0.0) * contrast * (0.52 + 0.78 * hot);
    float shadow = max(-grad, 0.0) * contrast * 0.12;
    float tangentContrast = smoothstep(0.025, 0.18, abs(luminance(tangentA) - luminance(tangentB)));
    vec3 tangentColor = luminance(tangentA) > luminance(tangentB) ? tangentA : tangentB;
    vec3 highlight = mix(outside, outside / max(lumOut, 0.001), 0.18);
    vec3 delta = highlight * lit + tangentColor * tangentContrast * hot * 0.10;
    delta -= vec3(0.32, 0.34, 0.38) * shadow;
    float gain = clamp(0.35 + opacity * 1.35, 0.0, 1.35);
    return delta * edgeBand * opacity * gain;
}

void main() {
    RectData r = rects[vInstanceID];

    vec2 halfSize = r.rect.zw * 0.5;
    vec2 p = vLocalPos - halfSize;

    float dist = roundedBoxSDF(p, halfSize, r.radius);
    
    float aa   = 0.5 * fwidth(dist);
    float edge = max(aa, r.extraInfo.x);

    
    float gradT = clamp(vLocalPos.y / r.rect.w, 0.0, 1.0);
    bool hasGradient = r.gradientEnd.a > 0.001;

    
    vec4 result = vec4(0.0);
    bool inset = r.extraInfo.y > 0.5;

    if (r.shadow.z > 0.0) {
        vec2 sp = p - r.shadow.xy;
        float sd;
        float sa;
        if (inset) {
            
            sd = roundedBoxSDF(sp, halfSize - r.shadow.w, r.radius);
            sa = r.shadowColor.a * smoothstep(-r.shadow.z, r.shadow.z, sd);
        } else {
            
            sd = roundedBoxSDF(sp, halfSize + r.shadow.w, r.radius);
            sa = r.shadowColor.a * (1.0 - smoothstep(-r.shadow.z, r.shadow.z, sd));
            result = vec4(r.shadowColor.rgb * sa, sa);
        }
    }

    
    float fillMask = 1.0 - smoothstep(-edge, edge, dist);

    
    vec4 fillColor = r.color;
    if (hasGradient) {
        fillColor = mix(r.color, r.gradientEnd, gradT);
    }

    vec3 fillRGB;
    float fillA;

    bool hasBlur = r.borderInfo.y > 0.5;
    vec2 screenUV = gl_FragCoord.xy / uResolution;
    if (hasBlur) {
        vec3 blurred = texture(uBlurTexture, screenUV).rgb;
        float lum = dot(blurred, vec3(0.2126, 0.7152, 0.0722));
        float vibrancy = r.borderInfo.z;
        blurred = mix(vec3(lum), blurred, vibrancy);
        blurred *= 1.05;
        float panelOpacity = r.borderInfo.w;
        fillRGB = mix(blurred, fillColor.rgb, fillColor.a);
        fillA   = fillMask * panelOpacity;
    } else {
        fillRGB = fillColor.rgb;
        fillA   = fillColor.a * fillMask;
    }

    result = over(vec4(fillRGB * fillA, fillA), result);

    if (hasBlur) {
        vec2 n = length(p) > 0.0001 ? normalize(p) : vec2(0.0, -1.0);
        vec2 t = vec2(-n.y, n.x);
        float rimWidth = max(2.0, r.borderInfo.x * 3.0 + edge * 2.5);
        float rim = (1.0 - smoothstep(0.0, rimWidth, abs(dist))) * fillMask;
        vec3 bevel = blurBevelResponse(screenUV, n, t, pow(rim, 1.25), r.borderInfo.w);
        result.rgb = clamp(result.rgb + bevel * fillMask, 0.0, 1.0);
    }

    
    if (inset && r.shadow.z > 0.0) {
        vec2 sp = p - r.shadow.xy;
        float sd = roundedBoxSDF(sp, halfSize - r.shadow.w, r.radius);
        float sa = r.shadowColor.a * smoothstep(-r.shadow.z, r.shadow.z, sd) * fillMask;
        vec4 insetColor = vec4(r.shadowColor.rgb * sa, sa);
        result = over(insetColor, result);
    }

    
    
    
    float ih = r.extraInfo.z;
    if (ih > 0.001) {
        float size = r.extraInfo.w > 0.5 ? r.extraInfo.w : max(r.rect.w * 0.42, 8.0);
        float topT = clamp(vLocalPos.y / size, 0.0, 1.0);
        float falloff = pow(1.0 - topT, 1.6);
        float ha = ih * falloff * fillMask;
        result = over(vec4(vec3(1.0) * ha, ha), result);
    }

    
    
    
    
    
    
    float bw = r.borderInfo.x;
    if (bw > 0.0) {
        vec4 bc = r.borderColor;
        if (r.borderColor2.a > 0.001) {
            vec2 n = (length(p) > 0.0001) ? normalize(p) : vec2(0.0, -1.0);
            float t = clamp(-n.y * 0.5 + 0.5, 0.0, 1.0);
            t = smoothstep(0.0, 1.0, t);
            bc = mix(r.borderColor2, r.borderColor, t);
        }
        float bd = abs(dist) - bw * 0.5;
        float ba = bc.a * (1.0 - smoothstep(-edge, edge, bd));
        result = over(vec4(bc.rgb * ba, ba), result);
    }

    
    
    result.rgb += ditherBias(gl_FragCoord.xy) * result.a / 255.0;

    fragColor = result * elliceRoundedClipMask();
}
