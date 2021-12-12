import org.joml.Vector3f;

public class WorldShader extends Shader {

    private int viewMatrix, modelMatrix, projectionMatrix;

    private int location_lightPosition;
    private int location_lightColour;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_useFakeLighting;
    private int location_skycolour;


    public WorldShader(String vertexShader, String fragmentShader) {
        super(vertexShader, fragmentShader);

        bindAttribLocation(0, "position");
        bindAttribLocation(1, "textureCoordinates");
        bindAttribLocation(2, "normal");
        linkAndValidate();

        modelMatrix = getUniformLocation("transformationMatrix");
        projectionMatrix = getUniformLocation("projectionMatrix");
        viewMatrix = getUniformLocation("viewMatrix");

        location_lightPosition = getUniformLocation("lightPosition");
        location_lightColour = getUniformLocation("lightColour");
        location_shineDamper = getUniformLocation("shineDamper");
        location_reflectivity = getUniformLocation("reflectivity");
        location_useFakeLighting = getUniformLocation("useFakeLighting");
        location_skycolour = getUniformLocation("skyColour");

    }

    public void loadViewMatrix(PerspectiveCamera camera) {
        Matrix4 matrix = createViewMatrix(camera);
        loadMatrix(viewMatrix, matrix);
    }

    public void loadShineVariables(float damper, float reflectivity){
        loadFloat(location_shineDamper, damper);
        loadFloat(location_reflectivity, reflectivity);
    }

    public void loadLight(Light light){
        loadVector3f(location_lightPosition, light.getPosition());
        loadVector3f(location_lightColour, light.getColour());
    }

    public void loadSkyColour(float r, float g, float b){
        loadVector3f(location_skycolour, new Vector3f(r, g, b));
    }

    public void loadFakeLightingVariable(boolean useFake){
        loadBoolean(location_useFakeLighting, useFake);
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