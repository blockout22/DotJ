package dotj;

import org.joml.Vector2f;

public class Utilities {

    public static float remapFloat(float value, float inA, float inB, float outA, float outB){
//        return inB + (value - inA) * (outB - inB) / (outA - inA);
        return outA + (outB - outA) * ((value - inA) / (inB - inA));
    }

    public static Vector2f remapVector2f(Vector2f value, Vector2f inA, Vector2f inB, Vector2f outA, Vector2f outB){
        float xRes = remapFloat(value.x, inA.x, inB.x, outA.x, outB.x);
        float yRes = remapFloat(value.y, inA.y, inB.y, outA.y, outB.y);

        return new Vector2f(xRes, yRes);
    }
}
