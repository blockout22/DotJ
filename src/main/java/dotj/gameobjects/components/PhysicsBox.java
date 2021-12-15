package dotj.gameobjects.components;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Vector3f;
import dotj.physics.PhysicsWorld;

public class PhysicsBox implements Component {
    private PhysicsRigidBody body;

    public PhysicsBox(PhysicsWorld physicsWorld, float xHalfExtent, float yHalfExtent, float zHalfExtent, float mass){
        CollisionShape shape = new BoxCollisionShape(xHalfExtent, yHalfExtent, zHalfExtent);
//        float mass = PhysicsBody.massForStatic;
        body = new PhysicsRigidBody(shape, mass);
        physicsWorld.add(body);
    }

    public void setMass(){

    }

    public org.joml.Vector3f getPosition(){
        Vector3f res = new Vector3f();
        body.getPhysicsLocation(res);
        return new org.joml.Vector3f(res.x, res.y, res.z);
    }

    public void execute() {

    }
}
