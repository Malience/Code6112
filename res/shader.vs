#version 330 core
  
layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;

out vec2 TexCoord;

uniform vec2 pos;
uniform vec2 res;

void main(){
	gl_Position = vec4((pos.x + position.x) * res.x, (pos.y + position.y) * res.y,0,1);
	TexCoord = vec2(texCoord.x, 1.0f - texCoord.y);
}