package de.macbury.botlogic.core.controller;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.macbury.botlogic.core.controller.api.RobotLib;
import de.macbury.botlogic.core.graphics.camera.RTSCameraController;
import de.macbury.botlogic.core.runtime.script_runners.MissionScriptRunner;
import de.macbury.botlogic.core.runtime.script_runners.RobotScriptRunner;
import de.macbury.botlogic.core.screens.level.GameLevelScreen;
import de.macbury.botlogic.core.runtime.ScriptRunner;
import de.macbury.botlogic.core.runtime.ScriptRuntimeListener;
import de.macbury.botlogic.core.tween.CameraAccessor;
import org.mozilla.javascript.RhinoException;

/**
 * Created by macbury on 31.03.14.
 */
public class GameController implements Disposable, ScriptRuntimeListener {
  private static final String TAG = "GameController";
  private MissionScriptRunner missionScriptRunner;
  private GameAction currentAction;
  private RobotScriptRunner robotScriptRunner;
  private GameLevelScreen level;
  private RobotLib robotController;

  public GameController(GameLevelScreen level) {
    this.level               = level;
    this.robotScriptRunner   = new RobotScriptRunner(this);
    this.missionScriptRunner = new MissionScriptRunner(this);
    this.currentAction       = null;

    this.robotScriptRunner.addListener(this);
    this.missionScriptRunner.addListener(this);
    this.robotController = new RobotLib(this);
  }

  public void run(String source) {
    this.robotScriptRunner.execute(source);

    String missionScript = level.getFile().getScriptContent();

    if (missionScript != null) {
      this.missionScriptRunner.execute(missionScript);
    } else {
      Gdx.app.log(TAG, "Mission script is empty skipping...");
    }

  }

  public void stop() {
    finishAction();
    this.robotScriptRunner.finish();
  }

  public ScriptRunner getRobotScriptRunner() {
    return robotScriptRunner;
  }

  @Override
  public void dispose() {
    robotScriptRunner.dispose();
    missionScriptRunner.dispose();
    finishAction();
  }

  public synchronized void setAction(GameAction action) {
    if (currentAction != null && action != null) {
      throw new GdxRuntimeException("Still processing action: " + action.getClass().toString());
    }
    currentAction = action;

    if (currentAction != null) {
      synchronized (currentAction) {
        currentAction.setController(this);
        currentAction.setLevel(level);
        currentAction.start();
      }
    }
  }

  public void update(float delta) {
    if (robotScriptRunner.isRunning()) {
      level.get3DCameraController().setCenter(level.robot.position.x, level.robot.position.z);
    }

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

  @Override
  public void onScriptStart(ScriptRunner runner) {
    if (runner == robotScriptRunner) {
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
  }

  @Override
  public void onScriptInterput(ScriptRunner runner) {

  }

  @Override
  public void onScriptError(ScriptRunner runner, RhinoException error) {
    if (runner == missionScriptRunner) {
      error.printStackTrace();
      stop();
    }
  }

  @Override
  public void onScriptFinish(ScriptRunner runner) {
    Gdx.app.log(TAG, "OnScriptFinish!");
    if (runner == robotScriptRunner) {
      finishAction();
      level.get3DCameraController().setEnabled(true);
      level.gameObjectsTweenManager.killAll();
      level.reset();

      missionScriptRunner.finish();
    }
  }

  public void finishAction() {
    if (currentAction != null) {
      currentAction.finish();
    }

    currentAction = null;
  }

  public RobotLib getRobotController() {
    return robotController;
  }

  public boolean isRunning() {
    return (robotScriptRunner != null && robotScriptRunner.isRunning()) && (missionScriptRunner != null && robotScriptRunner.isRunning());
  }

  public void test(final String test) {
    Gdx.app.postRunnable(new Runnable() {
      @Override
      public void run() {
        Gdx.app.log(TAG, test);
      }
    });

  }

  public GameAction getCurrentAction() {
    return currentAction;
  }
}
