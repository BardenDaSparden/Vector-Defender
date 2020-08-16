#version 330

in vec2 texCoord0;

uniform sampler2D texture;
uniform vec2 resolution;

out vec4 outputColor;

const float THRESHOLD = 0.75;
const float PI = 3.14;

void main(){

	float distance = 1.0;

	for (float y=resolution.y; y > -1.0; y-=1.0) {
  		//rectangular to polar filter
		vec2 norm = vec2(texCoord0.s, y/resolution.y) * 2.0 - 1.0;
		float theta = PI*1.5 + norm.x * PI; 
		float r = (1.0 + norm.y) * 0.5;
		
		//coord which we will sample from occlude map
		vec2 coord = vec2(-r * sin(theta), -r * cos(theta))/2.0 + 0.5;
		
		//sample the occlusion map
		vec4 data = texture2D(texture, coord);
		
		//the current distance is how far from the top we've come
		float dst = y/resolution.y;
		
		//if we've hit an opaque fragment (occluder), then get new distance
		//if the new distance is below the current, then we'll use that for our ray
		float caster = data.a;
		if (caster > THRESHOLD) {
			distance = min(distance, dst);
			//NOTE: we could probably use "break" or "return" here
  		}
  } 

 outputColor = vec4(vec3(distance), 1.0);
 //outputColor = vec4(1, 1, 0, 1);

}