package de.macbury.botlogic.core.levels;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.UBJsonReader;
import de.macbury.botlogic.core.controller.GameController;
import de.macbury.botlogic.core.entites.Entity;
import de.macbury.botlogic.core.entites.RobotEntity;
import de.macbury.botlogic.core.graphics.camera.RTSCameraController;
import de.macbury.botlogic.core.levels.file.LevelFile;
import de.macbury.botlogic.core.map.Map;
import de.macbury.botlogic.core.runtime.ScriptRunner;

import java.util.ArrayList;

public abstract class BaseLevel implements Screen {
  public TweenManager tweenManager;
  public Music music;
  public RobotEntity robot;
  private GameController controller;
  private Model robotModel;
  private Map map;
  private RTSCameraController cameraController;
  private Environment environment;
  private ModelBatch modelBatch;
  private PerspectiveCamera perspectiveCamera;
  private ArrayList<Entity> entities;
  private int speed;

  public BaseLevel(String mapName) {
    LevelFile levelDefinition = null;

    this.tweenManager       = new TweenManager();
    this.entities           = new ArrayList<Entity>();
    this.perspectiveCamera  = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    try {
      levelDefinition = LevelFile.load(mapName);
    } catch (Exception e) {
      throw new GdxRuntimeException(e);
    }

    this.map                = new Map(levelDefinition.geometryFile());
    this.music              = Gdx.audio.newMusic(Gdx.files.internal("audio/code_testing.mp3"));
    music.setLooping(true);

    perspectiveCamera.position.set(0, 0, 0);
    perspectiveCamera.near = 0.1f;
    perspectiveCamera.far = 300f;
    perspectiveCamera.update(true);

    this.cameraController = new RTSCameraController();
    cameraController.setCamera(perspectiveCamera);
    cameraController.setCenter(0, 0);
    cameraController.setEnabled(true);
    Gdx.input.setInputProcessor(cameraController);

    environment = new Environment();
    environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 1f));

    G3dModelLoader modelLoader = new G3dModelLoader(new UBJsonReader());
    this.robotModel            = modelLoader.loadModel(Gdx.files.getFileHandle("models/robot.g3db", Files.FileType.Internal));

    this.robot = new RobotEntity(robotModel);
    this.entities.add(robot);

    modelBatch      = new ModelBatch();
    map.environment = environment;
    this.controller = new GameController(this);
    reset();

    environment.add(new PointLight().set(new Color(0.9f,0.1f, 0.1f, 1f), map.getRobotStartPosition().cpy().add(0,1,0), 5));
  }

  public GameController getController() {
    return controller;
  }

  public void reset() {
    robot.position.set(map.getRobotStartPosition());
    cameraController.setCenter(robot.position.x, robot.position.z);
  }

  public RTSCameraController get3DCameraController() {
    return cameraController;
  }

  @Override
  public void render(float delta) {
    float nd = delta * speed;
    tweenManager.update(nd);
    controller.update(nd);
    cameraController.update(delta);
    perspectiveCamera.update();

    for (Entity entity : entities) {
      entity.update(nd);
    }

    modelBatch.begin(perspectiveCamera);
      modelBatch.render(map);
      for (Entity entity : entities) {
        entity.render(modelBatch, environment);
      }
    modelBatch.end();
  }

  @Override
  public void resize(int width, int height) {
    perspectiveCamera.viewportWidth = width;
    perspectiveCamera.viewportHeight = height;
    perspectiveCamera.update(true);
  }

  @Override
  public void show() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void dispose() {
    modelBatch.dispose();
    controller.dispose();
    map.dispose();
    robotModel.dispose();

    for (Entity e : entities) {
      e.dispose();
    }
  }

  public abstract boolean winCondition();


  public void setSpeed(int speed) {
    this.speed = speed;
  }

  public int getSpeed() {
    return speed;
  }
}
