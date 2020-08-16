#version 330

uniform vec2 position;
uniform vec3 color;
uniform vec3 falloff;
uniform float radius;
uniform vec2 direction;
uniform vec2 left;
uniform vec2 right;
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
	attenuation *= clamp(rand(dPos), 0.9, 0.95);

	vec2 p = normalize(dPos);
	float ld = dot(p, left);
	float rd = dot(p, right);
	float v = ld * rd;
	float a = 0;

	if(v < 0.0){
		a = 1;
	}

	float tl = dot(left, left);
	float tr = dot(right, right);
	ld *= 1.4;
	rd *= 1.4;
	float m = 0;

	if(abs(ld) < abs(rd)){
		m = ld / tl;
	} else {
		m = rd / tr;
	}

	m = abs(m);

	a *= sign(dot(dPos, direction)) * m;

	c *= attenuation * a;
	return c;
}

void main(){
	outputColor = vec4(calculateLight(gl_FragCoord.xy), 1) + vec4(rand(0.1 + gl_FragCoord.xy) / 255);
}