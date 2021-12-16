package dotj;

import dotj.Light;
import dotj.Matrix4;
import dotj.PerspectiveCamera;
import org.joml.Vector3f;

import java.util.Vector;

public class WorldShader extends Shader {

    private int viewMatrix, modelMatrix, projectionMatrix;

    private int color;
    private int viewPos;
//    private int lightColor;
//    private int lightPos;

    private int mat_ambient, mat_diffuse, mat_specular, mat_shininess;
    private int light_pos, light_ambient, light_diffuse, light_specular;


    public WorldShader(String vertexShader, String fragmentShader) {
        super(vertexShader, fragmentShader);

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

        mat_ambient = getUniformLocation("material.ambient");
        mat_diffuse = getUniformLocation("material.diffuse");
        mat_specular = getUniformLocation("material.specular");
        mat_shininess = getUniformLocation("material.shininess");

        light_pos = getUniformLocation("light.position");
        light_ambient = getUniformLocation("light.ambient");
        light_diffuse = getUniformLocation("light.diffuse");
        light_specular = getUniformLocation("light.specular");

    }

    public void setMaterial(Material material){
        setMaterial(material.getAmbientColor(), material.getDiffuseColor(), material.getSpecularColor(), material.getShininess());
    }

    public void setMaterial(Vector3f ambient, Vector3f diffuse, Vector3f specular, float shininess){
        loadVector3f(mat_ambient, ambient);
        loadVector3f(mat_diffuse, diffuse);
        loadVector3f(mat_specular, specular);
        loadFloat(mat_shininess, shininess);
    }

    public void setLight(Vector3f position, Vector3f ambient, Vector3f diffuse, Vector3f specular){
        loadVector3f(light_pos, position);
        loadVector3f(light_ambient, ambient);
        loadVector3f(light_diffuse, diffuse);
        loadVector3f(light_specular, specular);
    }

    public void setLight(Light light){
        setLight(light.getPosition(), light.getAmbient(), light.getDiffuse(), light.getSpecular());
    }

    public void setColor(Vector3f col){
        loadVector3f(color, col);
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