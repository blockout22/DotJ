package dotj;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class OBJLoader {

    //TODO just delete this OBJLoader as Assimp already loads these types of files
    public static Model load(String objFile){
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(OBJLoader.class.getResourceAsStream("/" + objFile)));


            ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
            ArrayList<Vector2f> texCoords = new ArrayList<Vector2f>();
            ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
            ArrayList<Integer> indices = new ArrayList<Integer>();
            ArrayList<Integer> texIndex = new ArrayList<Integer>();
            ArrayList<Integer> normIndex = new ArrayList<Integer>();

            String line;
            while((line = br.readLine()) != null){
                if(line.startsWith("v "))
                {
                    vertices.add(new Vector3f(Float.valueOf(line.split(" ")[1]), Float.valueOf(line.split(" ")[2]), Float.valueOf(line.split(" ")[3])));
                }

                if (line.startsWith("vt ")) {
                    texCoords.add(new Vector2f(Float.valueOf(line.split(" ")[1]), Float.valueOf(line.split(" ")[2])));
                }

                if (line.startsWith("vn ")) {
                    normals.add(new Vector3f(Float.valueOf(line.split(" ")[1]), Float.valueOf(line.split(" ")[2]), Float.valueOf(line.split(" ")[3])));
                }

                if(line.startsWith("f ")){
//					Vector3f p1 = null;
//					Vector3f p2 = null;
//					Vector3f p3 = null;

                    for(int i = 1; i < line.split(" ").length; i++){
                        indices.add(Integer.valueOf(line.split(" ")[i].split("/")[0]) - 1);
                        texIndex.add(Integer.valueOf(line.split(" ")[i].split("/")[1]) - 1);
                        normIndex.add(Integer.valueOf(line.split(" ")[i].split("/")[2]) - 1);

//						if (i == 1) {
//							p1 = vertices.get(Integer.valueOf(line.split(" ")[i].split("/")[0]) - 1);
//						} else if (i == 2) {
//							p2 = vertices.get(Integer.valueOf(line.split(" ")[i].split("/")[0]) - 1);
//						} else if (i == 3) {
//							p3 = vertices.get(Integer.valueOf(line.split(" ")[i].split("/")[0]) - 1);
//						}
                    }
                }
            }

            float[] finalTexCoords = new float[vertices.size() * 2];
            float[] finalVertices = new float[vertices.size() * 3];
            float[] finalNormals = new float[vertices.size() * 3];
            int[] finalIndices = new int[indices.size()];

            int vPos = 0;
            for (int v = 0; v < vertices.size(); v++) {
                float x = vertices.get(v).x;
                float y = vertices.get(v).y;
                float z = vertices.get(v).z;
                finalVertices[vPos++] = x;
                finalVertices[vPos++] = y;
                finalVertices[vPos++] = z;
            }

            for (int i = 0; i < indices.size(); i++) {
                finalIndices[i] = indices.get(i);
                int vertex = indices.get(i);
                int texturePos = texIndex.get(i);
                int normalsPos = normIndex.get(i);
                finalTexCoords[vertex * 2] = texCoords.get(texturePos).x;
                finalTexCoords[vertex * 2 + 1] = 1 - texCoords.get(texturePos).y;

                finalNormals[vertex * 3] = normals.get(normalsPos).x;
                finalNormals[vertex * 3 + 1] = normals.get(normalsPos).y;
                finalNormals[vertex * 3 + 2] = normals.get(normalsPos).z;
            }

            Model model = new Model(finalVertices, finalTexCoords, finalNormals, finalIndices);
//            mesh.add(finalVertices, finalTexCoords, finalNormals, finalIndices);
//            mesh.setIsModel();

            return model;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}