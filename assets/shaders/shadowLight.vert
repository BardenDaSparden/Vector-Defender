#version 330

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec4 color;

uniform mat4 projection;
uniform mat4 view;

out vec2 texCoord0;
out vec4 vertexColor;

void main(){

	texCoord0 = texCoord;
	vertexColor = color;
	gl_Position = projection * view * vec4(position, 1, 1);

}