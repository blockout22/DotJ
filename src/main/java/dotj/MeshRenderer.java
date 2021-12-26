package dotj;

import dotj.gameobjects.components.MeshInstance;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.*;

/**
 * not currently setup
 */
public class MeshRenderer {

    private Mesh mesh;

    public void enable(Mesh mesh){
        this.mesh = mesh;
        GL30.glBindVertexArray(mesh.getVao());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getVboi());
    }

    public void render(Shader shader, int modelMatrix, MeshInstance object){
        //        GL11.glBindTexture(GL11.GL_TEXTURE_2D, object.getMaterial().getDiffuse());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, object.getTextureID());

        if(object.getSpecularTextureID() != 0){
            GL13.glActiveTexture(GL13.GL_TEXTURE1);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, object.getSpecularTextureID());
        }

//        if (camera.isInBounds(object.getWorldPosition().x, object.getWorldPosition().y, object.getWorldPosition().z)) {
            Matrix4f transformationMatrix = createTransformationMatrix(object.getWorldTransform().getPosition(), object.getWorldTransform().getRotation(), object.getWorldTransform().getScale());
            shader.loadMatrix4f(modelMatrix, transformationMatrix);
//            object.update();
            GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndicesSize(), GL11.GL_UNSIGNED_INT, 0);
//        }
    }

    private Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale) {
//        Matrix4 matrix = new Matrix4();
//        matrix.setIdentity();
//        Matrix4.translate(translation, matrix, matrix);
//        Matrix4.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), matrix, matrix);
//        Matrix4.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), matrix, matrix);
//        Matrix4.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), matrix, matrix);
//        Matrix4.scale(scale, matrix, matrix);

        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.rotateX((float)Math.toRadians(rotation.x), matrix);
        matrix.rotateY((float)Math.toRadians(rotation.y), matrix);
        matrix.rotateZ((float)Math.toRadians(rotation.z), matrix);
        matrix.translate(translation, matrix);
        matrix.scale(scale, matrix);

        return matrix;
    }

    public void disable(){
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
        mesh = null;
    }
}
