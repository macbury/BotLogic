package de.macbury.botlogic.core.screens.level;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.DepthShader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.controller.GameController;
import de.macbury.botlogic.core.entites.Entity;
import de.macbury.botlogic.core.entites.EntityDecalRenderable;
import de.macbury.botlogic.core.entites.EntityModelRenderable;
import de.macbury.botlogic.core.entites.RobotEntity;
import de.macbury.botlogic.core.graphics.camera.RTSCameraController;
import de.macbury.botlogic.core.graphics.compositor.LevelCompositor;
import de.macbury.botlogic.core.screens.level.file.LevelFile;
import de.macbury.botlogic.core.screens.level.map.Block;
import de.macbury.botlogic.core.screens.level.map.Map;

import java.util.ArrayList;

public class GameLevelScreen implements Screen {

  private ModelBatch colorModelBatch;
  private ModelBatch depthModelBatch;
  private DecalBatch decalBatch;
  private RenderContext renderContext;

  private FrameBuffer colorBuffer;
  private FrameBuffer depthBuffer;
  private FrameBuffer decalBuffer;
  private LevelCompositor cellShadingCompositor;

  private LevelFile levelDefinition;
  private Map map;
  private GameController controller;
  private ArrayList<Entity> entities;
  public RobotEntity robot;

  protected PerspectiveCamera perspectiveCamera;
  protected RTSCameraController cameraController;
  public Environment environment;

  private int speed = 1;

  public TweenManager gameObjectsTweenManager;
  public TweenManager uiTweenManager;

  public GameLevelScreen(LevelFile levelDef) {
    this.gameObjectsTweenManager = new TweenManager();
    this.uiTweenManager          = new TweenManager();
    this.renderContext      = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));
    this.entities           = new ArrayList<Entity>();
    this.perspectiveCamera  = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    levelDefinition         = levelDef;

    this.map                = new Map(levelDefinition.geometryFile());
    perspectiveCamera.position.set(0, 0, 0);
    perspectiveCamera.near = 0.1f;
    perspectiveCamera.far = 50f;
    perspectiveCamera.update(true);

    this.cameraController = new RTSCameraController();
    cameraController.setCamera(perspectiveCamera);
    cameraController.setCenter(0, 0);
    cameraController.setEnabled(true);

    environment = new Environment();
    environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.3f, 0.3f, 0.3f, 1f));

    this.cellShadingCompositor = new LevelCompositor(renderContext);

    this.robot = BotLogic.entities.robot();
    this.robot.led = BotLogic.entities.led();

    addEntity(this.robot);
    addEntity(this.robot.led);

    colorModelBatch = new ModelBatch(renderContext);
    decalBatch      = new DecalBatch(new CameraGroupStrategy(perspectiveCamera));

    DepthShader.Config depthConfig     = new DepthShader.Config(Gdx.files.internal("shaders/depth.vertex").readString(), Gdx.files.internal("shaders/depth.frag").readString());
    depthConfig.defaultCullFace        = GL20.GL_BACK;
    depthConfig.depthBufferOnly        = false;
    depthModelBatch = new ModelBatch(renderContext, new DepthShaderProvider(depthConfig));

    map.environment = environment;
    this.controller = new GameController(this);
    reset();

    environment.set(new ColorAttribute(ColorAttribute.Fog, 0f, 0f, 0f, 1f));
    environment.add(new DirectionalLight().set(new Color(.4f,.4f, .4f, 0.1f), new Vector3(1,-1,0)));
    BotLogic.audio.mainMenuMusic.play();
  }

  private void addEntity(Entity e) {
    e.setLevel(this);
    this.entities.add(e);
  }

  public GameController getController() {
    return controller;
  }

  public void reset() {
    gameObjectsTweenManager.killAll();
    robot.startPosition = map.getRobotStartPosition();

    for (Entity e : entities) {
      e.reset();
    }

    cameraController.setCenter(robot.position.x, robot.position.z);
  }

  public RTSCameraController get3DCameraController() {
    return cameraController;
  }

  public void renderEntitiesForBufferAndBatch(FrameBuffer buffer, ModelBatch batch, boolean renderDecals) {
    buffer.begin();
      Gdx.gl.glClearColor(0, 0, 0, 0);
      Gdx.gl.glViewport(0, 0, buffer.getWidth(), buffer.getHeight());
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

      batch.begin(perspectiveCamera);
        map.shader = null;
        batch.render(map);
        for (Entity entity : entities) {
          if (EntityModelRenderable.class.isInstance(entity)) {
            EntityModelRenderable en = (EntityModelRenderable)entity;
            en.renderModel(batch, environment);
          }
        }
      batch.end();
      if (renderDecals) {
        renderContext.end();
        renderContext.begin();
        for (Entity entity : entities) {
          if (EntityDecalRenderable.class.isInstance(entity)) {
            EntityDecalRenderable en = (EntityDecalRenderable)entity;
            en.renderBatch(decalBatch, environment);
          }
        }
        decalBatch.flush();
        renderContext.end();
      }

    buffer.end();
  }

  public void renderDecalsForBufferAndBatch(FrameBuffer buffer, DecalBatch batch) {
    buffer.begin();
      renderContext.begin();
      renderContext.end();
      renderContext.begin();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glViewport(0, 0, buffer.getWidth(), buffer.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT  | GL20.GL_DEPTH_BUFFER_BIT);

        for (Entity entity : entities) {
          if (EntityDecalRenderable.class.isInstance(entity)) {
            EntityDecalRenderable en = (EntityDecalRenderable)entity;
            en.renderBatch(batch, environment);
          }
        }

        batch.flush();
      renderContext.end();
    buffer.end();
  }

  @Override
  public void render(float delta) {
    float nd = delta * speed;
    gameObjectsTweenManager.update(nd);
    uiTweenManager.update(delta);
    controller.update(nd);
    cameraController.update(delta);
    perspectiveCamera.update();

    for (Entity entity : entities) {
      entity.update(nd);
    }


    renderEntitiesForBufferAndBatch(depthBuffer, depthModelBatch, false);
    renderEntitiesForBufferAndBatch(colorBuffer, colorModelBatch, true);
    //renderDecalsForBufferAndBatch(decalBuffer, decalBatch);

    cellShadingCompositor.render(colorBuffer, depthBuffer, decalBuffer);
  }

  @Override
  public void resize(int width, int height) {
    perspectiveCamera.viewportWidth = width;
    perspectiveCamera.viewportHeight = height;
    perspectiveCamera.update(true);

    if (this.colorBuffer != null) {
      this.colorBuffer.dispose();
    }

    if (this.depthBuffer != null) {
      this.depthBuffer.dispose();
    }

    if (this.decalBuffer != null) {
      this.decalBuffer.dispose();
    }

    this.colorBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    this.depthBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    this.decalBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    cellShadingCompositor.resize(width, height);
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
    //skybox.dispose();
    colorModelBatch.dispose();
    depthModelBatch.dispose();
    decalBuffer.dispose();
    controller.dispose();
    map.dispose();
    decalBatch.dispose();
    for (Entity e : entities) {
      e.dispose();
    }
    this.colorBuffer.dispose();
    this.depthBuffer.dispose();
  }


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

  public boolean isPassable(Vector3 position) {
    int bx = (int)(Math.floor(position.x));
    int by = (int)(Math.floor(position.z));
    Block targetBlock = map.get(bx, by);
    return (targetBlock != null && targetBlock.isPassable());
  }
}
