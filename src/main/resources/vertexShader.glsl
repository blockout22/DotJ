#version 150

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;

out vec2 texCoordinates;
out vec3 outNormal;
out vec3 FragPos;

uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void){

	vec4 worldPosition = modelMatrix * vec4(position,1.0);
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCam;

//	gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(position, 1.0);

	//pased to the Frag Shader
	texCoordinates = textureCoordinates;
	FragPos = vec3(modelMatrix * vec4(position, 1.0));
//	outNormal = normal;
	outNormal = mat3(transpose(inverse(modelMatrix))) * normal;
}