package dotj.example;

import dotj.*;
import dotj.gameobjects.components.MeshInstance;
import dotj.shaders.ReflectShader;

import java.io.File;

public class ReflectionExample {

    private ReflectShader reflectShader;
    private Mesh ReflectMesh;
    private MeshInstance ReflectMeshInstance;

    public ReflectionExample(){
        reflectShader = new ReflectShader();
//        ReflectMesh = ModelLoader.load("monkey.fbx");
        Model model = ModelLoader.loadModel(new File(Utilities.getModelDir() + "Backpack.fbx"))[4];
        ReflectMesh = new Mesh(model);
        ReflectMeshInstance = new MeshInstance(null, ReflectMesh);
        ReflectMeshInstance.getWorldTransform().setPosition(-20, 7, -20);

        reflectShader.bind();
        reflectShader.loadInt(reflectShader.skybox, 0);
//        reflectShader.loadMatrix(reflectShader.projection, camera.getProjectionMatrix());
    }

    public void update(PerspectiveCamera camera){
        reflectShader.bind();
        reflectShader.loadMatrix(reflectShader.projection, camera.getProjectionMatrix());
        Matrix4 matrix = Utilities.createTransformationMatrix(ReflectMeshInstance.getWorldTransform().getPosition(), ReflectMeshInstance.getWorldTransform().getRotation(), ReflectMeshInstance.getWorldTransform().getScale());
        Matrix4 matrix4 = new Matrix4();
        matrix4.m00 = 1f;
        matrix4.m01 = 1f;
        matrix4.m02 = 1f;
        matrix4.m03 = 1f;
        matrix4.m10 = 1f;
        matrix4.m11 = 1f;
        matrix4.m12 = 1f;
        matrix4.m13 = 1f;
        matrix4.m20 = 1f;
        matrix4.m21 = 1f;
        matrix4.m22 = 1f;
        matrix4.m23 = 1f;
        matrix4.m30 = 1f;
        matrix4.m31 = 1f;
        matrix4.m32 = 1f;
        matrix4.m33 = 1f;
        Shader.loadMatrix(reflectShader.model, matrix);
        reflectShader.loadViewMatrix(camera);
        reflectShader.loadVector3f(reflectShader.cameraPos, camera.getPosition());
        ReflectMesh.enable();
        ReflectMesh.render(reflectShader.model, ReflectMeshInstance, camera);
        ReflectMesh.disable();
    }

    public void cleanup()
    {
        ReflectMesh.cleanup();
        reflectShader.cleanup();
    }

}
