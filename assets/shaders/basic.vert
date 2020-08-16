#version 330

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec4 color;

uniform mat3 modelViewProjection;

out vec2 outTexCoord;
out vec4 outColor;

void main(){

	outTexCoord = texCoord;
	outColor = color;
	vec3 transformedVertex = modelViewProjection * vec3(position.x, position.y, 1);
	gl_Position = vec4(transformedVertex, 1);

}