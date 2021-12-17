#version 400

out vec4 out_Color;

float near = 0.1;
float far = 150;

void main(){

    float depth = gl_FragCoord.z;
    float ndc = depth * 2.0 - 1.0;

    float linearDepth = ((2.0 * near * far) / (far + near - ndc * (far - near))) / far;
    out_Color = vec4(vec3(linearDepth), 1.0);
}