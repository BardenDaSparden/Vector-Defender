#version 330

uniform vec2 position;
uniform vec3 color;
uniform vec3 falloff;
uniform float radius;
uniform vec2 resolution;

in vec4 vertexColor;
out vec4 outputColor;

float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

vec3 calculateLight(vec2 point){
	vec3 c = color;
	vec2 center = resolution / 2;
	vec2 dPos = point - center - position;

	float d = length(dPos);
	float cf = falloff.x;
	float lf = falloff.y * 2 / radius * d;
	float qf = falloff.z / radius / radius * d * d;

	float attenuation = 1.0 / (cf + lf + qf);
	c *= attenuation;
	c += vec3(rand(point + position) / 300);
	return c;
}

void main(){
	outputColor = vec4(calculateLight(gl_FragCoord.xy), 1);
}