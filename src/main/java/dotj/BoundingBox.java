package dotj;

import org.joml.Vector3f;

public class BoundingBox {

    public Vector3f min;
    public Vector3f max;

    public BoundingBox() {
        this(new Vector3f(), new Vector3f());
    }

    public BoundingBox(Vector3f min, Vector3f max) {
        this.min = min;
        this.max = max;
    }

    public Vector3f getMin() {
        return min;
    }

    public void setMin(Vector3f min) {
        this.min = min;
    }

    public Vector3f getMax() {
        return max;
    }

    public void setMax(Vector3f max) {
        this.max = max;
    }
}
