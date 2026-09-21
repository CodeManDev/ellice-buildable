#version 330 core
#include <common/rounded_clip.glsl>
in vec2 vUV;
out vec4 fragColor;
uniform vec2 uSize, uAlpha;
uniform float uRadius, uOpacity, uTime, uDensity;
uniform int uKind;
uniform vec4 uParams;
uniform vec3 uColor0, uColor1;

float box(vec2 p,vec2 b,float r) {
    r=min(r,min(b.x,b.y));vec2 q=abs(p)-b+r;
    return length(max(q,0.0))+min(max(q.x,q.y),0.0)-r;
}
float hash(vec2 p){return fract(sin(dot(p,vec2(127.1,311.7)))*43758.5453);}
float noise(vec2 p){vec2 i=floor(p),f=fract(p);f=f*f*(3.0-2.0*f);
    return mix(mix(hash(i),hash(i+vec2(1,0)),f.x),mix(hash(i+vec2(0,1)),hash(i+vec2(1)),f.x),f.y);}
float clouds(vec2 p){return noise(p)*.57+noise(p*2.1)*.27+noise(p*4.1)*.16;}

void main() {
    vec2 p=vUV*uSize, halfSize=uSize*.5;
    float aa=max(.65,fwidth(p.x)*.7);
    if(uKind==0) {
        vec2 uv=vUV;
        vec3 sky=mix(vec3(.035,.065,.105),vec3(.145,.120,.210),smoothstep(.05,1.0,uv.y));
        float horizon=exp(-pow((uv.y-.90)*5.0,2.0));
        sky+=horizon*vec3(.046,.035,.035);
        float cloud=clouds(vec2(uv.x*3.6+uTime*.012,uv.y*7.0));
        sky+=smoothstep(.52,.79,cloud)*.046*smoothstep(.22,.98,uv.y)*uParams.w;
        float moon=length((uv-vec2(.80,.22))*vec2(uSize.x/uSize.y,1));
        sky+=vec3(.45,.43,.54)*(1.0-smoothstep(.022,.0235,moon))*.30*uParams.w;
        sky+=vec3(.06,.07,.10)*exp(-moon*19.0)*uParams.w;
        vec2 world=(p/uDensity-uParams.xy)/max(.1,uParams.z);
        vec2 grid=abs(mod(world+12.0,24.0)-12.0)*max(.1,uParams.z)*uDensity;
        float dots=1.0-smoothstep(.65*uDensity,1.15*uDensity,length(grid));
        sky+=vec3(.065,.074,.094)*dots;
        sky+=(hash(p)-.5)/255.0;
        fragColor=vec4(sky*uOpacity,uOpacity)*elliceRoundedClipMask();return;
    }
    vec2 q=p-halfSize;
    float angle=radians(uParams.w), co=cos(angle),si=sin(angle);
    q=mat2(co,-si,si,co)*q;
    float distance=box(q,halfSize,uRadius);
    if(uKind==2 || uKind==3) {
        vec2 radii=max(vec2(1),halfSize-vec2(1));
        distance=(length(q/radii)-1.0)*min(radii.x,radii.y);
        if(uKind==3)distance=abs(distance+max(1.0,uParams.y*uDensity)*.5)-max(1.0,uParams.y*uDensity)*.5;
    }
    if(uKind==4)distance=(abs(q.x)/halfSize.x+abs(q.y)/halfSize.y-1.0)*min(halfSize.x,halfSize.y)*.707;
    if(uKind==5) {
        float unit=uDensity*max(.1,uParams.x);
        float body=box(q+vec2(0,3.0*unit),halfSize-vec2(0,3.0*unit),uRadius);
        float tab=box(p-vec2(34.0*unit,uSize.y-6.0*unit),vec2(12.0,6.0)*unit,3.0*unit);
        float notch=box(p-vec2(34.0*unit,0),vec2(13.0,5.0)*unit,3.0*unit);
        distance=max(min(body,tab),-notch);
    }
    float blend=clamp(vUV.x*.65+vUV.y*.35,0.0,1.0);
    if(uParams.z>1.5)blend=clamp(.5+.3*sin(vUV.x*4.0+uTime*.35)+.18*cos(vUV.y*5.0-uTime*.22),0.0,1.0);
    if(uParams.z<.5)blend=0.0;
    vec3 color=mix(uColor0,uColor1,blend);
    float alpha=mix(uAlpha.x,uAlpha.y,blend);
    if(uKind==3) {
        float phase=fract(atan(q.x,-q.y)/6.2831853+1.0);
        float progressMask=1.0-smoothstep(uParams.x-.002,uParams.x+.002,phase);
        if(uParams.x>=.999)progressMask=1.0;if(uParams.x<=.001)progressMask=0.0;
        color=mix(color*.21,color,progressMask);
    }
    if(uKind==6)color=mix(color*.20,color,1.0-smoothstep(uParams.x-.002,uParams.x+.002,vUV.x));
    if(uKind==5) {
        float rim=1.0-smoothstep(.6,1.7,abs(distance+1.0));
        color=mix(color,uColor1,rim*mix(.22,.76,uParams.y));
        color+=(1.0-smoothstep(0.0,.38,vUV.y))*.018;
    }
    float coverage=1.0-smoothstep(-aa,aa,distance);
    alpha*=coverage*uOpacity;
    fragColor=vec4(color*alpha,alpha)*elliceRoundedClipMask();
}
