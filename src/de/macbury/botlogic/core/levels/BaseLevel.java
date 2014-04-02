package de.macbury.botlogic.core.levels;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.UBJsonReader;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.controller.GameController;
import de.macbury.botlogic.core.entites.*;
import de.macbury.botlogic.core.graphics.camera.RTSCameraController;
import de.macbury.botlogic.core.levels.file.LevelFile;
import de.macbury.botlogic.core.map.Map;
import de.macbury.botlogic.core.runtime.ScriptRunner;
import de.macbury.botlogic.core.tween.ModelEntityAccessor;

import java.util.ArrayList;

public abstract class BaseLevel implements Screen {
  private DecalBatch decalBatch;
  private LevelFile levelDefinition;
  public TweenManager tweenManager;
  public RobotEntity robot;
  private GameController controller;
  private Model robotModel;
  private Map map;
  private RTSCameraController cameraController;
  private Environment environment;
  private ModelBatch modelBatch;
  private PerspectiveCamera perspectiveCamera;
  private ArrayList<Entity> entities;
  private int speed = 1;

  public BaseLevel(String mapName) {
    this.tweenManager       = new TweenManager();

    this.entities           = new ArrayList<Entity>();
    this.perspectiveCamera  = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    try {
      levelDefinition = LevelFile.load(mapName);
    } catch (Exception e) {
      throw new GdxRuntimeException(e);
    }

    this.map                = new Map(levelDefinition.geometryFile());
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

    /*for (Material material : robotModel.materials) {

     // for led
      Gdx.app.log("TEST", material.id);
      if (material.id.contains("Led")) {
        /*ColorAttribute attr = (ColorAttribute)material.get(ColorAttribute.Diffuse);
        attr.color.set(Color.BLUE);

        BlendingAttribute ba = (BlendingAttribute)material.get(BlendingAttribute.Type);
        ba.opacity = 1.0f;
        ba.sourceFunction = GL20.GL_ONE;
        ba.destFunction = GL20.GL_SRC_ALPHA;
        //attr = (ColorAttribute)material.get(ColorAttribute.Emissive);
        //attr.color.set(Color.WHITE);


      }
    }*/
    //TextureAttribute attr = (TextureAttribute) robotModel.materials.first().get(TextureAttribute.Diffuse);
    //attr.textureDescription.minFilter = Texture.TextureFilter.Nearest;
    //attr.textureDescription.magFilter = Texture.TextureFilter.Nearest;

    this.robot = new RobotEntity(robotModel);
    this.entities.add(robot);

    for(MeshPart part : robotModel.meshParts) {
      Gdx.app.log("TAG", part.id);
    }

    modelBatch      = new ModelBatch();
    decalBatch      = new DecalBatch();
    decalBatch.setGroupStrategy(new CameraGroupStrategy(perspectiveCamera));

    map.environment = environment;
    this.controller = new GameController(this);
    reset();

    environment.add(new PointLight().set(new Color(1f,1f, 1f, 1f), map.getRobotStartPosition().cpy().add(0,1,0), 1.5f));

    BotLogic.audio.music.play();
  }

  public GameController getController() {
    return controller;
  }

  public void reset() {
    tweenManager.killAll();
    robot.startPosition = map.getRobotStartPosition();

    for (Entity e : entities) {
      e.reset();
    }

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
        if (EntityModelRenderable.class.isInstance(entity)) {
          EntityModelRenderable en = (EntityModelRenderable)entity;
          en.renderModel(modelBatch, environment);
        }
      }
    modelBatch.end();

    for (Entity entity : entities) {
      if (EntityDecalRenderable.class.isInstance(entity)) {
        EntityDecalRenderable en = (EntityDecalRenderable)entity;
        en.renderBatch(decalBatch, environment);
      }
    }

    decalBatch.flush();
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
    decalBatch.dispose();
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

  public ArrayList<LevelFile.LevelFeature> getFeatures() {
    return levelDefinition.getFeatures();
  }

  public LevelFile getFile() {
    return levelDefinition;
  }
}
