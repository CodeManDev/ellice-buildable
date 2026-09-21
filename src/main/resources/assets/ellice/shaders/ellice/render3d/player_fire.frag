#version 330 core

flat in float vSeed;
noperspective in vec2 vScreenMotion;
out vec4 fragColor;

void main() {
    
    
    const float MAX_SCREEN_FLOW = 320.0;
    
    
    vec2 encodedMotion = (clamp(vScreenMotion / MAX_SCREEN_FLOW,
        vec2(-1.0), vec2(1.0)) * 127.0 + 128.0) / 255.0;
    fragColor = vec4(1.0, clamp(vSeed, 0.0, 1.0), encodedMotion);
}
