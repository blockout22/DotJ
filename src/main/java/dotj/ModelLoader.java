package dotj;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;

public class ModelLoader {

    public static Model load(String fileName){
        return load(new File(Utilities.getModelDir() + fileName));
    }

    public static Model load(File file){
        AIScene scene = aiImportFile(file.toString(),
                aiProcess_Triangulate |
                        aiProcess_GenSmoothNormals |
                        aiProcess_FlipUVs |
                        aiProcess_CalcTangentSpace |
                        aiProcess_LimitBoneWeights );

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

        Model model = new Model(vertices, texCoods, normals, indices);

//        Mesh mesh = new Mesh();
//        mesh.add(vertices, texCoods, normals, indices);
//        mesh.setAABB(new Vector3f(aabb.mMin().x(), aabb.mMin().y(), aabb.mMin().z()), new Vector3f(aabb.mMax().x(), aabb.mMax().y(), aabb.mMax().z()));
//        mesh.setIsModel();
//        scene.free();
//        buff.free();
//        aiMesh.free();
//        aabb.free();
        return model;
    }

    public static Model[] loadModel(File file){
        AIScene aiScene = Assimp.aiImportFile(file.toString(),
                Assimp.aiProcess_Triangulate |
                        Assimp.aiProcess_GenSmoothNormals |
                        Assimp.aiProcess_FlipUVs |
                        Assimp.aiProcess_CalcTangentSpace |
                        Assimp.aiProcess_LimitBoneWeights);

        int numMaterial = aiScene.mNumMaterials();
        PointerBuffer aiMaterials = aiScene.mMaterials();

        List<Material> materials = new ArrayList<>();

        for(int i = 0; i < numMaterial; i++){
            AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
            processMaterial(aiMaterial, materials);
        }

        int numMeshes = aiScene.mNumMeshes();
        PointerBuffer aiMeshes = aiScene.mMeshes();
        Model[] models = new Model[numMeshes];
        for (int i = 0; i < numMeshes; i++) {
            AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
            Model mesh = processMesh(aiMesh, materials);
            models[i] = mesh;
        }

        return models;
    }

    /**
     * TODO process materials correctly
     */
    private static void processMaterial(AIMaterial aiMaterial, List<Material> materials) {
        AIColor4D color = AIColor4D.create();

        AIString path = AIString.calloc();
        Assimp.aiGetMaterialTexture(aiMaterial, aiTextureType_DIFFUSE, 0, path, (IntBuffer) null, null, null, null, null, null);
        String textPath = path.dataString();
        Texture texture = null;
        if (textPath != null && textPath.length() > 0) {
//            TextureCache textCache = TextureCache.getInstance();
//            texture = textcache.getTexture(texturesDir + "/" + textPath);
        }

//        Vector4f ambient = Material.DEFAULT_COLOR;
//        int result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_AMBIENT, aiTextureType_NONE, 0, color);
//        if(result == 0){
//            ambient = new Vector4f(color.r(), color.g(), color.b(), color.a());
//        }

//        Vector4f diffuse = Material.DEFAULT_COLOUR;
//        result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, colour);
//        if (result == 0) {
//            diffuse = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
//        }
//
//        Vector4f specular = Material.DEFAULT_COLOUR;
//        result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0, colour);
//        if (result == 0) {
//            specular = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
//        }
//
//        Material material = new Material(ambient, diffuse, specular, 1.0f);
//        material.setTexture(texture);
//        materials.add(material);
    }

    private static Model processMesh(AIMesh aiMesh, List<Material> materials){
        List<Float> vertices = new ArrayList<>();
        List<Float> textures = new ArrayList<>();
        List<Float> normals  = new ArrayList<>();
        List<Integer> indices   = new ArrayList<>();

        processVertices(aiMesh, vertices);
        processNormals(aiMesh, normals);
        processTextCoords(aiMesh, textures);
        processIndices(aiMesh, indices);

//        Mesh mesh = new Mesh();
        Model model = new Model(Utilities.toFloatArray(vertices), Utilities.toFloatArray(textures), Utilities.toFloatArray(normals), Utilities.toIntArray(indices));

        Material material;
        int materialIdx = aiMesh.mMaterialIndex();
        if(materialIdx >= 0 && materialIdx < materials.size()){
            material = materials.get(materialIdx);
        }else{
            material = new Material();
        }

        //TODO add material to models
//        mesh.setMaterial(material);
        return model;
    }

    private static void processVertices(AIMesh aiMesh, List<Float> vertices) {
        AIVector3D.Buffer aiVertices = aiMesh.mVertices();

        while(aiVertices.remaining() > 0){
            AIVector3D aiVertex = aiVertices.get();
            vertices.add(aiVertex.x());
            vertices.add(aiVertex.y());
            vertices.add(aiVertex.z());
        }
    }

    private static void processNormals(AIMesh aiMesh, List<Float> normals) {
        AIVector3D.Buffer aiNormals = aiMesh.mNormals();

        while(aiNormals.remaining() > 0){
            AIVector3D aiNormal = aiNormals.get();
            normals.add(aiNormal.x());
            normals.add(aiNormal.y());
            normals.add(aiNormal.z());
        }
    }

    private static void processTextCoords(AIMesh aiMesh, List<Float> textures) {
        AIVector3D.Buffer aiTextureCoords = aiMesh.mTextureCoords(0);
//        System.out.println(aiTextureCoords.capacity());
        for(int i = 0; i < aiTextureCoords.capacity(); i++){
            textures.add(aiTextureCoords.get(i).x());
            textures.add(aiTextureCoords.get(i).y());
        }
    }

    private static void processIndices(AIMesh aiMesh, List<Integer> indices) {
        AIFace.Buffer aiFace = aiMesh.mFaces();

        while(aiFace.remaining() > 0){
            AIFace face = aiFace.get();
            IntBuffer indicesBuffer = face.mIndices();

            for (int i = 0; i < indicesBuffer.capacity(); i++) {
                indices.add(indicesBuffer.get(i));
            }
        }
    }
}
