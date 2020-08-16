#version 330

in vec2 v_texCoord0;

out vec4 fragColor;

uniform float offset[5] = float[](0, 1, 2, 3, 4);
uniform float weight[5] = float[](0.227027027, 0.194594594, 0.121621621, 0.054054054, 0.016216216);
uniform float texelSize;
uniform vec2  blurDir;
uniform sampler2D texture0;

void main(){
	fragColor = vec4(0, 0, 0, 1);
	fragColor += texture2D(texture0, v_texCoord0) * weight[0];

	for(int i = 1; i < 5; i += 1){
		fragColor += texture2D(texture0, v_texCoord0 + vec2((offset[i] * texelSize) * blurDir.x, (offset[i] * texelSize) * blurDir.y)) * weight[i];
		fragColor += texture2D(texture0, v_texCoord0 - vec2((offset[i] * texelSize) * blurDir.x, (offset[i] * texelSize) * blurDir.y)) * weight[i];
	}
}