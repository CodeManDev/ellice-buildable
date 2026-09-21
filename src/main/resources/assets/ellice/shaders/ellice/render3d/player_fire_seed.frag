#version 330 core

uniform sampler2D uPlayerMask;
uniform sampler2D uPlayerDepth;

in vec2 vTexCoord;
out vec4 fragColor;

void main() {
    ivec2 resolution = textureSize(uPlayerMask, 0);
    ivec2 pixel = clamp(ivec2(gl_FragCoord.xy),
        ivec2(0), resolution - ivec2(1));
    vec2 mask = texelFetch(uPlayerMask, pixel, 0).rg;
    
    
    if (mask.r <= 0.001) {
        fragColor = vec4(0.0);
        return;
    }
    float rawDepth = texelFetch(uPlayerDepth, pixel, 0).r;
    if (rawDepth >= 0.999999) {
        fragColor = vec4(0.0);
        return;
    }

    
    
    
    bool boundary = mask.r < 0.999;
    for (int y = -1; y <= 1 && !boundary; ++y) {
        for (int x = -1; x <= 1; ++x) {
            if (x == 0 && y == 0) continue;
            ivec2 neighbour = pixel + ivec2(x, y);
            if (any(lessThan(neighbour, ivec2(0)))
                || any(greaterThanEqual(neighbour, resolution))) {
                boundary = true;
                break;
            }
            if (texelFetch(uPlayerMask, neighbour, 0).r < 0.5) {
                boundary = true;
                break;
            }
        }
    }
    if (!boundary) {
        fragColor = vec4(0.0);
        return;
    }

    
    
    float proximity = max(1.0 - rawDepth, 0.0001);
    float identity = clamp(mask.g / max(mask.r, 0.0001), 0.0, 1.0);
    fragColor = vec4(0.0, 0.0, proximity, identity);
}
