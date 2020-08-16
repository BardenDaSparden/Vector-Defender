#version 330

layout (location = 0) in vec2 in_position;
layout (location = 1) in vec2 in_texCoord;
layout (location = 2) in vec4 in_color;

uniform mat4 projection;

out vec2 out_texCoord;
out vec4 out_color;

void main(){
	out_texCoord = in_texCoord;
	out_color = in_color;
	gl_Position = projection * vec4(in_position, 1, 1);
}