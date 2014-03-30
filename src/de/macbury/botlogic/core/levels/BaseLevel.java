package de.macbury.botlogic.core.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import de.macbury.botlogic.core.graphics.camera.RTSCameraController;
import de.macbury.botlogic.core.map.Map;
import de.macbury.botlogic.core.runtime.ScriptRunner;

import java.util.ArrayList;

public abstract class BaseLevel implements Screen {
  private Map map;
  private RTSCameraController cameraController;
  private Environment environment;
  private ModelBatch modelBatch;
  private PerspectiveCamera perspectiveCamera;
  private ScriptRunner scriptRunner;

  public BaseLevel() {
    this.scriptRunner       = new ScriptRunner();
    this.perspectiveCamera  = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    this.map                = new Map(Gdx.files.internal("maps/playground.map"));
    perspectiveCamera.position.set(0, 0, 0);
    perspectiveCamera.near = 0.1f;
    perspectiveCamera.far = 300f;
    perspectiveCamera.update(true);

    this.cameraController = new RTSCameraController();
    cameraController.setCamera(perspectiveCamera);
    cameraController.setCenter(0,0);
    cameraController.setEnabled(true);
    Gdx.input.setInputProcessor(cameraController);
    environment = new Environment();
    environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .4f, .4f, .4f, 1f));
    environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

    modelBatch = new ModelBatch();
  }

  @Override
  public void render(float delta) {
    cameraController.update(delta);
    perspectiveCamera.update();
    modelBatch.begin(perspectiveCamera);
      modelBatch.render(map);
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
    scriptRunner.dispose();
    map.dispose();
  }

  public abstract boolean winCondition();

  public ScriptRunner getScriptRunner() {
    return scriptRunner;
  }
}
