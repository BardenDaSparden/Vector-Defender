#version 330

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 texCoord;

uniform mat4 projection;
uniform mat4 view;

out vec2 texCoord0; 
out vec2 resolution;

void main(){
	texCoord0 = texCoord;
	resolution = vec2((1.0 / projection[0][0]) * 2.0, (1.0 / projection[1][1]) * 2.0);
	gl_Position = projection * vec4(position, 1, 1);
}