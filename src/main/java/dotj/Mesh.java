package dotj;

import dotj.gameobjects.components.MeshInstance;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL33.*;

public class Mesh {


    private Model model;
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

    public Mesh(Model model) {
        this.model = model;
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        vboTexture = glGenBuffers();
        fbo = glGenBuffers();
        vbon = glGenBuffers();
        vboi = glGenBuffers();
        this.boundingBox = new BoundingBox();
        this.material = new Material();

        add(model.getVertices(), model.getTexCoords(), model.getNormals(), model.getIndices());
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

    private void add(float[] vertices, float[] texCoords, float[] normals, int[] indices) {
//        this.texCoords = texCoords;
        if (isModel) {
            System.out.println("Something tried overriding model coords");
            return;
        }
        indicesSize = indices.length;
        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, Utilities.flip(vertices), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vboTexture);
        glBufferData(GL_ARRAY_BUFFER, Utilities.flip(texCoords), GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vbon);
        glBufferData(GL_ARRAY_BUFFER, Utilities.flip(normals), GL_STATIC_DRAW);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);


        glBindVertexArray(0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboi);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Utilities.flip(indices), GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        // setBox(vertices);
        calculateBoundingBox(vertices);
    }

//    private float[] texCoords;
//    public void print(){
//        for(int i = 0; i < texCoords.length; i += 2){
//            System.out.println(texCoords[i] + " : " + texCoords[i + 1]);
//        }
//    }

    protected void setIsModel() {
        isModel = true;
    }

    public void enable() {
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboi);
    }

    public void render(int modelMatrix, MeshInstance object, PerspectiveCamera camera) {
//        GL11.glBindTexture(GL11.GL_TEXTURE_2D, object.getMaterial().getDiffuse());
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, object.getTextureID());

        if(object.getSpecularTextureID() != 0){
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, object.getSpecularTextureID());
        }

        if (camera.isInBounds(object.getWorldTransform().getPosition().x, object.getWorldTransform().getPosition().y, object.getWorldTransform().getPosition().z)) {
            Matrix4f transformationMatrix = Utilities.createTransformationMatrix(object.getWorldTransform().getPosition(), object.getWorldTransform().getRotation(), object.getWorldTransform().getScale(), object.getParent() == null ? null :object.getParent().getTransform().getPosition());
            Shader.loadMatrix4f(modelMatrix, transformationMatrix);
//            object.update();
            glDrawElements(GL_TRIANGLES, indicesSize, GL_UNSIGNED_INT, 0);
        }
    }

    public void disable() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glBindVertexArray(0);
    }

    public void cleanup() {
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glDeleteBuffers(vbo);
        glDeleteBuffers(vboTexture);
        glDeleteBuffers(fbo);
        glDeleteBuffers(vboi);
        glDeleteVertexArrays(vao);
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