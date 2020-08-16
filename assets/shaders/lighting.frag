#version 330

precision highp float;

struct Light{
	vec3 position;
	vec3 color;
	vec3 falloff;
	float radius;
};

const int MAX_LIGHTS = 40;

const float specularPower = 10.0f;

uniform sampler2D diffuseTexture;
uniform sampler2D normalTexture;
uniform sampler2D specularTexture;

uniform Light lights[MAX_LIGHTS];

uniform vec3 ambientColor;
uniform vec2 resolution;

in vec2 texCoord0;
in vec4 color0;
in vec4 worldPos0;

out vec4 finalColor;

vec3 calculatePointLight(Light light){
	vec3 color = vec3(0);

	vec3 diffuse = 	texture2D(diffuseTexture, texCoord0.st).rgb;
	vec3 normal = 	texture2D(normalTexture, texCoord0.st).rgb;
	vec3 specular = texture2D(specularTexture, texCoord0.st).rgb;

	vec3 position = vec3(resolution / 2, 0) + light.position;
	vec3 lightDirection = position - gl_FragCoord.xyz;

	float D = length(lightDirection);
	float radius = light.radius;

	vec3 N = normalize((normal * 2.0 - 1.0f));
	vec3 L = normalize(lightDirection);

	float cFactor = light.falloff.x;
	float lFactor = light.falloff.y * D * 2 / radius;
	float qFactor = light.falloff.z * D * D / radius / radius;

	float attenuation = max(1.0 / (cFactor + lFactor + qFactor), 0);

	vec3 diffuseColor = light.color * max(dot(N, L), 0.0);
	vec3 intensity = (ambientColor + diffuseColor) * attenuation;
	color += (diffuse) * intensity;

	vec3 specularColor = vec3(0);

	vec3 directionToEye = normalize(worldPos0.xyz - light.position);
	vec3 reflectionDirection = normalize(reflect(L, N));

	float specularFactor = dot(directionToEye, reflectionDirection);
	specularFactor = max(pow(specularFactor, specularPower), 0.0);

	specularColor = light.color * specular * specularFactor * attenuation;

	color += specularColor;
	return color;
}

void main(){

	vec3 sum = vec3(0);
	for(int i = 0; i < MAX_LIGHTS; i++){
		Light light = lights[i];
		sum += calculatePointLight(light);
	}

	finalColor = vec4(sum, 1);
	//finalColor = texture2D(diffuseTexture, texCoord0.st);
}