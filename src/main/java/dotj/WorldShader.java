package dotj;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class WorldShader extends Shader {

    private int viewMatrix, modelMatrix, projectionMatrix;

    private int color;
    private int viewPos;
//    private int lightColor;
//    private int lightPos;

    private int mat_diffuse, mat_specular, mat_shininess;
    private int light_dir, light_ambient, light_diffuse, light_specular;


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

        mat_diffuse = getUniformLocation("material.diffuse");
        mat_specular = getUniformLocation("material.specular");
        mat_shininess = getUniformLocation("material.shininess");

        light_dir = getUniformLocation("light.direction");
        light_ambient = getUniformLocation("light.ambient");
        light_diffuse = getUniformLocation("light.diffuse");
        light_specular = getUniformLocation("light.specular");

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