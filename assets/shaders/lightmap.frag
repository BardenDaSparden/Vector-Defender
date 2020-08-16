#version 330

uniform sampler2D texture0;
uniform sampler2D texture1;

in vec2 out_texCoord;
out vec4 finalColor;

void main(){

	vec3 diffuseColor = texture2D(texture0, out_texCoord.st).rgb;
	vec3 lightmapColor = texture2D(texture1, out_texCoord.st).rgb;

	finalColor = vec4((diffuseColor * lightmapColor), 1);

}