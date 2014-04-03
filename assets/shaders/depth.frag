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

void main(){
    float depth = 1.0 - v_depth;
    gl_FragColor = vec4(depth, depth, depth, 1.0);
}