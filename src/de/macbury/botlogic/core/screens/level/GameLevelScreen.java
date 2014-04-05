package de.macbury.botlogic.core.screens.level;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.shaders.DepthShader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.UBJsonReader;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.controller.GameController;
import de.macbury.botlogic.core.entites.*;
import de.macbury.botlogic.core.graphics.camera.RTSCameraController;
import de.macbury.botlogic.core.graphics.compositor.LevelCompositor;
import de.macbury.botlogic.core.graphics.skybox.SkyBox;
import de.macbury.botlogic.core.screens.level.file.LevelFile;
import de.macbury.botlogic.core.screens.level.map.Map;

import java.util.ArrayList;

public class GameLevelScreen implements Screen {
  private SkyBox skybox;
  private ModelBatch depthModelBatch;
  private RenderContext renderContext;
  private LevelCompositor cellShadingCompositor;
  private FrameBuffer colorBuffer;
  private DecalBatch decalBatch;
  private LevelFile levelDefinition;
  public TweenManager tweenManager;
  public RobotEntity robot;
  private GameController controller;
  private Model robotModel;
  private Map map;
  private RTSCameraController cameraController;
  private Environment environment;
  private ModelBatch colorModelBatch;
  private PerspectiveCamera perspectiveCamera;
  private ArrayList<Entity> entities;
  private int speed = 1;
  private FrameBuffer depthBuffer;

  public GameLevelScreen(LevelFile levelDef) {
    this.tweenManager       = new TweenManager();
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
    Gdx.input.setInputProcessor(cameraController);

    environment = new Environment();
    environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 1f));

    G3dModelLoader modelLoader = new G3dModelLoader(new UBJsonReader());
    this.robotModel            = modelLoader.loadModel(Gdx.files.getFileHandle("models/robot.g3db", Files.FileType.Internal));

    this.cellShadingCompositor = new LevelCompositor(renderContext);
    //this.skybox                = new SkyBox("grid.png");
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

    colorModelBatch = new ModelBatch(renderContext);
    decalBatch      = new DecalBatch();
    decalBatch.setGroupStrategy(new CameraGroupStrategy(perspectiveCamera));

    DepthShader.Config depthConfig     = new DepthShader.Config(Gdx.files.internal("shaders/depth.vertex").readString(), Gdx.files.internal("shaders/depth.frag").readString());
    depthConfig.defaultCullFace        = GL20.GL_BACK;
    depthConfig.depthBufferOnly        = false;
    depthModelBatch = new ModelBatch(renderContext, new DepthShaderProvider(depthConfig));

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

  public void renderEntitiesForBufferAndBatch(FrameBuffer buffer, ModelBatch batch, boolean renderSkyBox) {
    buffer.begin();
      Gdx.gl.glClearColor(0, 0, 0, 0);
      Gdx.gl.glViewport(0, 0, buffer.getWidth(), buffer.getHeight());
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

      if (renderSkyBox) {
        //skybox.render(renderContext, perspectiveCamera);
      }

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
    buffer.end();
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

    renderEntitiesForBufferAndBatch(depthBuffer, depthModelBatch, false);
    renderEntitiesForBufferAndBatch(colorBuffer, colorModelBatch, true);


    for (Entity entity : entities) {
      if (EntityDecalRenderable.class.isInstance(entity)) {
        EntityDecalRenderable en = (EntityDecalRenderable)entity;
        en.renderBatch(decalBatch, environment);
      }
    }

    decalBatch.flush();

    cellShadingCompositor.render(colorBuffer, depthBuffer);
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

    this.colorBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    this.depthBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

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
    controller.dispose();
    map.dispose();
    robotModel.dispose();
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
}
