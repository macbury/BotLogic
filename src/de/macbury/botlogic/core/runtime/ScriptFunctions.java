package de.macbury.botlogic.core.runtime;

import com.badlogic.gdx.Gdx;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContinuationPending;

public class ScriptFunctions{
  private static final String TAG = "ScriptFunctions";

  public void pause() {
    Context cx = Context.enter();
    try {
      ContinuationPending pending = cx.captureContinuation();
      pending.setApplicationState(1);
      throw pending;
    } finally {
      Context.exit();
    }
  }

  protected void test(String arg) {
    Gdx.app.log(TAG, "TEST: " +arg);
  }

  public void log(String message) {
    Gdx.app.log(TAG, message);
  }

  public void block() {
    while(true) {}
  }
}