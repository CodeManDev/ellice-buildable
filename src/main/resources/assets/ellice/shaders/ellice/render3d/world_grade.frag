#version 330 core

#include <render3d/common/math.glsl>
#include <render3d/common/world_looks.glsl>

uniform sampler2D uColorTexture;
uniform sampler2D uDepthTexture;
uniform vec2 uResolution;
uniform mat4 uInvProjection;
uniform float uTime;
uniform int uDepthAvailable;
uniform int uMode;
uniform float uIntensity;
uniform float uExposure;
uniform float uContrast;
uniform float uSaturation;
uniform float uVignette;
uniform float uChromatic;
uniform float uDepthFog;
uniform float uFogDistance;
uniform float uBloom;
uniform float uBloomRadius;
uniform float uBloomThreshold;
uniform float uHalation;
uniform float uGrain;

in vec2 vTexCoord;
out vec4 fragColor;

float hash12(vec2 value) {
    vec3 p3 = fract(vec3(value.xyx) * 0.1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

float viewDistance(vec2 uv, float depth) {
    vec4 clip = vec4(uv * 2.0 - 1.0, depth * 2.0 - 1.0, 1.0);
    vec4 view = uInvProjection * clip;
    return length(view.xyz / max(abs(view.w), 0.00001));
}

vec3 bright(vec2 uv) {
    vec3 value=texture(uColorTexture,clamp(uv,0.0,1.0)).rgb;
    float luma=elliceLuminance(value);
    return value*smoothstep(uBloomThreshold,min(1.001,uBloomThreshold+.22),luma);
}

vec3 highlightGlow(vec2 uv,vec2 radius) {
    
    vec3 sum=bright(uv)*.12;
    for(int i=0;i<8;i++) {
        float angle=float(i)*ELLICE_PI*.25;
        vec2 offset=vec2(cos(angle),sin(angle))*radius;
        sum+=bright(uv+offset)*.075;
        sum+=bright(uv+offset*2.4)*.035;
    }
    return sum;
}

void main() {
    vec2 centered = vTexCoord - 0.5;
    float radial = dot(centered, centered);
    vec2 chromaOffset = centered * max(uChromatic, 0.0) * (0.35 + radial * 2.5);

    vec4 original = texture(uColorTexture, vTexCoord);
    vec3 color;
    color.r = texture(uColorTexture, clamp(vTexCoord + chromaOffset, 0.0, 1.0)).r;
    color.g = original.g;
    color.b = texture(uColorTexture, clamp(vTexCoord - chromaOffset, 0.0, 1.0)).b;

    if (uBloom>.0001 || uHalation>.0001) {
        vec2 radius=vec2(max(uBloomRadius,1.0)*uResolution.y/1080.0)/max(uResolution,vec2(1));
        vec3 glow=highlightGlow(vTexCoord,radius);
        color+=glow*uBloom*.85;
        float rim=max(0.0,elliceLuminance(glow)-elliceLuminance(bright(vTexCoord))*.65);
        color+=vec3(1.0,.24,.075)*rim*uHalation*.65;
    }

    color *= exp2(clamp(uExposure, -3.0, 3.0));
    color = gradePreset(color, uMode);
    color = (color - 0.5) * max(uContrast, 0.0) + 0.5;
    float luma = elliceLuminance(color);
    color = mix(vec3(luma), color, max(uSaturation, 0.0));

    if (uDepthAvailable != 0 && uDepthFog > 0.0001) {
        float rawDepth = texture(uDepthTexture, vTexCoord).r;
        if(rawDepth < .999999) {
        float distanceToPixel = viewDistance(vTexCoord, rawDepth);
        float end=max(uFogDistance,24.0);
        float fog = smoothstep(end*.16,end,distanceToPixel) * elliceSaturate(uDepthFog);
        color = mix(color, worldFogColor(uMode), fog * 0.68);
        }
    }

    float vignetteShape = 1.0 - smoothstep(0.18, 0.82, radial);
    color *= mix(1.0, vignetteShape, elliceSaturate(uVignette));
    float grain = hash12(gl_FragCoord.xy + fract(uTime) * 173.0) - 0.5;
    color += grain * max(uGrain,0.0);

    color = mix(original.rgb, elliceSaturate(color), elliceSaturate(uIntensity));
    fragColor = vec4(color, original.a);
}
