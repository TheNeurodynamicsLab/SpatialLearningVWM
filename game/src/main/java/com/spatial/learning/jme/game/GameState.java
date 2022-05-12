package com.spatial.learning.jme.game;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

public class GameState extends BaseAppState {
    private SpatialLearningVWM app;
    public Boolean initialized;
    private Node rootNode;
    private AssetManager assetManager;
    private BulletAppState physics;
    @Override
    protected void initialize(Application app) {
        this.app = (SpatialLearningVWM) app;
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.physics = this.app.getPhysics();
        initialized=true;
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        if (initialized) {initScene();}
    }

    @Override
    protected void onDisable() {
        if (initialized) {}
    }

    public SpatialLearningVWM getVWMApplication() {
        return this.app;
    }

    public void initScene() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1,0,-2).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);

        Spatial scene = assetManager.loadModel("Models/scene.obj");
        scene.scale(2f, 2f, 2f);
        rootNode.attachChild(scene);
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(scene);
        RigidBodyControl scenePhyControl = new RigidBodyControl(sceneShape, 0f);
        this.physics.getPhysicsSpace().add(scenePhyControl);
    }

    public void initFloor() {
        float locations = this.app.getTotalLocations();
        float ground = this.app.getGround();
        Box floorMesh = new Box(new Vector3f(0f-(locations/2), ground, 0f-(locations/2)), new Vector3f(0f+(locations/2), ground-1f, 0f+(locations/2)));
        Geometry floorGeom = new Geometry("floorGeom", floorMesh);
        Material floorMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        floorMat.setTexture("floorTex", assetManager.loadTexture("Textures//floor/Floor.png"));
        floorGeom.setMaterial(floorMat);
        rootNode.attachChild(floorGeom);

        CollisionShape floorShape = CollisionShapeFactory.createMeshShape(floorGeom);
        RigidBodyControl floorPhyControl = new RigidBodyControl(floorShape, 0f);
        physics.getPhysicsSpace().add(floorPhyControl);
    }
}