public class WorldShader extends Shader {

    private int viewMatrix, modelMatrix, projectionMatrix;

    public WorldShader(String vertexShader, String fragmentShader) {
        super(vertexShader, fragmentShader);

        bindAttribLocation(0, "position");
        bindAttribLocation(1, "pass_texCoords");
        bindAttribLocation(2, "normal");
        linkAndValidate();

        modelMatrix = getUniformLocation("modelMatrix");
        projectionMatrix = getUniformLocation("projectionMatrix");
        viewMatrix = getUniformLocation("viewMatrix");

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