package de.macbury.botlogic.core.controller;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.macbury.botlogic.core.controller.api.GameLib;
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
  private GameLib gameLib;
  private MissionScriptRunner missionScriptRunner;
  private GameAction currentAction;
  private RobotScriptRunner robotScriptRunner;
  private GameLevelScreen level;
  private RobotLib robotLib;
  private float runningTime;

  public GameController(GameLevelScreen level) {
    this.level               = level;
    this.robotScriptRunner   = new RobotScriptRunner(this);
    this.missionScriptRunner = new MissionScriptRunner(this);
    this.currentAction       = null;

    this.robotScriptRunner.addListener(this);
    this.missionScriptRunner.addListener(this);

    this.robotLib = new RobotLib(this);
    this.gameLib  = new GameLib(this);
  }

  public void run(String source) {
    runningTime = 0.0f;
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

  public void reset() {
    level.get3DCameraController().setEnabled(true);
    level.gameObjectsTweenManager.killAll();
    level.reset();
  }

  public ScriptRunner getRobotScriptRunner() {
    return robotScriptRunner;
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
      runningTime += delta;
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
      camera.setEnabled(false);

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
      missionScriptRunner.finish();
    }

    if (runner == missionScriptRunner) {
      finishAction();
    }
  }

  public void finishAction() {
    if (currentAction != null) {
      currentAction.finish();
    }

    currentAction = null;
  }

  public RobotLib getRobotLib() {
    return robotLib;
  }

  public boolean isRunning() {
    return (robotScriptRunner != null && robotScriptRunner.isRunning()) || (missionScriptRunner != null && missionScriptRunner.isRunning());
  }

  public GameAction getCurrentAction() {
    return currentAction;
  }

  public MissionScriptRunner getMissionScriptRunner() {
    return missionScriptRunner;
  }

  public float getRunningTime() {
    return runningTime;
  }

  @Override
  public void dispose() {
    robotScriptRunner.dispose();
    missionScriptRunner.dispose();
    finishAction();
  }

  public GameLib getGameLib() {
    return gameLib;
  }

  public GameLevelScreen getLevel() {
    return level;
  }
}
