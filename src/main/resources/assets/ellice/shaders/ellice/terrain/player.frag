#version 330 core
#include <terrain/surface.glsl>
uniform sampler2D uBaseColorMap;
uniform sampler2D uLightmap;
uniform int uHasBaseColorMap;
uniform vec4 uBaseColor;
uniform float uAlphaCutoff;
uniform vec2 uPlayerLight;
uniform vec3 uMapSunDirection;
uniform float uHurtFlash;
in vec3 vWorldPosition;
in vec2 vUv;
in mat3 vTbn;
out vec4 fragColor;
void main() {
    vec4 base = uBaseColor * (uHasBaseColorMap != 0 ? texture(uBaseColorMap, vUv) : vec4(1.0));
    if (base.a < uAlphaCutoff) discard;
    vec3 normal = normalize(vTbn[2]);
    
    float shade = 0.65 + 0.35 * max(dot(normal, uMapSunDirection), 0.0);
    vec3 light = texture(uLightmap, (clamp(uPlayerLight, 0.0, 15.0) + 0.5) / 16.0).rgb;
    vec3 position = vWorldPosition + uCameraOffset;
    vec3 color = mapGrade(base.rgb * light * shade) * mapShadow(position);
    
    color.rgb = mix(color.rgb, vec3(0.85, 0.07, 0.05), clamp(uHurtFlash, 0.0, 1.0) * 0.65);
    fragColor = vec4(mapFog(color, position), base.a);
}
