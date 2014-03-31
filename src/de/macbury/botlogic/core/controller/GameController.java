package de.macbury.botlogic.core.controller;

import com.badlogic.gdx.utils.Disposable;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.graphics.camera.RTSCameraController;
import de.macbury.botlogic.core.levels.BaseLevel;
import de.macbury.botlogic.core.runtime.ScriptRunner;
import de.macbury.botlogic.core.runtime.ScriptRuntimeListener;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.FunctionCompletion;
import org.mozilla.javascript.RhinoException;

/**
 * Created by macbury on 31.03.14.
 */
public class GameController implements Disposable, ScriptRuntimeListener {
  private GameAction currentAction;
  private ScriptRunner scriptRunner;
  private BaseLevel level;
  private RobotController robotController;

  public GameController(BaseLevel level) {
    this.level          = level;
    this.scriptRunner   = new ScriptRunner(this);
    this.currentAction  = null;

    this.scriptRunner.addListener(this);

    this.robotController = new RobotController(this);
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
    camera.setEnabled(false);
    camera.setZoom(10);
    camera.setRotation(-3.1415f);
    camera.setTilt(1.2f);
    BotLogic.audio.music.play();
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
    BotLogic.audio.music.stop();
    level.tweenManager.killAll();
  }

  public void finishAction() {
    if (currentAction != null) {
      currentAction.finish();
      currentAction = null;
    }
  }

  public RobotController getRobotController() {
    return robotController;
  }

}
