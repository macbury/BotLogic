package de.macbury.botlogic.core.controller;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.botlogic.core.controller.api.RobotLib;
import de.macbury.botlogic.core.graphics.camera.RTSCameraController;
import de.macbury.botlogic.core.screens.level.GameLevelScreen;
import de.macbury.botlogic.core.runtime.ScriptRunner;
import de.macbury.botlogic.core.runtime.ScriptRuntimeListener;
import de.macbury.botlogic.core.tween.CameraAccessor;
import org.mozilla.javascript.RhinoException;

/**
 * Created by macbury on 31.03.14.
 */
public class GameController implements Disposable, ScriptRuntimeListener {
  private GameAction currentAction;
  private ScriptRunner scriptRunner;
  private GameLevelScreen level;
  private RobotLib robotController;

  public GameController(GameLevelScreen level) {
    this.level          = level;
    this.scriptRunner   = new ScriptRunner(this);
    this.currentAction  = null;

    this.scriptRunner.addListener(this);

    this.robotController = new RobotLib(this);
  }

  public void run(String source) {
    this.scriptRunner.execute(source);
  }

  public void stop() {
    this.scriptRunner.finish();
    finishAction();
  }

  public ScriptRunner getScriptRunner() {
    return scriptRunner;
  }

  @Override
  public void dispose() {
    scriptRunner.dispose();
    finishAction();
  }

  public synchronized void setAction(GameAction action) {
    currentAction = action;
    synchronized (currentAction) {
      currentAction.setLevel(level);
      currentAction.start();
    }
  }

  public void update(float delta) {
    if (scriptRunner.isRunning()) {
      level.get3DCameraController().setCenter(level.robot.position.x, level.robot.position.z);

      if (currentAction != null) {
        synchronized (currentAction) {
          if (currentAction.isDone()) {
            currentAction = null;
          } else {
            currentAction.update(delta);
          }
        }
      }

    }
  }

  @Override
  public void onScriptStart(ScriptRunner runner) {
    level.reset();

    RTSCameraController camera = level.get3DCameraController();
    //camera.setEnabled(false);

    float targetZoom = 5;
    float targetRotation = -5.8463125f;
    float targetTilt = 1.0542735f;

    Timeline.createSequence().beginParallel()
      .push(Tween.to(camera, CameraAccessor.CENTER, 1).target(camera.getCenter().x, camera.getCenter().y, camera.getCenter().z))
      .push(Tween.to(camera, CameraAccessor.ROTATION_TILT_ZOOM, 1).target(targetZoom, targetRotation, targetTilt))
    .end().start(level.uiTweenManager);
  }

  @Override
  public void onScriptYield(ScriptRunner runner) {

  }

  @Override
  public void onScriptInterput(ScriptRunner runner) {

  }

  @Override
  public void onScriptError(ScriptRunner runner, RhinoException error) {

  }

  @Override
  public void onScriptFinish(ScriptRunner runner) {
    finishAction();
    level.get3DCameraController().setEnabled(true);
    //BotLogic.audio.mainMenuMusic.stop();
    level.gameObjectsTweenManager.killAll();
    level.reset();
  }

  public void finishAction() {
    if (currentAction != null) {
      currentAction.finish();
      currentAction = null;
    }
  }

  public RobotLib getRobotController() {
    return robotController;
  }

}
