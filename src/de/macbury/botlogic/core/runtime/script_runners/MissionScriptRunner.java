package de.macbury.botlogic.core.runtime.script_runners;

import com.badlogic.gdx.Gdx;
import de.macbury.botlogic.core.controller.GameController;
import de.macbury.botlogic.core.runtime.ScriptRunner;
import de.macbury.botlogic.core.runtime.ext.ScriptRunnable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Created by macbury on 09.04.14.
 */
public class MissionScriptRunner extends ScriptRunner {
  public static final String TAG = "MissionScriptRunner";
  private static final String LOOP_FUNCTION = "loop();";

  public MissionScriptRunner(GameController gameController) {
    super(gameController);
  }

  @Override
  public void prepareScriptEnv() {
    this.currentScriptRunnable.mode = ScriptRunnable.ScriptRunnableMode.Loop;

    ScriptableObject scriptObjectScope = currentScriptRunnable.getScriptObjectScope();
    scriptObjectScope.put("game", scriptObjectScope, gameController);
  }

  @Override
  public long getLoopSleep() {
    return 500;
  }

  @Override
  public void onScriptLoop() {
    currentScriptRunnable.getContext().evaluateString(currentScriptRunnable.getScriptObjectScope(), LOOP_FUNCTION, TAG, 0, null);
  }

  @Override
  public void beforeFinishScript() {
    Gdx.app.log(TAG, "Before finish");
  }
}
