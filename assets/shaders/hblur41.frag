#version 330

const int MAX_SAMPLES = 41;
float samples[] = float[](0.00026, 0.000447, 0.000746, 0.001212, 0.001914, 0.002941, 0.004396, 0.00639, 0.009035, 0.012427, 0.016624, 0.021631, 0.027377, 0.033702, 0.040354, 0.046998, 0.053241, 0.058664, 0.062872, 0.065541, 0.066456, 0.065541, 0.062872, 0.058664, 0.053241, 0.046998, 0.040354, 0.033702, 0.027377, 0.021631, 0.016624, 0.012427, 0.009035, 0.00639, 0.004396, 0.002941, 0.001914, 0.001212, 0.000746, 0.000447, 0.00026);

uniform sampler2D texture;
uniform float size;
uniform float resolution;

in vec2 texCoord0;
out vec4 finalColor;

void main(){
	vec4 sum = vec4(0);
	float blurSize = size / resolution;

	for(int i = -MAX_SAMPLES / 2; i <= MAX_SAMPLES / 2; i++){
		float weight = samples[i];
		vec2 texCoord = texCoord0 + vec2(i * blurSize, 0);
		sum += texture2D(texture, texCoord) * weight;
	}

	finalColor = sum;
}
