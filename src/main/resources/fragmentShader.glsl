#version 400

in vec2 texCoordinates;
in vec3 outNormal;
in vec3 FragPos;

out vec4 out_Color;

uniform sampler2D modelTexture;

uniform vec3 viewPos;
uniform vec3 color = vec3(1, 1, 1);

float specularStrength = 0.5;

struct Material {
	sampler2D diffuse;
	sampler2D specular;
	float shininess;
};

uniform Material material;

struct DirLight{
	vec3 direction;

	vec3 ambient;
	vec3 diffuse;
	vec3 specular;

	float constant;
	float linear;
	float quadratic;
};
uniform DirLight dirLight;

struct PointLight{
	bool isActive;
	vec3 position;

	vec3 ambient;
	vec3 diffuse;
	vec3 specular;

	float constant;
	float linear;
	float quadratic;
};
#define NR_POINT_LIGHTS 4
uniform PointLight pointLights[NR_POINT_LIGHTS];


vec3 CalcDirLight(DirLight light, vec3 normal, vec3 viewDir){
	vec3 lightDir = normalize(-light.direction);

	float diff = max(dot(normal, lightDir), 0.0);

	vec3 halfwayDir = normalize(lightDir + viewDir);

	vec3 reflectDir = reflect(-lightDir, normal);
//	float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
	float spec = pow(max(dot(normal, halfwayDir), 0.0), material.shininess);

	vec3 ambient = light.ambient * vec3(texture(material.diffuse, texCoordinates));
	vec3 diffuse = light.diffuse * diff * vec3(texture(material.diffuse, texCoordinates));
	vec3 specular = light.specular * spec * vec3(texture(material.specular, texCoordinates));

	return (ambient + diffuse + specular);
}

vec3 CalcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir){
	vec3 lightDir = normalize(light.position - fragPos);

	float diff = max(dot(normal, lightDir), 0.0);

	vec3 halfwayDir = normalize(lightDir + viewDir);

	vec3 reflectDir = reflect(-lightDir, normal);
//	float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
	float spec = pow(max(dot(normal, halfwayDir), 0.0), material.shininess);

	float distance    = length(light.position - fragPos);
	float attenuation = 1.0 / (light.constant + light.linear * distance +
	light.quadratic * (distance * distance));

	vec3 ambient = light.ambient * vec3(texture(material.diffuse, texCoordinates));
	vec3 diffuse = light.diffuse * diff * vec3(texture(material.diffuse, texCoordinates));
	vec3 specular = light.specular * spec * vec3(texture(material.specular, texCoordinates));

	ambient *= attenuation;
	diffuse *= attenuation;
	specular *= attenuation;

	return (ambient + diffuse + specular);
}


void main(void){
	vec4 texColor = texture(material.diffuse, texCoordinates);

	if(texColor.a < 0.1){
		discard;
	}

	vec3 norm = normalize(outNormal);
	vec3 viewDir = normalize(viewPos - FragPos);

	vec3 result = CalcDirLight(dirLight, norm, viewDir) * color;//(ambient + diffuse + specular) * color; //textureColour.xyz;

	for(int i = 0; i < NR_POINT_LIGHTS; i++){
		if(pointLights[i].isActive){
			result += CalcPointLight(pointLights[i], norm, FragPos, viewDir);
		}
	}



	float gamma = 2.2;
	vec3 diffuseColor = pow(texColor.rgb, vec3(gamma));

	out_Color = vec4(vec3(result * diffuseColor) , texColor.a);

	//depth view
//	out_Color = vec4(vec3(gl_FragCoord.z), 1.0);
}