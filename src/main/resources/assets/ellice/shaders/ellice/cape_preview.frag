#version 330 core
uniform sampler2D uTexture;
in vec2 vUv;
in vec3 vNormal;
out vec4 fragColor;
void main(){
    vec4 pixel=texture(uTexture,vUv);
    if(pixel.a<0.05)discard;
    vec3 n=normalize(vNormal);
    float light=0.62+0.28*max(dot(n,normalize(vec3(-0.5,0.9,0.7))),0.0)+0.10*max(dot(n,normalize(vec3(0.8,0.2,-0.6))),0.0);
    fragColor=vec4(pixel.rgb*light,pixel.a);
}
