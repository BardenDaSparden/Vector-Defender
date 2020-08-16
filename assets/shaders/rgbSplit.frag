#version 330

uniform sampler2D texture;
uniform vec2 redOffset;
uniform vec2 blueOffset;
uniform vec2 greenOffset;

in vec2 texCoord0;
in vec4 color0;

out vec4 finalColor;

void main(){
	float red = texture2D(texture, texCoord0 - redOffset).r;
	float green = texture2D(texture, texCoord0 - greenOffset).g;
	float blue = texture2D(texture, texCoord0 - blueOffset).b;
	finalColor = vec4(red, green, blue, 1) * color0;
}