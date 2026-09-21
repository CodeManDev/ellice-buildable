#version 330 core
uniform sampler2D uAtlas;
uniform float uAlphaCutoff;
uniform vec2 uAtlasSize;
uniform int uModernSampling;
#include <terrain/surface.glsl>
uniform float uTime;
uniform float uSparkle;
uniform vec3 uSunDirection;
in vec4 vertexColor;
in vec2 texCoord;
in vec3 vFocusPos;
out vec4 fragColor;

float hash21(vec2 value) {
    vec3 p3 = fract(vec3(value.xyx) * 0.1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

void main() {
    vec4 surface;
    if (uModernSampling == 1) {
        
        vec2 pixelSize = 1.0 / uAtlasSize;
        vec2 du = dFdx(texCoord), dv = dFdy(texCoord);
        vec2 screenSize = max(sqrt(du * du + dv * dv), vec2(1e-10));
        vec2 texel = texCoord / pixelSize;
        vec2 center = round(texel) - 0.5;
        vec2 offset = clamp((texel - center - 0.5) * pixelSize / screenSize + 0.5, 0.0, 1.0);
        surface = textureGrad(uAtlas, (center + offset) * pixelSize, du, dv);
    } else {
        surface = texture(uAtlas, texCoord);
    }
    vec4 color = surface * vertexColor;
    if (color.a < uAlphaCutoff) discard;
    color.rgb = mapGrade(color.rgb) * mapShadow(vFocusPos);
    
    
    
    
    vec3 flatNormal = normalize(cross(dFdx(vFocusPos), dFdy(vFocusPos)));
    if (!gl_FrontFacing) flatNormal = -flatNormal;
    float bounceStrength = clamp(1.0 - normalize(uSunDirection).y * 1.6, 0.0, 1.0) * 0.30;
    vec3 hemi = mix(uFogColor * vec3(0.42, 0.36, 0.32), uFogColor, flatNormal.y * 0.5 + 0.5);
    color.rgb *= 1.0 + hemi * bounceStrength;
    if (uSparkle > 0.5) {
        
        vec2 cell = floor(texCoord * uAtlasSize * 0.25) + floor(uTime * 1.5) * 0.37;
        color.rgb += vec3(0.10, 0.11, 0.12) * step(0.985, hash21(cell));
        
        
        vec3 viewDir = normalize(uCameraOffset - vFocusPos);
        vec3 halfVec = normalize(viewDir + normalize(uSunDirection));
        float glitter = pow(max(dot(vec3(0.0, 1.0, 0.0), halfVec), 0.0), 120.0);
        float lowLight = clamp(1.0 - normalize(uSunDirection).y * 1.6, 0.0, 1.0);
        color.rgb += vec3(1.0, 0.85, 0.6) * glitter * (0.15 + 0.85 * lowLight);
        
        
        float fresnel = pow(1.0 - clamp(viewDir.y, 0.0, 1.0), 3.0);
        color.rgb = mix(color.rgb, uFogColor * 1.05 + vec3(0.06, 0.025, 0.0), fresnel * 0.38);
    }
    color.rgb = mapFog(color.rgb, vFocusPos);
    fragColor = color;
}
