#version 330 core
layout(location=0) in vec3 aStart;
layout(location=1) in vec3 aEnd;
layout(location=2) in vec2 aCorner;
uniform mat4 uView,uProjection;
uniform vec3 uOrigin;
uniform vec2 uViewport;
uniform float uLineWidth,uGlow;
noperspective out float vDistance;
out vec3 vLocal;
void main() {
    vec4 a=uProjection*uView*vec4(uOrigin+aStart,1.0);
    vec4 b=uProjection*uView*vec4(uOrigin+aEnd,1.0);
    float da=a.z+a.w,db=b.z+b.w;
    if(da<=0.0&&db<=0.0) {gl_Position=vec4(2.0,2.0,2.0,1.0);vDistance=0.0;vLocal=aStart;return;}
    
    float start=da<0.0?clamp(-da/(db-da),0.0,1.0):0.0;
    float end=db<0.0?clamp(da/(da-db),0.0,1.0):1.0;
    vec4 ca=mix(a,b,start),cb=mix(a,b,end);
    vec2 delta=(cb.xy/max(cb.w,0.00001)-ca.xy/max(ca.w,0.00001))*uViewport;
    vec2 normal=vec2(-delta.y,delta.x)/max(length(delta),0.0001);
    float spread=uLineWidth*0.5+1.0+uGlow*4.0;
    vec4 position=mix(ca,cb,aCorner.x);
    position.xy+=normal*aCorner.y*spread*2.0/uViewport*position.w;
    
    position.z-=0.00002*position.w;
    gl_Position=position;
    vDistance=aCorner.y*spread;
    vLocal=mix(aStart,aEnd,mix(start,end,aCorner.x));
}
