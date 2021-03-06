package dotj;

import java.io.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class Shader {

    private int program;
    private int vertex;
    private int fragment;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(4 * 4);

    private ArrayList<String> uniformsDetection = new ArrayList<String>();

    private HashMap<String, Integer> cachedUniforms = new HashMap<>();

    private boolean shouldValidateUniform = false;

    public Shader(String vertexShader, String fragmentShader){
        vertex = loadShader(vertexShader, GL20.GL_VERTEX_SHADER);
        fragment = loadShader(fragmentShader, GL20.GL_FRAGMENT_SHADER);

        createProgram();
        //validateAllUniforms();
    }

    public void validateAllUniforms() {
        shouldValidateUniform = true;
    }

    private void createProgram()
    {
        program = GL20.glCreateProgram();
        GL20.glAttachShader(program, vertex);
        GL20.glAttachShader(program, fragment);
    }

    public int getUnform(String uniformName){
        int value = 0;
        if(!cachedUniforms.isEmpty()) {
            value = cachedUniforms.get(uniformName);
        }
        if(value == 0) {
            int uniform = getUniformLocation(uniformName);
            if (uniform != 0) {
                cachedUniforms.put(uniformName, uniform);
            }
        }

        return value;
    }

    public void bindAttribLocation(int index, String name){
        GL20.glBindAttribLocation(program, index, name);
    }

    public void linkAndValidate()
    {
        GL20.glLinkProgram(program);
        GL20.glValidateProgram(program);
    }

    public void bind()
    {
        GL20.glUseProgram(program);
    }

    public static void unbind()
    {
        GL20.glUseProgram(0);
    }


    public static void loadMatrix4f(int location, Matrix4f matrix){
        FloatBuffer buffer = Utilities.toFlippedFloatBuffer(matrix);
        GL20.glUniformMatrix4fv(location, false, buffer);
    }

    public void loadVector2f(int location, Vector2f vector2f){
        loadVector2f(location, vector2f.x, vector2f.y);
    }

    public void loadVector2f(int location, float x, float y){
        GL20.glUniform2f(location, x, y);
    }

    public void loadVector3f(int location, Vector3f vector3f) {
        loadVector3f(location, vector3f.x, vector3f.y, vector3f.z);
    }

    public void loadVector3f(String location, Vector3f vector3f){
        int loc = getUniformLocation(location);
        loadVector3f(loc, vector3f);
    }

    public void loadVector4f(int location, Vector4f vector4f){
        GL20.glUniform4f(location, vector4f.x, vector4f.y, vector4f.z, vector4f.w);
    }

    public void loadVector3f(int location, float x, float y, float z){
        GL20.glUniform3f(location, x, y, z);
    }

    protected void loadBoolean(int location, boolean value){
        float toLoad = 0;
        if(value){
            toLoad = 1;
        }
        GL20.glUniform1f(location, toLoad);
    }

    public void loadFloat(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    public void loadInt(int location, int value){
        GL20.glUniform1i(location, value);
    }

    public int getUniformLocation(String uniform) {
        if (shouldValidateUniform) {
            for (String s : uniformsDetection) {
                if (s.equals(uniform)) {
                    return GL20.glGetUniformLocation(program, uniform);
                }
            }

            System.err.println("Uniform not found: " + uniform);
        }
        return GL20.glGetUniformLocation(program, uniform);
    }

    private int loadShader(String fileName, int type){
        StringBuilder sb = new StringBuilder();

        try {
            InputStream in = null;
            BufferedReader  br = null;
            File file = new File(Utilities.getAssetDir() + fileName);
            if(!file.exists()){
                file = new File(Utilities.getShaderDir() + fileName);
            }
            if(!file.exists()) {
                in = Shader.class.getResourceAsStream("/" + fileName);
                br = new BufferedReader(new InputStreamReader(in));
            }else{
                br = new BufferedReader(new FileReader(file));
            }


            String line;
            String previousString = "";
            String lastString = "";
            while((line = br.readLine()) != null){
                for(String s : line.split("\\s+")){
                    for(String s2 : s.split(";")){
                        if(lastString.equals("uniform")){
                            String uniform = s2.replace(" ", "").replace(";", "");
                            uniformsDetection.add(uniform);
                        }

                        lastString = previousString;
                        previousString = s2;
                    }
                }
                sb.append(line).append("\n");
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//		System.out.println("Shader: " + type);
//		System.out.println(sb);
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, sb);
        GL20.glCompileShader(shaderID);

        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
            System.err.println(GL20.glGetShaderInfoLog(shaderID));
            String error = "Shader.class\n" + GL20.glGetShaderInfoLog(shaderID);
            System.err.println(error);
//			System.exit(-1);
        }

        return shaderID;
    }

    public Matrix4f createViewMatrix(PerspectiveCamera camera)
    {
        Matrix4f vm = new Matrix4f();
        vm.identity();

        vm.rotateX((float)Math.toRadians(camera.getPitch()), vm);
        vm.rotateY((float)Math.toRadians(camera.getYaw()), vm);
        vm.rotateZ((float)Math.toRadians(camera.getRoll()), vm);
        Vector3f camPos = camera.getPosition();
        Vector3f negCamPos = new Vector3f(-camPos.x, -camPos.y, - camPos.z);
        vm.translate(negCamPos, vm);
        return vm;
    }

    public void cleanup() {
        unbind();
        GL20.glDetachShader(program, vertex);
        GL20.glDetachShader(program, fragment);
        GL20.glDeleteShader(vertex);
        GL20.glDeleteShader(fragment);
        GL20.glDeleteProgram(program);
    }

}