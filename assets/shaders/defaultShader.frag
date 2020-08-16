#version 330

uniform sampler2D texture0;

in vec2 v_texCoord0;
in vec4 v_color;

out vec4 f_color;

void main(){
	vec4 diffuse = texture2D(texture0, v_texCoord0.st);
	f_color = v_color * diffuse;
}