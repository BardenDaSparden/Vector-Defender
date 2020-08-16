#version 330

in vec3 texCoord0;
out vec4 fragColor;

uniform samplerCube cubemapTexture;

void main(){
	fragColor = texture(cubemapTexture, texCoord0);
}