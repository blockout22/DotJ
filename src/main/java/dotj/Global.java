package dotj;

import java.util.HashMap;

public class Global {

    public static boolean shouldClose = false;

    public static HashMap<String, Texture> texturePool = new HashMap<>();
    public static HashMap<String, Mesh> meshPool = new HashMap<>();
}
