#version 330

uniform sampler2D colorTable;
uniform sampler2D colorMap;

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 gl_FragColor;

void main(){
	
	vec4 diffuseColor = texture2D(colorMap, texCoord0.st);

	vec4 lookupColor = texture2D(colorTable, vec2(diffuseColor.r, 0.5));
	lookupColor.a = diffuseColor.a;

	gl_FragColor = lookupColor * vertexColor;
}