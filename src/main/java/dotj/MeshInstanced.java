package dotj;

import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

public class MeshInstanced extends Mesh{

    private int instanceVBO;

    public MeshInstanced(Model model){
        super(model);
        instanceVBO = glGenBuffers();
    }

    public void add(Vector3f[] instancedPositions){
            glBindVertexArray(getVao());
            float[] positions = Utilities.toFloatArray(instancedPositions);
            glBindBuffer(GL_ARRAY_BUFFER, instanceVBO);
            glBufferData(GL_ARRAY_BUFFER, Utilities.flip(positions), GL_STATIC_DRAW);
            glEnableVertexAttribArray(3);
            glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0);
            glVertexAttribDivisor(3, 1);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            glBindVertexArray(0);
    }

    public void enable(){
        super.enable();
        glEnableVertexAttribArray(3);
    }

    public void renderInstances(int count){
        glDrawElementsInstanced(GL_TRIANGLES, getIndicesSize(), GL_UNSIGNED_INT, 0, count);
    }

    public void disable(){
        glDisableVertexAttribArray(3);
        super.disable();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        glDisableVertexAttribArray(3);
        glDeleteBuffers(instanceVBO);
    }
}
