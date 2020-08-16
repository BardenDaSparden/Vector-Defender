#version 330

uniform sampler2D texture0;
uniform float size;

in vec2 texCoord0;
in vec2 resolution;
out vec4 finalColor;

void main(){
	vec4 sum = vec4(0);
	float blurSize = size / resolution.y;

	sum += texture2D(texture0, vec2(texCoord0.s, texCoord0.t - 5.0 * blurSize)) * 0.02;
	sum += texture2D(texture0, vec2(texCoord0.s, texCoord0.t - 4.0 * blurSize)) * 0.05;
	sum += texture2D(texture0, vec2(texCoord0.s, texCoord0.t - 3.0 * blurSize)) * 0.09;
	sum += texture2D(texture0, vec2(texCoord0.s, texCoord0.t - 2.0 * blurSize)) * 0.12;
	sum += texture2D(texture0, vec2(texCoord0.s, texCoord0.t - 1.0 * blurSize)) * 0.15;
	sum += texture2D(texture0, vec2(texCoord0.s, texCoord0.t)) * 0.16;
	sum += texture2D(texture0, vec2(texCoord0.s, texCoord0.t + 1.0 * blurSize)) * 0.15;
	sum += texture2D(texture0, vec2(texCoord0.s, texCoord0.t + 2.0 * blurSize)) * 0.12;
	sum += texture2D(texture0, vec2(texCoord0.s, texCoord0.t + 3.0 * blurSize)) * 0.09;
	sum += texture2D(texture0, vec2(texCoord0.s, texCoord0.t + 4.0 * blurSize)) * 0.05;
	sum += texture2D(texture0, vec2(texCoord0.s, texCoord0.t + 5.0 * blurSize)) * 0.02;

	finalColor = sum;
}