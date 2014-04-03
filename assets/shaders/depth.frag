#ifdef GL_ES
#define LOWP lowp
#define MED mediump
#define HIGH highp
precision mediump float;
#else
#define MED
#define LOWP
#define HIGH
#endif

varying HIGH float v_depth;

vec4 pack_depth(const in float depth){
    const HIGH vec4 bit_shift =
        vec4(256.0*256.0*256.0, 256.0*256.0, 256.0, 1.0);
    const HIGH vec4 bit_mask  =
        vec4(0.0, 1.0/256.0, 1.0/256.0, 1.0/256.0);
    vec4 res = fract(depth * bit_shift);
    res -= res.xxyz * bit_mask;
    return res;
}

void main(){
    float depth = 1.0 - v_depth;
    //gl_FragColor = vec4(depth, depth, depth, 1.0);
    gl_FragColor = pack_depth(1.0 - v_depth);
}