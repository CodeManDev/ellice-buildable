#pragma once

const float ELLICE_PI = 3.14159265358979323846;

float elliceSaturate(float value) {
    return clamp(value, 0.0, 1.0);
}

vec3 elliceSaturate(vec3 value) {
    return clamp(value, vec3(0.0), vec3(1.0));
}

float elliceLuminance(vec3 color) {
    return dot(color, vec3(0.2126, 0.7152, 0.0722));
}

vec3 elliceAces(vec3 color) {
    const float a = 2.51;
    const float b = 0.03;
    const float c = 2.43;
    const float d = 0.59;
    const float e = 0.14;
    return elliceSaturate((color * (a * color + b)) / (color * (c * color + d) + e));
}
