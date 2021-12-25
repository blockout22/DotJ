package dotj.levels;

import dotj.PerspectiveCamera;
import dotj.gameobjects.Cube;
import dotj.gameobjects.Floor;
import dotj.gameobjects.Monkey;
import dotj.gameobjects.Sphere;
import dotj.interfaces.OnFinishedListener;
import dotj.physics.PhysicsWorld;
import dotj.shaders.WorldShader;

public class TestLevel extends Level {

    private Floor floor;
    private Monkey monkey;
    private Sphere sphere;
    private Cube cube;

    public TestLevel(PhysicsWorld physicsWorld, PerspectiveCamera camera, WorldShader shader){
        super(physicsWorld, camera, shader);
        floor = new Floor(physicsWorld, camera, shader);
        addGameObject(floor);
        monkey = new Monkey(camera, shader);
        addGameObject(monkey);
        sphere = new Sphere(camera, shader, physicsWorld);
        addGameObject(sphere);
        cube = new Cube(camera, shader, physicsWorld);
        addGameObject(cube);

        setOnLevelLoaded(new OnFinishedListener() {
            @Override
            public void finished() {
                System.out.println("Finished Loading Test Level");
            }
        });
    }
}
