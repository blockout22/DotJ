package dotj;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ModelLoader {

    public static Mesh load(String fileName){
        return load(new File(Utilities.getModelDir() + fileName));
    }

    public static Mesh load(File file){
        AIScene scene = Assimp.aiImportFile(file.toString(),
                Assimp.aiProcess_Triangulate |
                        Assimp.aiProcess_GenSmoothNormals |
                        Assimp.aiProcess_FlipUVs |
                        Assimp.aiProcess_CalcTangentSpace |
                        Assimp.aiProcess_LimitBoneWeights);

        AIMatrix4x4 transform = scene.mRootNode().mTransformation();
//        System.out.println(file);
//        System.out.println(transform.a1() + " : " + transform.a2() + " : " + transform.a3() + " : " + transform.a4());
//        System.out.println(transform.b1() + " : " + transform.b2() + " : " + transform.b3() + " : " + transform.b4());
//        System.out.println(transform.c1() + " : " + transform.c2() + " : " + transform.c3() + " : " + transform.c4());
//        System.out.println(transform.d1() + " : " + transform.d2() + " : " + transform.d3() + " : " + transform.d4());


        AIString.Buffer buffer = scene.mMetaData().mKeys();

        ByteBuffer b = BufferUtils.createByteBuffer(1028 + 6);
        b.put("UpAxis".getBytes());
        // 1 == UpAxisSign 0 == value inside first get (upto 9)
//        System.out.println(buffer.get(new AIString(b)).get(1).data().get(9));

        for(int i = 0; i < buffer.capacity(); i++){
            AIString byBuff = buffer.get(i);
//            System.out.println(buffer.get(byBuff).dataString());


        }

        if(scene == null){
            throw new IllegalStateException("AIScene is null");
        }

        AIMesh aiMesh = AIMesh.create(scene.mMeshes().get(0));
        AIAABB aabb = aiMesh.mAABB();
        AIVector3D.Buffer buff = aiMesh.mTextureCoords(0);

        float[] vertices = new float[aiMesh.mNumVertices() * 3];
        int[] indices = new int[aiMesh.mNumFaces() * 3];
        float[] normals = new float[aiMesh.mNormals().capacity() * 3];
        float[] texCoods = new float[buff.capacity() * 2];

        int verticesIndex = 0;
        for(int i = 0; i < aiMesh.mNumVertices(); i++){
            AIVector3D position = aiMesh.mVertices().get(i);
            vertices[verticesIndex++] = position.x();
            vertices[verticesIndex++] = position.y();
            vertices[verticesIndex++] = position.z();

            float x = aiMesh.mTextureCoords(0).get(i).x();
            float y = aiMesh.mTextureCoords(0).get(i).y();

//            System.out.println(file + " -" + "X: " + x + " Y: " + y);
        }

        int indicesIndex = 0;
        for(int i = 0; i < aiMesh.mNumFaces(); i++){
            AIFace face = aiMesh.mFaces().get(i);
            indices[indicesIndex++] = face.mIndices().get(0);
            indices[indicesIndex++] = face.mIndices().get(1);
            indices[indicesIndex++] = face.mIndices().get(2);
        }

        int normalsIndex = 0;
        for(int i = 0; i < aiMesh.mNormals().capacity(); i++){
            AIVector3D normal = aiMesh.mNormals().get(i);

            normals[normalsIndex++] = normal.x();
            normals[normalsIndex++] = normal.y();
            normals[normalsIndex++] = normal.z();
        }

        int texIndex = 0;
        for(int i = 0; i < buff.capacity(); i++){
            texCoods[texIndex++] = buff.get(i).x();
            texCoods[texIndex++] = buff.get(i).y();
        }

        Mesh mesh = new Mesh();
        mesh.add(vertices, texCoods, normals, indices);
        mesh.setAABB(new Vector3f(aabb.mMin().x(), aabb.mMin().y(), aabb.mMin().z()), new Vector3f(aabb.mMax().x(), aabb.mMax().y(), aabb.mMax().z()));

//        scene.free();
//        buff.free();
//        aiMesh.free();
//        aabb.free();
        return mesh;
    }
}
