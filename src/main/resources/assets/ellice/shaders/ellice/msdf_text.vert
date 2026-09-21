#version 330 core

layout(location = 0) in vec2 aPos;       
layout(location = 1) in vec2 aTexCoord;  
layout(location = 2) in vec4 aColor;     
layout(location = 3) in vec4 aCell;      

uniform vec2 uResolution;

out vec2 vTexCoord;
out vec4 vColor;
out vec2 vCellMin;
out vec2 vCellMax;

void main() {
    gl_Position = vec4(
        aPos.x / uResolution.x *  2.0 - 1.0,
        aPos.y / uResolution.y * -2.0 + 1.0,
        0.0, 1.0
    );
    vTexCoord = aTexCoord;
    vColor = aColor;
    vCellMin = aCell.xy;
    vCellMax = aCell.zw;
}
