#pragma once


vec3 worldFogColor(int mode) {
    if (mode == 1) return vec3(.10,.12,.27);
    if (mode == 2) return vec3(.48,.40,.53);
    if (mode == 3) return vec3(.23);
    if (mode == 4) return vec3(.70,.44,.25);
    if (mode == 5) return vec3(.65,.43,.53);
    if (mode == 6) return vec3(.29,.40,.44);
    if (mode == 7) return vec3(.29,.44,.34);
    if (mode == 8) return vec3(.13,.19,.34);
    if (mode == 9) return vec3(.38,.22,.51);
    if (mode == 10) return vec3(.48,.35,.24);
    if (mode == 11) return vec3(.48,.64,.70);
    return vec3(.12,.15,.19);
}

vec3 gradePreset(vec3 color, int mode) {
    float luma = elliceLuminance(color);
    float highlights = smoothstep(.18,.82,luma);
    if (mode == 1) return color*vec3(.90,1.08,1.18)+vec3(.10,.015,.16)*(1.0-luma)
        +vec3(.03,.08,.12)*smoothstep(.55,1.0,luma);
    if (mode == 2) {
        vec3 lifted=mix(vec3(.055,.045,.075),color,.90);
        return mix(lifted,sqrt(max(lifted,vec3(0))),.16);
    }
    if (mode == 3) return vec3(smoothstep(.02,.96,luma));
    if (mode == 4) return color*mix(vec3(1.02,.97,.91),vec3(1.18,1.04,.85),highlights)+vec3(.018,.009,0);
    if (mode == 5) return mix(color,vec3(luma),.12)*mix(vec3(1.02,.95,1.10),vec3(1.09,.99,1.02),highlights)+vec3(.025,.012,.025);
    if (mode == 6) return mix(color,vec3(luma),.16)*mix(vec3(.90,1.02,1.10),vec3(.98,1.04,1.02),highlights);
    if (mode == 7) return color*mix(vec3(.90,1.08,1.01),vec3(1.05,1.08,.90),highlights)+vec3(.006,.012,.005);
    if (mode == 8) return color*mix(vec3(.77,.86,1.18),vec3(.94,1.01,1.16),highlights)+vec3(.005,.008,.020);
    if (mode == 9) return color*mix(vec3(1.10,.86,1.23),vec3(.95,1.08,1.14),highlights)+vec3(.025,.008,.045);
    if (mode == 10) return mix(color,vec3(luma),.18)*mix(vec3(1.04,.99,.90),vec3(1.14,1.02,.87),highlights)*.94+vec3(.035,.028,.019);
    if (mode == 11) return color*mix(vec3(.86,1.02,1.12),vec3(.99,1.05,1.10),highlights)+vec3(.010,.018,.022);
    return color*mix(vec3(.94,1.015,1.055),vec3(1.055,1.01,.94),highlights);
}
