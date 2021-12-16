#version 150

in vec2 pass_textureCoordinates;
in vec3 outNormal;
in vec3 FragPos;

out vec4 out_Color;

uniform sampler2D modelTexture;

uniform vec3 viewPos;
uniform vec3 color = vec3(1, 1, 1);

float specularStrength = 0.5;

struct Material {
	vec3 ambient;
	vec3 diffuse;
	vec3 specular;
	float shininess;
};

uniform Material material;

struct Light{
	vec3 position;

	vec3 ambient;
	vec3 diffuse;
	vec3 specular;
};

uniform Light light;


void main(void){


	//ambient/diffuse
	float ambientStrength = 0.1;
	vec3 ambient = light.ambient * material.ambient;

	vec3 norm = normalize(outNormal);
	vec3 lightDir = normalize(light.position - FragPos);

	float diff = max(dot(norm, lightDir), 0.0);
	vec3 diffuse = light.diffuse * (diff * material.diffuse);

	//specular
	vec3 viewDir = normalize(viewPos - FragPos);
	vec3 reflectDir = reflect(-lightDir, norm);

	float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
	vec3 specular = light.specular * (spec * material.specular); //specularStrength * spec * lightColor;


	vec4 textureColour = texture(modelTexture,pass_textureCoordinates);

	vec3 result = (ambient + diffuse + specular) * color; //textureColour.xyz;
	out_Color = vec4(result, 1.0) * textureColour;
}