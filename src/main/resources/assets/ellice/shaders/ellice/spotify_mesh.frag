#version 330 core

in vec2 vUV;

uniform float uTime;
uniform vec2 uSize;
uniform float uRadius;
uniform float uOpacity;
uniform float uBlur;
uniform vec3 uColor0, uColor1, uColor2, uColor3, uColor4;

out vec4 fragColor;

float roundedBox(vec2 p, vec2 b, float r) {
    vec2 q = abs(p) - b + vec2(min(r, min(b.x, b.y)));
    return length(max(q, 0.0)) + min(max(q.x, q.y), 0.0) - min(r, min(b.x, b.y));
}

void main() {
    vec2 pixel = vUV * uSize;
    vec2 center = uSize * 0.5;

    
    float outerDist = roundedBox(pixel - center, center, uRadius);
    float mask = 1.0 - smoothstep(-0.7, 0.7, outerDist);

    
    float bw = 2.5;
    float innerDist = roundedBox(pixel - center, center - vec2(bw), max(uRadius - bw, 0.0));
    float borderGlow = (1.0 - smoothstep(-0.5, bw * 2.0, innerDist)) * mask;

    float t = uTime;

    
    vec2 p0 = vec2(0.20 + 0.15*sin(t*0.21),       0.30 + 0.20*cos(t*0.27 + 1.0));
    vec2 p1 = vec2(0.80 + 0.12*cos(t*0.18 + 2.0), 0.25 + 0.15*sin(t*0.23 + 0.5));
    vec2 p2 = vec2(0.50 + 0.25*sin(t*0.40 + 1.5), 0.65 + 0.25*cos(t*0.45 + 2.5));
    vec2 p3 = vec2(0.10 + 0.12*cos(t*0.22 + 3.0), 0.70 + 0.15*sin(t*0.28 + 1.2));
    vec2 p4 = vec2(0.85 + 0.10*sin(t*0.30 + 0.8), 0.50 + 0.20*cos(t*0.26 + 3.5));

    float s2 = mix(0.10, 0.70, uBlur);
    float w0 = exp(-dot(vUV - p0, vUV - p0) / s2);
    float w1 = exp(-dot(vUV - p1, vUV - p1) / s2);
    float w2 = exp(-dot(vUV - p2, vUV - p2) / s2);
    float w3 = exp(-dot(vUV - p3, vUV - p3) / s2);
    float w4 = exp(-dot(vUV - p4, vUV - p4) / s2);

    float pw = mix(2.0, 1.0, uBlur);
    w0 = pow(w0, pw); w1 = pow(w1, pw); w2 = pow(w2, pw);
    w3 = pow(w3, pw); w4 = pow(w4, pw);

    float wSum = w0 + w1 + w2 + w3 + w4 + 0.0001;
    vec3 rawColor = (uColor0*w0 + uColor1*w1 + uColor2*w2 + uColor3*w3 + uColor4*w4) / wSum;

    
    float lum = dot(rawColor, vec3(0.299, 0.587, 0.114));
    rawColor = mix(vec3(lum), rawColor, 1.5);

    
    vec3 fillColor = rawColor * 0.50;
    float vig = 1.0 - 0.25 * dot(vUV - 0.5, vUV - 0.5) * 4.0;
    fillColor *= max(vig, 0.0);

    
    vec3 borderColor = rawColor * 1.6;

    
    vec3 color = mix(fillColor, borderColor, borderGlow);

    
    float grain = fract(sin(dot(vUV * 300.0 + t * 0.1, vec2(12.9898, 78.233))) * 43758.5453);
    color += (grain - 0.5) * 0.01;

    float alpha = mask * uOpacity;
    fragColor = vec4(color * alpha, alpha);
}
