#version 330

uniform sampler2D texture0;
in vec2 v_texCoord0;
out vec4 fragColor;

void main(){
	vec4 sample = texture2D(texture0, v_texCoord0);
	if(length(sample.rgb) < 0.1){
		sample.rgb = vec3(0);
	}
	fragColor = vec4(sample.rgb * 0.94, 1);
}