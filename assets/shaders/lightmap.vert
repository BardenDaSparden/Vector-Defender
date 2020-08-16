#version 330

layout (location = 0) in vec2 in_position;
layout (location = 1) in vec2 in_texCoord;

uniform mat4 projection;

out vec2 out_texCoord;

void main(){

	out_texCoord = in_texCoord;
	gl_Position = projection * vec4(in_position, 1, 1);

}