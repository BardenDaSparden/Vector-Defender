#version 330

uniform sampler2D diffuse;
uniform sampler2D normal;
uniform sampler2D illumination;
uniform sampler2D lightmap;

/*
uniform vec2 resolution;
uniform float time;
uniform vec4 ambientColor = vec4(0.00, 0.00, 0.00, 1);

const int MAX_LIGHTS = 20;

struct Light{
	vec3 position;
	vec4 color;
	vec3 falloff;
	float radius;
};

uniform Light lights[MAX_LIGHTS];
*/

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 finalColor;

void main(){
	vec4 diffuseColor = texture2D(diffuse, texCoord0.st);
	vec4 normalColor = texture2D(normal, texCoord0.st);
	vec4 illuminationColor = texture2D(illumination, texCoord0.st);
	vec4 lightmapColor = texture2D(lightmap, texCoord0.st);

	vec3 lightColor = lightmapColor.rgb;

	vec3 color = diffuseColor.rgb * lightColor;

	finalColor = vec4(color, 1);


	/*
	vec3 ambient = ambientColor.rgb * ambientColor.a;
	vec3 lightSum = ambient;
	vec2 center = resolution.xy / 2;

	for(int i = 0; i < MAX_LIGHTS; i++){
		Light light = lights[i];
		
		vec3 position = vec3(center - gl_FragCoord.xy, 0) + light.position;
		float D = length(position);

		vec3 N = normalize(normalColor.rgb * 2.0 - 1.0);
		vec3 L = normalize(position);

		vec3 diffuse = (light.color.rgb * light.color.a);

		float attenuation = 1.0 / ((light.falloff.x) + (light.falloff.y * 2.0 * D / light.radius) + (light.falloff.z * D * D / light.radius / light.radius));

		//vec3 intensity = ambient + diffuse * attenuation;

		lightSum += attenuation * diffuse.rgb + (diffuse.rgb * max(dot(N, L), 0)) * attenuation;
	}
	
	finalColor = vec4(lightSum * diffuseColor.rgb, diffuseColor.a);
	*/
}