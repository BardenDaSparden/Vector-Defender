#version 330

uniform sampler2D texture;
uniform float size;
uniform float resolution;

in vec2 texCoord0;
out vec4 finalColor;

void main(){
	vec4 sum = vec4(0);
	float blurSize = size / resolution;

	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t - 10.0 * blurSize)) * 0.000001;
	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t - 9.0 * blurSize)) * 0.00001;
	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t - 8.0 * blurSize)) * 0.000078;
	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t - 7.0 * blurSize)) * 0.000489;
	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t - 6.0 * blurSize)) * 0.002403;
	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t - 5.0 * blurSize)) * 0.009245;
	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t - 4.0 * blurSize)) * 0.027835;
	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t - 3.0 * blurSize)) * 0.065591;
	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t - 2.0 * blurSize)) * 0.120978;
	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t - 1.0 * blurSize)) * 0.174666;

	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t)) * 0.197413;

	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t + 10.0 * blurSize)) * 0.000001;
	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t + 9.0 * blurSize)) * 0.00001;
	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t + 8.0 * blurSize)) * 0.000078;
	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t + 7.0 * blurSize)) * 0.000489;
	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t + 6.0 * blurSize)) * 0.002403;
	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t + 5.0 * blurSize)) * 0.009245;
	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t + 4.0 * blurSize)) * 0.027835;
	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t + 3.0 * blurSize)) * 0.065591;
	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t + 2.0 * blurSize)) * 0.120978;
	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t + 1.0 * blurSize)) * 0.174666;

	finalColor = sum;
}