#version 330

layout(location = 0) in vec2 position;
layout(location = 1) in vec2 texCoord0;
layout(location = 2) in vec4 color;

out vec2 v_texCoord0;
out vec4 v_color;

uniform mat4 projection;

void main(){
	v_texCoord0 = texCoord0;
	v_color = color;
	gl_Position = projection * vec4(position, 1, 1);
}