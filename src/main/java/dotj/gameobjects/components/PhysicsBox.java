package dotj.gameobjects.components;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Quaternion;
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
//        Quaternion rot = new Quaternion();
//        rot.fromAngleNormalAxis(180, new Vector3f(1, 0, 0));
//        body.setPhysicsRotation(rot);
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

    public org.joml.Vector3f getRotation()
    {
        Quaternion res = new Quaternion();
        res = body.getPhysicsRotation(res);
        return toEuler(res);
    }

    public org.joml.Vector3f getScale(){
        Vector3f res = new Vector3f();
        body.setPhysicsScale(res);
        return new org.joml.Vector3f(res.x, res.y, res.z);
    }

    private org.joml.Vector3f toEuler(Quaternion q){
        float x = 0;
        float y = 0;
        float z = 0;

        float sinr_cosp = 2 *(q.getW() * q.getX() + q.getY() * q.getZ());
        double cosr_cosp = 1 - 2 * (q.getX() * q.getX() + q.getY() *q.getY());
        z = (float) Math.atan2(sinr_cosp, cosr_cosp);

        float sinp = 2 * (q.getW() * q.getY() - q.getZ() * q.getX());
        if(Math.abs(sinp) >= 1){
            y = (float) Math.copySign(Math.PI / 2, sinp);
        }else{
            y = (float) Math.asin(sinp);
        }

        double siny_cosp = 2 * (q.getW() * q.getZ() + q.getX() * q.getY());
        double cosy_cosp = 1 - 2 * (q.getY() * q.getY() + q.getZ() * q.getZ());
        x = (float) Math.atan2(siny_cosp, cosy_cosp);

        return new org.joml.Vector3f(x, y, z);
    }

    public void execute() {

    }
}
