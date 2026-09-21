#version 330 core

#include <render3d/common/math.glsl>

struct PointLight {
    vec3 position;
    vec3 color;
    float intensity;
    float radius;
};

uniform vec4 uBaseColor;
uniform vec3 uEmissive;
uniform float uEmissiveStrength;
uniform float uMetallic;
uniform float uRoughness;
uniform float uNormalScale;
uniform float uAlphaCutoff;
uniform int uHasBaseColorMap;
uniform int uHasNormalMap;
uniform sampler2D uBaseColorMap;
uniform sampler2D uNormalMap;

uniform vec3 uSunDirection;
uniform vec3 uSunColor;
uniform float uSunIntensity;
uniform vec3 uAmbientColor;
uniform float uAmbientIntensity;
uniform int uPointLightCount;
uniform PointLight uPointLights[8];

in vec3 vWorldPosition;
in vec2 vUv;
in mat3 vTbn;

out vec4 fragColor;

float distributionGgx(vec3 normal, vec3 halfVector, float roughness) {
    float alpha = roughness * roughness;
    float alpha2 = alpha * alpha;
    float nDotH = max(dot(normal, halfVector), 0.0);
    float denominator = nDotH * nDotH * (alpha2 - 1.0) + 1.0;
    return alpha2 / max(ELLICE_PI * denominator * denominator, 0.000001);
}

float geometrySchlickGgx(float nDotV, float roughness) {
    float r = roughness + 1.0;
    float k = (r * r) / 8.0;
    return nDotV / max(nDotV * (1.0 - k) + k, 0.000001);
}

float geometrySmith(vec3 normal, vec3 viewDirection, vec3 lightDirection, float roughness) {
    return geometrySchlickGgx(max(dot(normal, viewDirection), 0.0), roughness)
        * geometrySchlickGgx(max(dot(normal, lightDirection), 0.0), roughness);
}

vec3 fresnelSchlick(float cosTheta, vec3 f0) {
    return f0 + (1.0 - f0) * pow(1.0 - elliceSaturate(cosTheta), 5.0);
}

vec3 evaluateLight(vec3 normal, vec3 viewDirection, vec3 lightDirection,
                   vec3 radiance, vec3 albedo, float metallic, float roughness) {
    vec3 halfVector = normalize(viewDirection + lightDirection);
    float nDotL = max(dot(normal, lightDirection), 0.0);
    float nDotV = max(dot(normal, viewDirection), 0.0);
    if (nDotL <= 0.0 || nDotV <= 0.0) return vec3(0.0);

    vec3 f0 = mix(vec3(0.04), albedo, metallic);
    vec3 fresnel = fresnelSchlick(max(dot(halfVector, viewDirection), 0.0), f0);
    float distribution = distributionGgx(normal, halfVector, roughness);
    float geometry = geometrySmith(normal, viewDirection, lightDirection, roughness);
    vec3 specular = distribution * geometry * fresnel
        / max(4.0 * nDotV * nDotL, 0.0001);

    vec3 diffuseWeight = (vec3(1.0) - fresnel) * (1.0 - metallic);
    vec3 diffuse = diffuseWeight * albedo / ELLICE_PI;
    return (diffuse + specular) * radiance * nDotL;
}

void main() {
    vec4 sampledBase = uHasBaseColorMap != 0 ? texture(uBaseColorMap, vUv) : vec4(1.0);
    vec4 base = uBaseColor * sampledBase;
    if (base.a < uAlphaCutoff) discard;

    vec3 normal = normalize(vTbn[2]);
    if (uHasNormalMap != 0) {
        vec3 mapped = texture(uNormalMap, vUv).xyz * 2.0 - 1.0;
        mapped.xy *= uNormalScale;
        normal = normalize(vTbn * mapped);
    }

    vec3 albedo = pow(max(base.rgb, vec3(0.0)), vec3(2.2));
    float metallic = elliceSaturate(uMetallic);
    float roughness = clamp(uRoughness, 0.045, 1.0);
    vec3 viewDirection = normalize(-vWorldPosition);

    vec3 color = evaluateLight(
        normal, viewDirection, normalize(uSunDirection),
        uSunColor * uSunIntensity, albedo, metallic, roughness);

    int lightCount = clamp(uPointLightCount, 0, 8);
    for (int i = 0; i < lightCount; i++) {
        vec3 toLight = uPointLights[i].position - vWorldPosition;
        float distanceToLight = length(toLight);
        vec3 lightDirection = distanceToLight > 0.00001
            ? toLight / distanceToLight
            : normal;
        float radius = max(uPointLights[i].radius, 0.0001);
        float falloff = elliceSaturate(1.0 - distanceToLight / radius);
        falloff *= falloff;
        vec3 radiance = uPointLights[i].color * uPointLights[i].intensity * falloff;
        color += evaluateLight(normal, viewDirection, lightDirection,
            radiance, albedo, metallic, roughness);
    }

    vec3 f0 = mix(vec3(0.04), albedo, metallic);
    float fresnel = pow(1.0 - max(dot(normal, viewDirection), 0.0), 5.0);
    color += albedo * uAmbientColor * uAmbientIntensity * (1.0 - metallic);
    color += f0 * fresnel * uAmbientColor * uAmbientIntensity;
    color += uEmissive * max(uEmissiveStrength, 0.0);

    color = elliceAces(color);
    color = pow(color, vec3(1.0 / 2.2));
    fragColor = vec4(color, base.a);
}
