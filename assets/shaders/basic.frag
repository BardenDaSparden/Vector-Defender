#version 330

uniform sampler2D texture0;

in vec2 outTexCoord;
in vec4 outColor;

out vec4 finalColor;

void main(){
	vec4 texture0Color = texture2D(texture0, outTexCoord.st);
	finalColor = texture0Color * outColor;
}