#version 330 core

layout(location = 0) in vec3 aPosition;
layout(location = 1) in float aSeed;
layout(location = 2) in vec2 aScreenMotion;

invariant gl_Position;

uniform mat4 uView;
uniform mat4 uProjection;

flat out float vSeed;
noperspective out vec2 vScreenMotion;

void main() {
    
    
    vec4 viewPosition = uView * vec4(aPosition, 1.0);
    gl_Position = uProjection * viewPosition;
    vSeed = aSeed;
    vScreenMotion = aScreenMotion;
}
