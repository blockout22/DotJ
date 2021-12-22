package dotj.debug;

import dotj.Matrix4;
import dotj.PerspectiveCamera;
import dotj.Shader;
import dotj.Utilities;
import dotj.shaders.WorldShader;
import org.joml.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL30.*;

public class DebugRender {

//    private int vao;
//    private int vbo;
//    private int vboi;

    private static  ArrayList<DebugInstance> instances = new ArrayList<>();
    private static ArrayList<Integer> vaos = new ArrayList<Integer>();
    private static ArrayList<Integer> vbos = new ArrayList<Integer>();
    private static ArrayList<Integer> vbois = new ArrayList<Integer>();

    public DebugRender(){

    }

    public static DebugInstance add(Vector3f start, Vector3f end){

        DebugInstance instance = new DebugInstance();
        int vao = glGenVertexArrays();
        int vbo = glGenBuffers();
        int vboi = glGenBuffers();

        instances.add(instance);
        vaos.add(vao);
        vbos.add(vbo);
        vbois.add(vboi);
        float[] verts = {
                start.x, start.y, start.z,
                end.x, end.y, end.z
        };

        int[] indices = {
                0, 1
        };

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, Utilities.flip(verts), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);


        glBindVertexArray(0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboi);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Utilities.flip(indices), GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        return instance;
    }

    public static DebugInstance[] addCubeRender(Vector3f min, Vector3f max){

        int i = 0;
        DebugInstance[] instanceList = new DebugInstance[12];

        //front face
        instanceList[i++] = DebugRender.add(new Vector3f(min.x, max.y, min.z), new Vector3f(max.x, max.y, min.z));
        instanceList[i++] = DebugRender.add(new Vector3f(max.x, max.y, min.z), new Vector3f(max.x, min.y, min.z));
        instanceList[i++] = DebugRender.add(new Vector3f(max.x, min.y, min.z), new Vector3f(min.x, min.y, min.z));
        instanceList[i++] = DebugRender.add(new Vector3f(min.x, min.y, min.z), new Vector3f(min.x, max.y, min.z));

        //back face
        instanceList[i++] = DebugRender.add(new Vector3f(min.x, max.y, max.z), new Vector3f(max.x, max.y, max.z));
        instanceList[i++] = DebugRender.add(new Vector3f(max.x, max.y, max.z), new Vector3f(max.x, min.y, max.z));
        instanceList[i++] = DebugRender.add(new Vector3f(max.x, min.y, max.z), new Vector3f(min.x, min.y, max.z));
        instanceList[i++] = DebugRender.add(new Vector3f(min.x, min.y, max.z), new Vector3f(min.x, max.y, max.z));

        //sides
        instanceList[i++] = DebugRender.add(new Vector3f(max.x, max.y, min.z), new Vector3f(max.x, max.y, max.z));
        instanceList[i++] = DebugRender.add(new Vector3f(max.x, min.y, min.z), new Vector3f(max.x, min.y, max.z));
        instanceList[i++] = DebugRender.add(new Vector3f(min.x, max.y, min.z), new Vector3f(min.x, max.y, max.z));
        instanceList[i++] = DebugRender.add(new Vector3f(min.x, min.y, min.z), new Vector3f(min.x, min.y, max.z));

        return instanceList;
    }

    public static void render(WorldShader shader, PerspectiveCamera camera){
        for(int i = 0; i < vaos.size(); i++){
            glDisable(GL_DEPTH_TEST);
            glBindVertexArray(vaos.get(i));
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbois.get(i));

            {
                Vector3f position = new Vector3f();
                Vector3f rotation = new Vector3f();
                Vector3f scale = new Vector3f(1f, 1f, 1f);
                Matrix4 transformationMatrix = createTransformationMatrix(instances.get(i).position, instances.get(i).rotation, instances.get(i).scale);
                Shader.loadMatrix(shader.getModelMatrix(), transformationMatrix);
                shader.setColor(instances.get(i).getColor());

                glLineWidth(2);
                glDrawElements(GL_LINES, 2, GL_UNSIGNED_INT, 0);
            }
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(2);
            glBindVertexArray(0);
            glEnable(GL_DEPTH_TEST);
        }
    }

    private static Matrix4 createTransformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale) {
        Matrix4 matrix = new Matrix4();
        matrix.setIdentity();
        Matrix4.translate(translation, matrix, matrix);
        Matrix4.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), matrix, matrix);
        Matrix4.scale(scale, matrix, matrix);

        return matrix;
    }

    public static void cleanup(){
        for(int i = 0; i < vaos.size(); i++){
            glDeleteVertexArrays(vaos.get(i));
        }
        for(int i = 0; i < vbos.size(); i++){
            glDeleteBuffers(vbos.get(i));
        }
        for(int i = 0; i < vbois.size(); i++){
            glDeleteBuffers(vbois.get(i));
        }
    }
}
