#version 330

const int MAX_LIGHTS = 20;
const float CUTOFF = 0.01;

struct Light{
	vec3 position;
	vec3 falloff;
	vec4 color;
	float radius;
};

uniform sampler2D diffuse;
uniform sampler2D normal;
uniform sampler2D specular;

uniform vec4 ambient_color = vec4(0, 0, 0, 1);
uniform Light lights[MAX_LIGHTS];

uniform vec2 resolution;

in vec2 out_texCoord;
in vec4 out_color;

out vec4 final_color;

void main(){

	vec4 diffuseColor = texture2D(diffuse, out_texCoord.st);
	vec3 normal = 		texture2D(normal, out_texCoord.st).rgb;
	vec4 specular =		texture2D(specular, out_texCoord.st);

	vec3 light_sum = (ambient_color.rgb * ambient_color.a);
	vec2 center = resolution / 2.0;

	for(int i = 0; i < MAX_LIGHTS; i++){
		Light light = lights[i];

		vec2 p = gl_FragCoord.xy - center;
		vec3 L = light.position - vec3(p, 0);
		vec3 N = normal * 2.0 - 1.0;

		float dist = distance(vec3(p, 0), light.position);
		N = normalize(N);
		L = normalize(L);

		float cA = 1.0 * light.falloff.x;
		float lA = (2.0 * dist / light.radius) * light.falloff.y;
		float qA = (dist * dist / light.radius / light.radius) * light.falloff.z;

		float attenutation = max(CUTOFF, 1.0 / (cA + lA + qA));

		vec3 diffuse = (light.color.rgb * light.color.a) * max(dot(N, L), 0) * attenutation * specular.rgb;
		vec3 intensity = diffuse;

		light_sum += intensity;

		//light_sum += light.color * attenutation;
	}

	final_color = vec4(light_sum * diffuseColor.rgb, diffuseColor.a);
}