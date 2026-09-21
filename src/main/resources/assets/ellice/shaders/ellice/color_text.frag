#version 330 core

#include <common/rounded_clip.glsl>

in vec2 vTexCoord;
in vec4 vColor;
in vec2 vCellMin;
in vec2 vCellMax;

uniform sampler2D uAtlas;



uniform vec4 uShadow;       
uniform vec4 uShadowColor;

out vec4 fragColor;

void main() {
    vec4 tex = texture(uAtlas, vTexCoord);
    float alpha = tex.a * vColor.a;
    vec4 result = vec4(tex.rgb * alpha, alpha);

    if (uShadow.z > 0.0) {
        vec2 uvPerPx = fwidth(vTexCoord);
        vec2 shadowUV = clamp(vTexCoord - uShadow.xy * uvPerPx, vCellMin, vCellMax);
        float sa = texture(uAtlas, shadowUV).a * uShadowColor.a;
        vec4 shadow = vec4(uShadowColor.rgb * sa, sa);
        
        result = result + shadow * (1.0 - result.a);
    }

    fragColor = result * elliceRoundedClipMask();
}
