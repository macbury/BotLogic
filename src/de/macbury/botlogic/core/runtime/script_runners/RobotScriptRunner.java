package de.macbury.botlogic.core.runtime.script_runners;

import com.badlogic.gdx.Gdx;
import de.macbury.botlogic.core.controller.GameController;
import de.macbury.botlogic.core.controller.api.MathLib;
import de.macbury.botlogic.core.runtime.ScriptRunner;
import org.mozilla.javascript.ScriptableObject;

/**
 * Created by macbury on 09.04.14.
 */
public class RobotScriptRunner extends ScriptRunner {

  public RobotScriptRunner(GameController gameController) {
    super(gameController);
  }

  @Override
  public void prepareScriptEnv() {
    ScriptableObject scriptObjectScope = currentScriptRunnable.getScriptObjectScope();
    scriptObjectScope.put("robot", scriptObjectScope, gameController.getRobotLib());
    scriptObjectScope.put("math", scriptObjectScope, new MathLib());
    currentScriptRunnable.getContext().evaluateString(scriptObjectScope, Gdx.files.internal("sketches/rdk/helpers.js").readString(), "RobotScriptRunner", 0, null);//TODO: Better loading
  }

  @Override
  public long getLoopSleep() {
    return 0;
  }

  @Override
  public void onScriptLoop() {

  }

  @Override
  public void onScriptEnd() {

  }

}
