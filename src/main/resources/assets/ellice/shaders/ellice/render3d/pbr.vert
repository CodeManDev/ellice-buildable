#version 330 core

layout(location = 0) in vec3 aPosition;
layout(location = 1) in vec3 aNormal;
layout(location = 2) in vec2 aUv;
layout(location = 3) in vec4 aTangent;

invariant gl_Position;

uniform mat4 uModel;
uniform mat4 uNormalMatrix;
uniform mat4 uView;
uniform mat4 uProjection;

out vec3 vWorldPosition;
out vec2 vUv;
out mat3 vTbn;

void main() {
    vec4 world = uModel * vec4(aPosition, 1.0);
    mat3 modelLinear = mat3(uModel);
    mat3 normalMatrix = mat3(uNormalMatrix);
    vec3 normal = normalize(normalMatrix * aNormal);
    
    
    vec3 tangent = normalize(modelLinear * aTangent.xyz);
    tangent = normalize(tangent - normal * dot(normal, tangent));
    float modelHandedness = determinant(modelLinear) < 0.0 ? -1.0 : 1.0;
    vec3 bitangent = normalize(cross(normal, tangent))
        * aTangent.w * modelHandedness;

    vWorldPosition = world.xyz;
    vUv = aUv;
    vTbn = mat3(tangent, bitangent, normal);
    vec4 viewPosition = uView * world;
    gl_Position = uProjection * viewPosition;
}
