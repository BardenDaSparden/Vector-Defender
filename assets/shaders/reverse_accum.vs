#version 330

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 texCoord;

out vec2 v_texCoord0;

uniform mat4 projection;

void main(){
	v_texCoord0 = texCoord;
	gl_Position = projection * vec4(position, 1, 1);
}