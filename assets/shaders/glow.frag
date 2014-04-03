
uniform sampler2D u_texture;
varying vec2 v_texCoords;

float toonify(in float intensity) {
    if (intensity > 0.8)
        return 1.0;
    else if (intensity > 0.5)
        return 0.8;
    else if (intensity > 0.25)
        return 0.3;
    else
        return 0.1;
}

void main(){
	vec4 color = texture2D(u_texture, v_texCoords);
	float factor = toonify(max(color.r, max(color.g, color.b)));
	gl_FragColor = vec4(factor*color.rgb, color.a);
}