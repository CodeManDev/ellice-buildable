#version 330 core
layout(location = 0) in vec3 aPosition;
layout(location = 1) in vec4 aColor;
layout(location = 2) in vec2 aUv;
layout(location = 3) in vec2 aLight;
uniform mat4 uViewProjection;
uniform vec3 uOrigin;
uniform sampler2D uLightmap;
out vec4 vertexColor;
out vec2 texCoord;
out vec3 vFocusPos;
void main() {
    vec3 focusRelative = aPosition + uOrigin;
    gl_Position = uViewProjection * vec4(focusRelative, 1.0);
    
    
    vec2 lightUv = clamp(aLight / 256.0 + 0.5 / 16.0, vec2(0.5 / 16.0), vec2(15.5 / 16.0));
    vertexColor = (aColor / 255.0) * texture(uLightmap, lightUv);
    texCoord = aUv;
    vFocusPos = focusRelative;
}
