package dotj;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.objects.PhysicsBody;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.bullet.util.DebugShapeFactory;
import com.jme3.math.Plane;
import com.jme3.math.Vector3f;
import com.jme3.system.NativeLibraryLoader;

import java.io.File;
import java.nio.FloatBuffer;

public class PhysicsWorld {

    private PhysicsSpace space;

    public PhysicsWorld(){
        //load bullet DLL
        File bulletDLL = new File(System.getProperty("user.dir") + File.separator + "bulletjme");
        NativeLibraryLoader.loadLibbulletjme(true, bulletDLL, "Release", "Sp");

        PhysicsSpace.BroadphaseType bPhase = PhysicsSpace.BroadphaseType.DBVT;
        space = new PhysicsSpace(bPhase);

        //Create a plane
        float planeY = -1;
        Plane plane = new Plane(Vector3f.UNIT_Y, planeY);
        CollisionShape planeShape = new PlaneCollisionShape(plane);
        float mass = PhysicsBody.massForStatic;
        PhysicsRigidBody floor = new PhysicsRigidBody(planeShape, mass);
        space.addCollisionObject(floor);


        //create a sphere
        float radius = 0.3f;
        CollisionShape ballShape = new SphereCollisionShape(radius);
        mass = 1f;
        PhysicsRigidBody ball = new PhysicsRigidBody(ballShape, mass);
        space.addCollisionObject(ball);


        Vector3f location = new Vector3f();
        //get the location from a Rigid body and store it in a vector3f
        ball.getPhysicsLocation(location);

    }

    public void step(){
        float timeStep = 0.02f;
        space.update(timeStep, 0);
    }
}
