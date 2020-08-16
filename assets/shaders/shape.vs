#version 330

layout (location = 0) in vec2 position;
layout (location = 1) in vec4 color;

out vec4 pass_color;

uniform mat4 projection;

void main(){
	pass_color = color;
	gl_Position = projection * vec4(position, 1, 1);
}