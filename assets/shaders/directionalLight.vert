#version 330

layout (location = 0) in vec2 position;
layout (location = 2) in vec4 color;

uniform mat4 projection;

out vec4 vertexColor;

void main(){

	vertexColor = color;
	gl_Position = projection * vec4(position, 1, 1);

}