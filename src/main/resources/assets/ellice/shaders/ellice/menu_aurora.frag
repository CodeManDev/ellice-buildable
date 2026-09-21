#version 330 core
#include <common/rounded_clip.glsl>
in vec2 vUV;
out vec4 fragColor;
uniform vec2 uSize;
uniform vec2 uPage;
uniform float uRadius, uOpacity, uTime;
uniform vec3 uColor0, uColor1, uColor2, uColor3, uColor4;
uniform float uLight;

float hash(vec2 p) { return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453); }
float roundedBox(vec2 p, vec2 b, float r) {
    vec2 q = abs(p) - b + r;
    return length(max(q, 0.0)) + min(max(q.x, q.y), 0.0) - r;
}
mat2 rotate2(float a) { float c = cos(a), s = sin(a); return mat2(c, -s, s, c); }
void paper(inout vec3 color, float d, vec3 tint, vec2 p, float aa) {
    
    float shadow = exp(-max(d, 0.0) * 32.0) * smoothstep(-.005, .018, d);
    color *= 1.0 - mix(.19, .055, uLight) * shadow;
    float cover = 1.0 - smoothstep(-aa, aa, d);
    vec3 surface = tint * (.99 + mix(.07, .018, uLight) * clamp(-p.y + p.x * .12, -1.0, 1.0));
    surface += vec3(mix(.022, .009, uLight)) * exp(-abs(d) * 330.0);
    color = mix(color, surface, cover);
}
void main() {
    vec2 uv = vUV;
    float aspect = uSize.x / max(1.0, uSize.y);
    vec2 p = (uv - .5) * vec2(aspect, 1.0);
    float t = uTime * .035;
    vec2 drift = vec2(sin(t), cos(t * .83)) * .018;
    float servers = clamp(uPage.x, 0.0, 1.0), worlds = clamp(uPage.y, 0.0, 1.0);
    float aa = 1.1 / max(1.0, uSize.y);
    vec3 color = mix(uColor0, mix(uColor0, uColor3, .35), uv.y);
    
    
    vec2 a = rotate2(-.34 + servers * .22 - worlds * .15)
        * (p - vec2(-aspect * (.23 - servers * .13 + worlds * .09), -.31 + servers * .12) - drift);
    paper(color, roundedBox(a, vec2(aspect * .48, .32 + servers * .09), .24),
        mix(uColor1, uColor4, servers * .35), a, aa);
    vec2 b = rotate2(.39 - worlds * .31 + servers * .19)
        * (p - vec2(aspect * (.40 - worlds * .21 + servers * .11), .13 - worlds * .08) + drift);
    paper(color, roundedBox(b, vec2(.35 + worlds * .18, .65), .32),
        mix(uColor2, uColor3, worlds * .35), b, aa);
    vec2 c = p - vec2(-aspect * .45 - servers * .17 + worlds * .22, .57 + worlds * .12) + drift * .7;
    paper(color, length(c * vec2(.88, 1.0)) - .60, uColor3, c, aa);
    vec2 e = rotate2(-.28 - servers * .18 + worlds * .16)
        * (p - vec2(aspect * .29 + servers * .19, .69 - servers * .13 + worlds * .07) - drift * .8);
    paper(color, roundedBox(e, vec2(.68, .23), .20), uColor4, e, aa);
    color *= 1.0 - mix(.16, .015, uLight) * smoothstep(.20, .85, length(uv - .5));
    
    color += (hash(gl_FragCoord.xy) - .5) / 510.0;
    float r = min(uRadius, min(uSize.x, uSize.y) * .5);
    float d = roundedBox(uv*uSize - uSize*.5, uSize*.5, r);
    float alpha = (1.0 - smoothstep(-.7, .7, d)) * uOpacity * elliceRoundedClipMask();
    fragColor = vec4(color * alpha, alpha);
}
