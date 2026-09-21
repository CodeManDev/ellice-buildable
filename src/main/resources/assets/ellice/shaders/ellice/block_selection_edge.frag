#version 330 core
noperspective in float vDistance;
in vec3 vLocal;
uniform vec4 uEdge,uSecondary;
uniform float uLineWidth,uGlow,uOpacity,uTime;
uniform int uMaterial;
out vec4 fragColor;
void main() {
    float distance=abs(vDistance),halfWidth=uLineWidth*0.5;
    float core=1.0-smoothstep(max(0.0,halfWidth-0.6),halfWidth+0.6,distance);
    float halo=uGlow*0.23*exp(-pow(distance/max(0.5,halfWidth+uGlow*2.4),2.0)*1.6);
    vec3 tint=uEdge.rgb;
    if(uMaterial==2) tint=mix(tint,uSecondary.rgb,(0.5+0.5*sin(dot(vLocal,vec3(1.6,2.4,1.0))-uTime*0.8))*0.55);
    float alpha=max(core,halo)*uEdge.a*uOpacity;
    if(alpha<0.002)discard;
    fragColor=vec4(tint,alpha);
}
