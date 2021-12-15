package dotj;

import org.lwjgl.assimp.*;

import java.io.File;

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

        if(scene == null){
            throw new IllegalStateException("AIScene is null");
        }

        AIMesh aiMesh = AIMesh.create(scene.mMeshes().get(0));

        float[] vertices = new float[aiMesh.mNumVertices() * 3];
        int[] indices = new int[aiMesh.mNumFaces() * 3];
        float[] normals = new float[aiMesh.mNormals().capacity() * 3];
        float[] texCoods = new float[4];

        int verticesIndex = 0;
        for(int i = 0; i < aiMesh.mNumVertices(); i++){
            AIVector3D position = aiMesh.mVertices().get(i);
            vertices[verticesIndex++] = position.x();
            vertices[verticesIndex++] = position.y();
            vertices[verticesIndex++] = position.z();
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

        Mesh mesh = new Mesh();
        mesh.add(vertices, texCoods, normals, indices);
        return mesh;
    }
}
