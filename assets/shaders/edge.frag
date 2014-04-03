#ifdef GL_ES
precision mediump float;
#endif
uniform sampler2D u_texture;
uniform vec2 u_size;
varying vec2 v_texCoords;

float unpack_depth(const in vec4 rgba_depth){
    const vec4 bit_shift =
        vec4(1.0/(256.0*256.0*256.0)
            , 1.0/(256.0*256.0)
            , 1.0/256.0
            , 1.0);
    float depth = dot(rgba_depth, bit_shift);
    return depth;
}

vec2 cords_to_pixels(vec2 cords) {
  return vec2(cords.x * u_size.x, cords.y * u_size.y);
}

vec2 pixels_to_cords(vec2 pixel_pos) {
  return vec2(pixel_pos.x / u_size.x, pixel_pos.y / u_size.y);
}

vec4 get_pixel(vec2 pos) {
  return texture2D(u_texture, pixels_to_cords(pos));
}

float get_depth(vec2 pos) {
  return unpack_depth(get_pixel(pos));
}

float avg_intensity(in vec4 pix) {
  return (pix.r + pix.g + pix.b)/3.;
}

float avg_pixel_intensity(vec2 pos) {
  return avg_intensity(get_pixel(pos));
}

float threshold(in float thr1, in float thr2 , in float val) {
  if (val < thr1) {return 0.0;}
  if (val > thr2) {return 1.0;}
  return val;
}

float isEdge(in vec2 coords) {
  float pix[9];
  int k = -1;
  float delta;
  vec2 pixel_pos = cords_to_pixels(coords);

  // read neighboring pixel intensities
  for (int i=-1; i<2; i++) {
    for(int j=-1; j<2; j++) {
      k++;
      pix[k] = get_depth(pixel_pos + vec2(i, j));
    }
  }

  // average color differences around neighboring pixels
  delta = (abs(pix[1]-pix[7])+
          abs(pix[5]-pix[3]) +
          abs(pix[0]-pix[8])+
          abs(pix[2]-pix[6])
           )/4.;

  return threshold(0.2,0.6,clamp(20.0*delta,0.0,1.0));
}

void main() {;
  vec2 pixel_pos      = cords_to_pixels(v_texCoords);
  vec4 color          = get_pixel(pixel_pos);

  if (isEdge(v_texCoords) > 0.0) {
    gl_FragColor = vec4(0,0,0,1);
  } else {
    gl_FragColor = vec4(1,1,1,0);
  }

}