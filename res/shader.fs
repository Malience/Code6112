#version 330 core

const int ROAD = 1;
const int BUILDING = 0;
const int CAR = 2;
const int DEPOT = 3;

in vec2 TexCoord;

out vec4 FragColor;

uniform sampler2D roadTexture;
uniform sampler2D buildingTexture;
uniform sampler2D carTexture;
uniform int Block;

void main(){
	switch(Block){
	case ROAD:
		FragColor = texture(roadTexture, TexCoord);
		break;
	case BUILDING:
		FragColor = texture(buildingTexture, TexCoord);
		break;
	case CAR:
		vec4 carTex = texture(carTexture, TexCoord);
		if(carTex.a > 0) FragColor = carTex;
		else FragColor = texture(roadTexture, TexCoord);
		break;
	}
	
}