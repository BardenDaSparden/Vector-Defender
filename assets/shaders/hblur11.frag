#version 330

uniform sampler2D texture;
uniform float size;
uniform float resolution;

in vec2 texCoord0;
out vec4 finalColor;

void main(){
	vec4 sum = vec4(0);
	float blurSize = size / resolution;

	sum += texture2D(texture, vec2(texCoord0.s - 5.0 * blurSize, texCoord0.t)) * 0.0093;
	sum += texture2D(texture, vec2(texCoord0.s - 4.0 * blurSize, texCoord0.t)) * 0.0280;
	sum += texture2D(texture, vec2(texCoord0.s - 3.0 * blurSize, texCoord0.t)) * 0.0659;
	sum += texture2D(texture, vec2(texCoord0.s - 2.0 * blurSize, texCoord0.t)) * 0.1217;
	sum += texture2D(texture, vec2(texCoord0.s - 1.0 * blurSize, texCoord0.t)) * 0.1757;
	sum += texture2D(texture, vec2(texCoord0.s, texCoord0.t)) * 0.198596;
	sum += texture2D(texture, vec2(texCoord0.s + 1.0 * blurSize, texCoord0.t)) * 0.1757;
	sum += texture2D(texture, vec2(texCoord0.s + 2.0 * blurSize, texCoord0.t)) * 0.1217;
	sum += texture2D(texture, vec2(texCoord0.s + 3.0 * blurSize, texCoord0.t)) * 0.0659;
	sum += texture2D(texture, vec2(texCoord0.s + 4.0 * blurSize, texCoord0.t)) * 0.0280;
	sum += texture2D(texture, vec2(texCoord0.s + 5.0 * blurSize, texCoord0.t)) * 0.0093;


	finalColor = sum;
}