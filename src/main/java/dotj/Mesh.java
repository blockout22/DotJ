package dotj;

import dotj.gameobjects.components.MeshInstance;
import org.joml.Vector3f;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Mesh {


    private int vao;
    private int vbo;
    private int vboTexture;
    private int fbo;
    private int vbon;
    private int vboi;

    private int indicesSize;
    private boolean isModel = false;
    private Material material;

    private BoundingBox boundingBox;

    public Mesh() {
        vao = GL30.glGenVertexArrays();
        vbo = GL15.glGenBuffers();
        vboTexture = GL15.glGenBuffers();
        fbo = GL15.glGenBuffers();
        vbon = GL15.glGenBuffers();
        vboi = GL15.glGenBuffers();
        this.boundingBox = new BoundingBox();
    }

    public void setBoundingBox(Vector3f min, Vector3f max){
        boundingBox.setMin(min);
        boundingBox.setMax(max);
    }

    private void calculateBoundingBox(float[] vertices){
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float minZ = Float.MAX_VALUE;

        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;
        float maxZ = Float.MIN_VALUE;

        for(int i = 0; i < vertices.length; i+= 3){
            float x = vertices[i + 0];
            float y = vertices[i + 1];
            float z = vertices[i + 2];

            if(x < minX){
                minX = x;
            }

            if(y < minY){
                minY = y;
            }

            if(z < minZ){
                minZ = z;
            }

            if(x > maxX){
                maxX = x;
            }

            if(y > maxY){
                maxY = y;
            }

            if(z > maxZ){
                maxZ = z;
            }

            setBoundingBox(new Vector3f(minX, minY, minZ), new Vector3f(maxX, maxY, maxZ));
        }
    }

    public BoundingBox getBoundingBox(){
        return boundingBox;
    }

    public void add(float[] vertices, float[] texCoords, float[] normals, int[] indices) {
        this.texCoords = texCoords;
        if (isModel) {
            System.out.println("Something tried overriding model coords");
            return;
        }
        indicesSize = indices.length;
        GL30.glBindVertexArray(vao);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Utilities.flip(vertices), GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboTexture);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Utilities.flip(texCoords), GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbon);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Utilities.flip(normals), GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL30.glBindVertexArray(0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboi);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Utilities.flip(indices), GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        // setBox(vertices);
        calculateBoundingBox(vertices);

    }

    private float[] texCoords;
    public void print(){
        for(int i = 0; i < texCoords.length; i += 2){
            System.out.println(texCoords[i] + " : " + texCoords[i + 1]);
        }
    }

    protected void setIsModel() {
        isModel = true;
    }

    public void enable() {
        GL30.glBindVertexArray(vao);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboi);


    }

    public void render(int modelMatrix, MeshInstance object, PerspectiveCamera camera) {
//        GL11.glBindTexture(GL11.GL_TEXTURE_2D, object.getMaterial().getDiffuse());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, object.getTextureID());

        if(object.getSpecularTextureID() != 0){
            GL13.glActiveTexture(GL13.GL_TEXTURE1);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, object.getSpecularTextureID());
        }

        if (camera.isInBounds(object.getWorldTransform().getPosition().x, object.getWorldTransform().getPosition().y, object.getWorldTransform().getPosition().z)) {
            Matrix4 transformationMatrix = createTransformationMatrix(object.getWorldTransform().getPosition(), object.getWorldTransform().getRotation(), object.getWorldTransform().getScale());
            Shader.loadMatrix(modelMatrix, transformationMatrix);
//            object.update();
            GL11.glDrawElements(GL11.GL_TRIANGLES, indicesSize, GL11.GL_UNSIGNED_INT, 0);
        }

    }

    private Matrix4 createTransformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale) {
        Matrix4 matrix = new Matrix4();
        matrix.setIdentity();
        Matrix4.translate(translation, matrix, matrix);
        Matrix4.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), matrix, matrix);
        Matrix4.scale(scale, matrix, matrix);

        return matrix;
    }

    public void disable() {

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    public void cleanup() {
        GL30.glBindVertexArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL15.glDeleteBuffers(vbo);
        GL15.glDeleteBuffers(vboTexture);
        GL15.glDeleteBuffers(fbo);
        GL15.glDeleteBuffers(vboi);
        GL30.glDeleteVertexArrays(vao);
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getVao() {
        return vao;
    }

    public int getVboi() {
        return vboi;
    }

    public int getIndicesSize() {
        return indicesSize;
    }
}