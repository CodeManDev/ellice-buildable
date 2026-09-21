#version 330 core

#include <common/rounded_clip.glsl>

in float vPathDist;

uniform vec4 uColor;         
uniform float uDashLength;   
uniform float uDashOffset;   
uniform float uDashGap;      
uniform int uEdgeCoverage;   
uniform int uVertexAlpha;    

out vec4 fragColor;

void main() {
    
    if (uDashLength > 0.0) {
        float d = mod(vPathDist + uDashOffset, uDashLength);
        if (d > uDashLength * (1.0 - uDashGap)) discard;
    }

    float coverage = 1.0;
    if (uEdgeCoverage != 0) {
        float feather = max(0.0001, fwidth(vPathDist));
        coverage = 1.0 - smoothstep(1.0 - feather, 1.0, abs(vPathDist));
    }
    if (uVertexAlpha != 0) coverage *= clamp(vPathDist, 0.0, 1.0);
    fragColor = uColor * coverage * elliceRoundedClipMask();
}
