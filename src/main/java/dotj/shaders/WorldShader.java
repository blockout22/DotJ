package dotj.shaders;

import dotj.Material;
import dotj.Matrix4;
import dotj.PerspectiveCamera;
import dotj.Shader;
import dotj.light.DirectionalLight;
import dotj.light.PointLight;
import org.joml.Vector3f;

public class WorldShader extends Shader {

    private int viewMatrix, modelMatrix, projectionMatrix;

    private int color;
    private int viewPos;
//    private int lightColor;
//    private int lightPos;

    private int mat_diffuse, mat_specular, mat_shininess;

    private int light_dir, light_ambient, light_diffuse, light_specular;
    private int light_constant, light_linear, light_quadratic;


    public WorldShader() {
//        super("vertexShader.glsl", "fragmentShader.glsl");
        super("vertexShader.glsl", "fragmentShader.glsl");

        bindAttribLocation(0, "position");
        bindAttribLocation(1, "textureCoordinates");
        bindAttribLocation(2, "normal");
        linkAndValidate();

        modelMatrix = getUniformLocation("modelMatrix");
        projectionMatrix = getUniformLocation("projectionMatrix");
        viewMatrix = getUniformLocation("viewMatrix");

        color = getUniformLocation("color");
//        lightColor = getUniformLocation("lightColor");
//        lightPos = getUniformLocation("lightPos");
        viewPos = getUniformLocation("viewPos");

        mat_diffuse = getUniformLocation("material.diffuse");
        mat_specular = getUniformLocation("material.specular");
        mat_shininess = getUniformLocation("material.shininess");

        light_dir = getUniformLocation("dirLight.direction");
        light_ambient = getUniformLocation("dirLight.ambient");
        light_diffuse = getUniformLocation("dirLight.diffuse");
        light_specular = getUniformLocation("dirLight.specular");

        light_constant = getUniformLocation("dirLight.constant");
        light_linear = getUniformLocation("dirLight.linear");
        light_quadratic = getUniformLocation("dirLight.quadratic");

    }

    public void setLightConstant(float constant){
        loadFloat(light_constant, constant);
    }

    public void setLightLinear(float linear){
        loadFloat(light_linear, linear);
    }

    public void setLightQuadratic(float quadratic){
        loadFloat(light_quadratic, quadratic);
    }

    public void setMaterial(Material material){
        setMaterial(material.getDiffuse(), material.getSpecular(), material.getShininess());
    }

    public void setMaterial(int diffuse, int specular, float shininess){
//        loadVector3f(mat_diffuse, diffuse);
        loadInt(mat_diffuse, diffuse);
        loadInt(mat_specular, specular);
        loadFloat(mat_shininess, shininess);
    }

    public void setLight(Vector3f position, Vector3f ambient, Vector3f diffuse, Vector3f specular){
        loadVector3f(light_dir, position);
        loadVector3f(light_ambient, ambient);
        loadVector3f(light_diffuse, diffuse);
        loadVector3f(light_specular, specular);
    }

    public void setLight(DirectionalLight light){
        setLight(light.getDirection(), light.getAmbient(), light.getDiffuse(), light.getSpecular());
    }

    public void setPointLight(PointLight pointLight, int index){
        String name = "pointLights[" + index + "]";
        loadBoolean(getUniformLocation(name + ".isActive"), true);
        loadVector3f(getUniformLocation(name + ".position"), pointLight.getPosition());
        loadVector3f(getUniformLocation(name + ".ambient"), pointLight.getAmbient());
        loadVector3f(getUniformLocation(name + ".diffuse"), pointLight.getDiffuse());
        loadVector3f(getUniformLocation(name + ".specular"), pointLight.getSpecular());

        loadFloat(getUniformLocation(name + ".constant"), pointLight.getConstant());
        loadFloat(getUniformLocation(name + ".linear"), pointLight.getLinear());
        loadFloat(getUniformLocation(name + ".quadratic"), pointLight.getQuadratic());
    }

    public void setColor(Vector3f col){
        loadVector3f(color, col);
    }
    public void setColor(float r, float g, float b){
        loadVector3f(color, r, g, b);
    }

//    public void setLightColor(float r, float g, float b){
//        loadVector3f(lightColor, new Vector3f(r, g, b));
//    }
//
//    public void setLightPos(float x, float y, float z){
//        loadVector3f(lightPos, new Vector3f(x, y, z));
//    }

    public void setViewPos(PerspectiveCamera camera){
        loadVector3f(viewPos, camera.getPosition());
    }

    public void loadViewMatrix(PerspectiveCamera camera) {
        Matrix4 matrix = createViewMatrix(camera);
        loadMatrix(viewMatrix, matrix);
    }

    public int getViewMatrix() {
        return viewMatrix;
    }

    public int getModelMatrix() {
        return modelMatrix;
    }

    public int getProjectionMatrix() {
        return projectionMatrix;
    }

}