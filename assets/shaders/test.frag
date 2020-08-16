#version 330

uniform sampler2D texture0;

in vec2 v_texCoord;
in vec4 v_color;

out vec4 fragColor;

void main(){

	vec4 texture0Color = texture2D(texture0, v_texCoord);

	fragColor = texture0Color;
}