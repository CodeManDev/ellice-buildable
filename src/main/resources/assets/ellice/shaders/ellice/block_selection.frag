#version 330 core
in vec3 vLocal, vRelative;
flat in vec3 vNormal;
uniform vec4 uFill, uSecondary;
uniform float uOpacity, uTime;
uniform int uMaterial;
out vec4 fragColor;
void main() {
    float facing=dot(normalize(-vRelative),vNormal);
    if(facing<=0.0) discard;
    float rim=pow(1.0-facing,2.5);
    float light=0.78+0.22*max(dot(vNormal,normalize(vec3(-0.4,0.9,0.5))),0.0);
    vec3 tint=uFill.rgb;
    float coverage=0.48+rim*0.8;
    if(uMaterial==0) coverage=0.7;
    if(uMaterial==2) {
        float wave=0.5+0.5*sin(dot(vLocal,vec3(1.6,2.4,1.0))-uTime*0.8);
        tint=mix(tint,uSecondary.rgb,wave*0.55);
        coverage+=pow(wave,12.0)*0.28;
    }
    fragColor=vec4(tint*light,uFill.a*coverage*uOpacity);
}
