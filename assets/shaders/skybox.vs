#version 330

layout (location = 0) in vec3 position;

uniform mat4 PVM;

out vec3 texCoord0;

void main(){
	gl_Position = vec4(position, 1.0);
	texCoord0 = position;
}