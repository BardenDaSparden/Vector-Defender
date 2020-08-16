#version 330

const vec3 luminanceVector = vec3(0.3, 0.59, 0.11);

uniform sampler2D texture;
uniform highp float threshold;

in vec2 texCoord0;

out vec4 finalColor;

void main(){
	vec3 diffuse = texture2D(texture, texCoord0).rgb;

    float luminance = dot(luminanceVector, diffuse.rgb);
    luminance = max(0.0, luminance - threshold);

    diffuse *= sign(luminance);

    finalColor = vec4(diffuse, 1);
}