package de.macbury.botlogic.core.runtime;

import org.mozilla.javascript.RhinoException;

/**
 * Created by macbury on 28.03.14.
 */
public interface ScriptRuntimeListener {
  public void onScriptStart(ScriptRunner runner);
  public void onScriptInterput(ScriptRunner runner);
  public void onScriptError(ScriptRunner runner, RhinoException error);
  public void onScriptFinish(ScriptRunner runner);
}
