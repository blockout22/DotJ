package dotj.gameobjects.components;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import dotj.physics.PhysicsWorld;

public class PhysicsBox implements Component {
    private PhysicsRigidBody body;
    private Transform transform = new Transform();

    public PhysicsBox(PhysicsWorld physicsWorld, float xHalfExtent, float yHalfExtent, float zHalfExtent, float mass){
        CollisionShape shape = new BoxCollisionShape(xHalfExtent, yHalfExtent, zHalfExtent);
//        float mass = PhysicsBody.massForStatic;
        body = new PhysicsRigidBody(shape, mass);
        physicsWorld.add(body);
    }

    public void setMass(){

    }

    public BoundingBox getBoundingBox(){
        BoundingBox results = new BoundingBox();
        results = body.boundingBox(results);
        return results;
    }

    public void setPosition(float x, float y, float z){
        body.getTransform(transform);
        transform.setTranslation(new Vector3f(x, y, z));
        body.setPhysicsTransform(transform);
    }

    public org.joml.Vector3f getPosition(org.joml.Vector3f store){
        body.getTransform(transform);
        store.x = transform.getTranslation().x;
        store.y = transform.getTranslation().y;
        store.z = transform.getTranslation().z;
        return store;
    }

    public dotj.Transform getTransform(dotj.Transform store){
        transform = body.getTransform(transform);
        store.setPosition(transform.getTranslation().x, transform.getTranslation().y, transform.getTranslation().z);
        store.setRotation(transform.getRotation().getX(), transform.getRotation().getY(), transform.getRotation().getZ());
        store.setScale(transform.getScale().x, transform.getScale().y, transform.getScale().z);
        return store;
    }

    public org.joml.Vector3f getPosition(){
        Vector3f res = new Vector3f();
        body.getPhysicsLocation(res);
        return new org.joml.Vector3f(res.x, res.y, res.z);
    }

    public void execute() {

    }
}
