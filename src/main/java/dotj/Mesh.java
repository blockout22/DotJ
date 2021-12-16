package dotj;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Mesh {

    private int vao;
    private int vbo;
    private int vboTexture;
    private int vbon;
    private int vboi;

    private int indicesSize;
    private boolean isOBJ = false;

    private BoundingBox boundingBox;

    public Mesh() {
        vao = GL30.glGenVertexArrays();
        vbo = GL15.glGenBuffers();
        vboTexture = GL15.glGenBuffers();
        vbon = GL15.glGenBuffers();
        vboi = GL15.glGenBuffers();
        this.boundingBox = new BoundingBox();
    }

    public void setAABB(Vector3f min, Vector3f max){
        boundingBox.setMin(min);
        boundingBox.setMax(max);
    }

    public BoundingBox getBoundingBox(){
        return boundingBox;
    }

    public void add(float[] vertices, float[] texCoords, float[] normals, int[] indices) {
        this.texCoords = texCoords;
        if (isOBJ) {
            System.out.println("Something tried overriding .OBJ coords");
            return;
        }
        indicesSize = indices.length;
        GL30.glBindVertexArray(vao);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, flip(vertices), GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboTexture);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, flip(texCoords), GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbon);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, flip(normals), GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL30.glBindVertexArray(0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboi);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, flip(indices), GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        // setBox(vertices);

    }

    private float[] texCoords;
    public void print(){
        for(int i = 0; i < texCoords.length; i += 2){
            System.out.println(texCoords[i] + " : " + texCoords[i + 1]);
        }
    }

    protected void setOBJ() {
        isOBJ = true;
    }

    public void enable() {
        GL30.glBindVertexArray(vao);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboi);


    }

    public void render(Shader shader, int modelMatrix, MeshInstance object, PerspectiveCamera camera) {
//        GL11.glBindTexture(GL11.GL_TEXTURE_2D, object.getMaterial().getDiffuse());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, object.getTextureID());

        if(object.getSpecularTextureID() != 0){
            GL13.glActiveTexture(GL13.GL_TEXTURE1);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, object.getSpecularTextureID());
        }

        if (camera.isInBounds(object.getPosition().x, object.getPosition().y, object.getPosition().z)) {
            Matrix4 transformationMatrix = createTransformationMatrix(object.getPosition(), object.getRotation(), object.getScale());
            shader.loadMatrix(modelMatrix, transformationMatrix);
//            object.update();
            GL11.glDrawElements(GL11.GL_TRIANGLES, indicesSize, GL11.GL_UNSIGNED_INT, 0);
        }

    }

    private Matrix4 createTransformationMatrix(Vector3f translation, Vector3f rotation, float scale) {
        Matrix4 matrix = new Matrix4();
        matrix.setIdentity();
        Matrix4.translate(translation, matrix, matrix);
        Matrix4.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), matrix, matrix);
        Matrix4.scale(new Vector3f(scale, scale, scale), matrix, matrix);

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
        GL15.glDeleteBuffers(vbo);
        GL15.glDeleteBuffers(vboTexture);
        GL15.glDeleteBuffers(vboi);
        GL30.glDeleteVertexArrays(vao);
    }

    private static IntBuffer flip(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    private static FloatBuffer flip(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

}