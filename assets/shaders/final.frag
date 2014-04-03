#ifdef GL_ES
precision mediump float;
#endif
uniform sampler2D u_outline_texture;
uniform sampler2D u_color_texture;
varying vec2 v_texCoords;
void main() {

  vec4 outline_color = texture2D(u_outline_texture, v_texCoords);
  vec4 diffuse_color = texture2D(u_color_texture, v_texCoords);

  gl_FragColor = diffuse_color * outline_color;
}