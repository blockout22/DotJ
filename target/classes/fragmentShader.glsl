#version 150

in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D modelTexture;
uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;

struct DirLight {
	vec3 direction;

	vec3 ambient;
	vec3 diffuse;
	vec3 specular;
};

uniform DirLight dirLight;

struct Material {
	vec3 ambient;
	vec3 diffuse;
	vec3 specular;
	float shininess;
};

uniform Material material;

//vec3 CalcDirLight(DirLight light, vec3 normal, vec3 viewDir){
//	vec3 lightDir = normalize(-light.direction);
//
//	float diff = max(dot(normal, lightDir), 0.0);
//
//	vec3 reflectDir = reflect(-lightDir, normal);
//	float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
//
//	vec3 ambient = light.ambient * vec3(texture(material.diffuse, pass_textureCoordinates));
//	vec3 diffuse = light.diffuse * diff * vec3(texture(material.diffuse, pass_textureCoordinates));
//	vec3 specular = light.specular * spec * vec3(texture(material.specular, pass_textureCoordinates));
//
//	return (ambient + diffuse + specular);
//}

void main(void){
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);

	float nDot1 = dot(unitNormal, unitLightVector);
	float bightness = max(nDot1, 0.1);
	vec3 diffuse = bightness * lightColour;

	vec3 unitVectorToCamera = normalize(toCameraVector);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

	float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
	specularFactor = max(specularFactor, 0.0);
	float dampedFactor = pow(specularFactor, shineDamper);
	vec3 finalSpecular = dampedFactor * reflectivity * lightColour;

	vec4 textureColour = texture(modelTexture,pass_textureCoordinates);
	if(textureColour.a < 0.5){
		discard;
	}

	float ambientStrength = 0.1;
	vec3 ambient = ambientStrength + lightColour;

	vec3 result = ambient * textureColour.xyz;

//	out_Color = vec4(result, 1.0);
	out_Color = vec4(diffuse, 1.0) * textureColour + vec4(finalSpecular, 1.0);
//	out_Color = mix(vec4(skyColour, 1.0), out_Color, visibility);
}