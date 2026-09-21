#version 330 core

#include <common/rounded_clip.glsl>

in vec2 vTexCoord;

uniform sampler2D uTexture;
uniform float uOpacity;
uniform vec2 uSize;
uniform vec4 uRadius; 
uniform float uEdgeSoftness;
uniform vec4 uTint;
uniform vec4 uUvRect;
uniform float uForceOpaque; 

out vec4 fragColor;

float cornerRadius(vec2 p, vec4 r) {
    if (p.x < 0.0) {
        return p.y < 0.0 ? r.x : r.w;
    }
    return p.y < 0.0 ? r.y : r.z;
}

float roundedBox(vec2 p, vec2 b, vec4 radius) {
    vec4 r = clamp(radius, 0.0, min(b.x, b.y));
    float cr = cornerRadius(p, r);
    vec2 q = abs(p) - b + vec2(min(cr, min(b.x, b.y)));
    return length(max(q, 0.0)) + min(max(q.x, q.y), 0.0) - min(cr, min(b.x, b.y));
}

void main() {
    vec2 uv = mix(uUvRect.xy, uUvRect.zw, vTexCoord);
    
    
    
    
    vec4 tex = textureLod(uTexture, uv, 0.0);
    if (uForceOpaque > 0.5) tex.a = 1.0;
    if (uTint.a > 0.0) {
        tex = vec4(uTint.rgb, tex.a * uTint.a);
    }

    float mask = 1.0;
    if (max(max(uRadius.x, uRadius.y), max(uRadius.z, uRadius.w)) > 0.0) {
        vec2 pixel = vTexCoord * uSize;
        float dist = roundedBox(pixel - uSize * 0.5, uSize * 0.5, uRadius);
        float softness = max(0.7, uEdgeSoftness);
        mask = 1.0 - smoothstep(-softness, softness, dist);
    }

    float a = tex.a * uOpacity * mask;
    fragColor = vec4(tex.rgb * a, a) * elliceRoundedClipMask();
}
