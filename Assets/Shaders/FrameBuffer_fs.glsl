#version 330 core
out vec4 FragColor;

in vec2 TexCoords;

uniform sampler2D screenTexture;
uniform vec2 textureOffset;

void main()
{

    vec2 coords = TexCoords;

    coords.x += textureOffset.x;
    coords.y += textureOffset.y;

    //default/normal
    vec3 col = texture(screenTexture, coords).rgb;
    FragColor = vec4(col, 1.0);

    //Inverse
//    FragColor = vec4(1 - col, 1.0);

    //Grayscale
    float average = (FragColor.r + FragColor.g + FragColor.b) / 3.0;
//    FragColor = vec4(average, average, average, 1.0);


}