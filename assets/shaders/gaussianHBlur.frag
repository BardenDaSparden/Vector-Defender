#version 330

uniform sampler2D texture0;
uniform float size;

in vec2 texCoord0;
in vec2 resolution;
out vec4 finalColor;

void main(){
	vec4 sum = vec4(0);
	float blurSize = size / resolution.x;

	sum += texture2D(texture0, vec2(texCoord0.s - 5.0 * blurSize, texCoord0.t)) * 0.02;
	sum += texture2D(texture0, vec2(texCoord0.s - 4.0 * blurSize, texCoord0.t)) * 0.05;
	sum += texture2D(texture0, vec2(texCoord0.s - 3.0 * blurSize, texCoord0.t)) * 0.09;
	sum += texture2D(texture0, vec2(texCoord0.s - 2.0 * blurSize, texCoord0.t)) * 0.12;
	sum += texture2D(texture0, vec2(texCoord0.s - 1.0 * blurSize, texCoord0.t)) * 0.15;
	sum += texture2D(texture0, vec2(texCoord0.s, texCoord0.t)) * 0.16;
	sum += texture2D(texture0, vec2(texCoord0.s + 1.0 * blurSize, texCoord0.t)) * 0.15;
	sum += texture2D(texture0, vec2(texCoord0.s + 2.0 * blurSize, texCoord0.t)) * 0.12;
	sum += texture2D(texture0, vec2(texCoord0.s + 3.0 * blurSize, texCoord0.t)) * 0.09;
	sum += texture2D(texture0, vec2(texCoord0.s + 4.0 * blurSize, texCoord0.t)) * 0.05;
	sum += texture2D(texture0, vec2(texCoord0.s + 5.0 * blurSize, texCoord0.t)) * 0.02;

	finalColor = sum;
}