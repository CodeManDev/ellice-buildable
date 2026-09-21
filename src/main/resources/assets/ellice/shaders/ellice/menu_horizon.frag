#version 330 core
#include <common/rounded_clip.glsl>
in vec2 vUV;
out vec4 fragColor;
uniform vec2 uSize, uPage;
uniform float uRadius, uOpacity, uTime, uLight, uFocus, uLayoutWidth;
uniform vec3 uColor0, uColor1, uColor2, uColor3, uColor4;



mat2 turn(float a) { float c=cos(a),s=sin(a); return mat2(c,-s,s,c); }
float box(vec3 p,vec3 b,float r) {
    vec3 q=abs(p)-b;
    return length(max(q,0.0))+min(max(q.x,max(q.y,q.z)),0.0)-r;
}
vec2 nearer(vec2 a,vec2 b) { return a.x<b.x?a:b; }
vec2 sculpture(vec3 p) {
    float t=uTime*.16;
    p.xz=turn(-.24+uPage.x*.34-uPage.y*.26+sin(t*.4)*.035+uFocus*.10)*p.xz;
    p.y-=sin(t*.8)*.045+uFocus*.07;
    vec3 q=p-vec3(0.0,.22,0.0);
    q.xy=turn(-.13+uPage.y*.19)*q.xy;
    
    float outer=box(q,vec3(.92,1.13,.12),.24);
    float inner=box(q,vec3(.62,.83,.65),.22);
    vec2 d=vec2(max(outer,-inner),1.0);
    d=nearer(d,vec2(box(q+vec3(0,0,.15),vec3(.64,.85,.015),.19),5.0));
    vec3 a=p-vec3(-.05,-1.22,.12);
    a.xz=turn(.1)*a.xz;
    d=nearer(d,vec2(box(a,vec3(1.06,.075,.56),.10),2.0));
    vec3 b=p-vec3(.15,-1.53,.33);
    b.xz=turn(-.12-uPage.x*.12)*b.xz;
    d=nearer(d,vec2(box(b,vec3(.82,.055,.48),.095),3.0));
    vec3 c=p-vec3(-.16,-1.82,.58);
    c.xz=turn(.08+uPage.y*.2)*c.xz;
    d=nearer(d,vec2(box(c,vec3(.55,.04,.36),.08),2.0));
    vec3 orb=p-vec3(1.3+sin(t*.65)*.10,.90+cos(t*.6)*.10,.28);
    d=nearer(d,vec2(length(orb)-.23,3.0));
    vec3 stone=p-vec3(-1.25,-.63+sin(t*.7)*.11,.38);
    stone.xy=turn(.3+t*.10)*stone.xy;stone.xz=turn(.4)*stone.xz;
    d=nearer(d,vec2(box(stone,vec3(.13),.085),4.0));
    return d;
}
vec3 normalAt(vec3 p) {
    vec2 e=vec2(.0015,0);
    return normalize(vec3(sculpture(p+e.xyy).x-sculpture(p-e.xyy).x,
        sculpture(p+e.yxy).x-sculpture(p-e.yxy).x,
        sculpture(p+e.yyx).x-sculpture(p-e.yyx).x));
}
float roundedBox(vec2 p,vec2 b,float r) {
    vec2 q=abs(p)-b+r;
    return length(max(q,0.0))+min(max(q.x,q.y),0.0)-r;
}
void main() {
    vec2 uv=vUV;
    float aspect=uSize.x/max(1.0,uSize.y);
    vec2 p=(uv-.5)*vec2(aspect,1.0);
    float page=max(uPage.x,uPage.y);
    
    vec2 center=vec2(aspect*mix(.245,.36,page),-.04+page*.07);
    float halo=exp(-dot((p-center)*vec2(.85,1.1),(p-center)*vec2(.85,1.1))*4.5);
    vec3 base=mix(uColor0,uColor1,mix(.07,.035,uLight)*halo);
    base=mix(base,uColor3,halo*mix(.13,.06,uLight));
    float horizon=exp(-pow((uv.y-.76)*11.0,2.0));
    base=mix(base,uColor2,horizon*halo*mix(.055,.025,uLight));
    float shadow=exp(-pow((p.x-center.x)*2.6,2.0)-pow((p.y-.31)*23.0,2.0));
    base*=1.0-shadow*mix(.21,.08,uLight)*(1.0-page);
    vec2 screen=(p-center)*4.8;
    
    if(page<.995 && uLayoutWidth>=760.0 && abs(screen.x)<2.8 && abs(screen.y)<3.0) {
        vec3 ro=vec3(3.4,1.7,6.0);
        vec3 target=vec3(0,-.10,0);
        vec3 forward=normalize(target-ro);
        vec3 right=normalize(cross(forward,vec3(0,1,0)));
        vec3 up=cross(right,forward);
        vec3 rd=normalize(forward*4.8+right*screen.x-up*screen.y);
        float travel=3.4;vec2 hit=vec2(1,0);
        bool found=false;
        for(int i=0;i<60;i++) {
            hit=sculpture(ro+rd*travel);
            if(hit.x<.0018){found=true;break;}
            travel+=max(.001,hit.x*.82);
            if(travel>10.5)break;
        }
        if(found) {
            vec3 pos=ro+rd*travel,n=normalAt(pos),light=normalize(vec3(-3,5,4));
            float diffuse=max(0.0,dot(n,light));
            float ambient=clamp(sculpture(pos+n*.13).x/.13,.30,1.0);
            ambient*=clamp(sculpture(pos+n*.32).x/.32,.55,1.0);
            vec3 porcelain=mix(uColor1,vec3(1),mix(.12,.38,uLight));
            vec3 paint=hit.y<1.5?porcelain:hit.y<2.5?mix(uColor1,uColor2,.22):hit.y<3.5?uColor2:uColor3;
            vec3 color=paint*(.42+.58*diffuse)*mix(.75,1.0,ambient);
            if(hit.y>4.5) {
                
                float fold=.5+.5*sin(pos.y*5.0+sin(pos.x*2.4+uTime*.12)*.9+uTime*.08);
                color=mix(uColor2,uColor3,smoothstep(.12,.88,fold));
                color=mix(color,uColor1,.22+.12*sin(pos.x*2.0-pos.y*3.0));
                color*=.83+.17*diffuse;
            }
            float spec=pow(max(0.0,dot(n,normalize(light-rd))),42.0);
            float rim=pow(1.0-max(0.0,dot(n,-rd)),3.0);
            color+=vec3(1.0,.97,.94)*spec*.22+uColor3*rim*.13;
            base=mix(base,color,1.0-page);
        }
    }
    
    float grain=fract(sin(dot(gl_FragCoord.xy,vec2(12.9898,78.233)))*43758.5453)-.5;
    base+=grain/640.0;
    float r=min(uRadius,min(uSize.x,uSize.y)*.5);
    float d=roundedBox(uv*uSize-uSize*.5,uSize*.5,r);
    float alpha=(1.0-smoothstep(-.7,.7,d))*uOpacity*elliceRoundedClipMask();
    fragColor=vec4(clamp(base,0.0,1.0)*alpha,alpha);
}
