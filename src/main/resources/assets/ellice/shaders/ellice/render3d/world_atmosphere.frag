#version 330 core

#include <render3d/common/math.glsl>
#include <render3d/common/world_looks.glsl>

uniform sampler2D uColorTexture;
uniform sampler2D uDepthTexture;
uniform mat4 uInvProjection;
uniform mat4 uInvView;
uniform vec3 uCameraPosition;
uniform vec3 uCameraWrapped;
uniform float uTime;
uniform int uDepthAvailable;
uniform int uMode;
uniform float uIntensity;
uniform int uSkyMode;
uniform float uSkyStrength;
uniform float uStars;
uniform float uHeightMist;
uniform float uDepthFog;
uniform float uFogDistance;
uniform float uMistHeight;
uniform float uMotes;
uniform vec4 uMoteColor;
uniform float uAtmosphereSpeed;
in vec2 vTexCoord;
out vec4 fragColor;

float hash31(vec3 p) {
    p=fract(p*.1031);p+=dot(p,p.yzx+33.33);
    return fract((p.x+p.y)*p.z);
}
float worldNoise(vec3 p) {
    vec3 i=floor(p),f=fract(p);f=f*f*(3.0-2.0*f);
    return mix(mix(mix(hash31(i),hash31(i+vec3(1,0,0)),f.x),mix(hash31(i+vec3(0,1,0)),hash31(i+vec3(1,1,0)),f.x),f.y),
        mix(mix(hash31(i+vec3(0,0,1)),hash31(i+vec3(1,0,1)),f.x),mix(hash31(i+vec3(0,1,1)),hash31(i+vec3(1,1,1)),f.x),f.y),f.z);
}
float cloud(vec3 p) {
    float sum=0.0,weight=.55;
    for(int i=0;i<4;i++){sum+=worldNoise(p)*weight;p=p*2.03+vec3(4.1,1.3,7.7);weight*=.48;}
    return sum;
}
vec3 skyColor(vec3 ray,float time) {
    float horizon=exp(-max(ray.y,0.0)*4.5);
    if(uSkyMode==1) {
        
        float azimuth=atan(ray.z,ray.x),altitude=asin(clamp(ray.y,-1.0,1.0));
        float ribbon=.30+.11*sin(azimuth*3.0+time*.16)+.05*sin(azimuth*7.0-time*.11);
        float delta=altitude-ribbon;
        float curtain=exp(-abs(delta)*12.0)+.35*exp(-abs(altitude-ribbon-.24)*9.0);
        float folds=.30+.70*worldNoise(vec3(ray.x*80.0,ray.z*80.0,ray.y*2.0+time*.12));
        vec3 lights=mix(vec3(.12,.84,.49),vec3(.49,.19,.77),smoothstep(-.08,.24,delta));
        return vec3(.018,.028,.067)+vec3(.035,.065,.085)*horizon+lights*curtain*folds*smoothstep(-.02,.10,ray.y);
    }
    if(uSkyMode==2) {
        float clouds=cloud(ray*3.2+vec3(time*.023,0,time*.015));
        float band=exp(-pow((ray.y+ray.x*.35-.3)*2.5,2.0));
        vec3 tint=mix(vec3(.11,.40,.51),vec3(.55,.19,.47),cloud(ray*5.0+7.0));
        return vec3(.025,.025,.065)+tint*smoothstep(.22,.78,clouds)*band;
    }
    vec3 upper=mix(vec3(.17,.24,.42),worldFogColor(uMode)*.7,.25);
    vec3 sunset=mix(vec3(.98,.57,.35),worldFogColor(uMode),.3);
    float sun=pow(max(dot(ray,normalize(vec3(-.65,.18,-.4))),0.0),90.0);
    float wisps=cloud(ray*5.0+vec3(time*.025,0,0));
    return mix(upper,sunset,horizon)+vec3(.20,.10,.035)*sun+vec3(.10,.035,.06)*(wisps-.4)*horizon;
}
vec3 starField(vec3 ray,float time) {
    vec3 p=ray*210.0,i=floor(p),f=fract(p)-.5;
    float seed=hash31(i),spark=exp(-dot(f,f)*85.0)*step(.973,seed);
    float twinkle=.82+.18*sin(time*.7+seed*90.0);
    return mix(vec3(.66,.79,1.0),vec3(1.0,.85,.64),seed)*spark*twinkle*smoothstep(-.05,.2,ray.y);
}
vec3 lightMotes(vec3 ray,float sceneDistance,float time) {
    vec3 cameraCell=floor(uCameraWrapped/8.0),local=mod(uCameraWrapped,8.0),light=vec3(0);
    
    for(int x=-1;x<=1;x++)for(int y=-1;y<=1;y++)for(int z=-1;z<=1;z++) {
        vec3 offset=vec3(x,y,z),cell=mod(cameraCell+offset,512.0);
        vec3 cellCenter=(offset+.5)*8.0-local;
        float cellAlong=dot(cellCenter,ray);
        if(cellAlong < -7.0 || cellAlong > min(sceneDistance,7.5)+7.0)continue;
        if(dot(cellCenter,cellCenter)-pow(max(cellAlong,0.0),2.0)>49.0)continue;
        for(int particle=0;particle<4;particle++) {
        vec3 key=cell+float(particle)*vec3(13.13,27.27,41.41);
        float seed=hash31(key);
        vec3 jitter=vec3(seed,hash31(key+17.1),hash31(key+43.7));
        vec3 center=(offset+jitter*.65+.175)*8.0-local;
        center+=vec3(sin(time*.65+seed*18.0),sin(time*.43+seed*40.0),cos(time*.55+seed*25.0))*.45;
        float along=dot(center,ray),distance=length(center);
        if(along<=.15 || along>=sceneDistance || distance>=7.5)continue;
        float radius=mix(.025,.055,seed),d2=max(0.0,dot(center,center)-along*along);
        float glow=exp(-d2/(radius*radius))*1.2+exp(-d2/(radius*radius*14.0))*.14;
        float fade=smoothstep(.6,1.6,distance)*(1.0-smoothstep(5.5,7.5,distance));
        
        fade*=smoothstep(0.0,.2,sceneDistance-along);
        light+=uMoteColor.rgb*glow*fade*(.65+.35*sin(time*.8+seed*25.0));
        }
    }
    return light*uMotes*uMoteColor.a;
}
void main() {
    vec4 original=texture(uColorTexture,vTexCoord);
    
    if(uDepthAvailable==0 || uIntensity<=.0001){fragColor=original;return;}
    float depth=texture(uDepthTexture,vTexCoord).r;
    vec4 view=uInvProjection*vec4(vTexCoord*2.0-1.0,depth*2.0-1.0,1.0);
    vec3 position=view.xyz/max(abs(view.w),.00001);
    vec3 ray=normalize(mat3(uInvView)*position);
    float distance=depth>=.999999?10000.0:length(position);
    float time=uTime*uAtmosphereSpeed;
    vec3 color=original.rgb;
    if(depth>=.999999) {
        if(uSkyMode>0 && uSkyStrength>.0001)color=mix(color,skyColor(ray,time),uSkyStrength);
        if(uStars>.0001)color+=starField(ray,time)*uStars;
    } else {
        if(uDepthFog>.0001) {
            float end=max(uFogDistance,24.0);
            color=mix(color,worldFogColor(uMode),smoothstep(end*.16,end,distance)*uDepthFog*.68);
        }
        if(uHeightMist>.0001) {
        float middleY=uCameraPosition.y+ray.y*distance*.5;
        float density=exp(-abs(middleY-uMistHeight)/18.0);
        vec3 anchor=uCameraWrapped+ray*min(distance,256.0)*.5;
        density*=.75+.25*worldNoise(anchor*.07+vec3(time*.045,0,0));
        float amount=(1.0-exp(-distance*.014))*density*uHeightMist;
        color=mix(color,worldFogColor(uMode),amount*.8);
        }
    }
    if(uMotes>.0001)color+=lightMotes(ray,distance,time);
    fragColor=vec4(mix(original.rgb,elliceSaturate(color),uIntensity),original.a);
}
