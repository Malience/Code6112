#version 330 core

const int ROAD = 82;
const int BUILDING = 66;
const int CAR = 67;
const int DEPOT = 68;

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