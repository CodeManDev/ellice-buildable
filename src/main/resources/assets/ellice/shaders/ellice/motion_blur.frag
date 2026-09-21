#version 330 core




















in vec2 vTexCoord;

uniform sampler2D uCurrent;
uniform sampler2D uHistory;
uniform float uStrength;     
uniform float uPersistence;  
uniform int   uMode;
uniform int   uFirstFrame;   
                             
                             
                             

out vec4 fragColor;

void main() {
    vec4 curr = texture(uCurrent, vTexCoord);

    if (uFirstFrame == 1) {
        fragColor = vec4(curr.rgb, 1.0);
        return;
    }

    vec4 hist = texture(uHistory, vTexCoord);

    float w = uStrength * uPersistence;
    if (uMode == 1) w *= uPersistence;
    else if (uMode == 2) w *= uPersistence * uPersistence;
    w = clamp(w, 0.0, 0.96);

    vec3 outRGB;
    if (uMode == 3) {
        vec3 c2 = curr.rgb * curr.rgb;
        vec3 h2 = hist.rgb * hist.rgb;
        outRGB = sqrt(mix(c2, h2, w));
    } else {
        outRGB = mix(curr.rgb, hist.rgb, w);
    }

    fragColor = vec4(outRGB, 1.0);
}
