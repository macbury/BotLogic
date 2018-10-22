package de.macbury.botlogic.core.runtime.ext;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.macbury.botlogic.core.runtime.ScriptRunner;
import de.macbury.botlogic.core.runtime.ScriptRuntimeListener;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.ScriptableObject;

/**
 * Created by macbury on 09.04.14.
 */
public class ScriptRunnable implements Runnable, Disposable {
  public enum ScriptRunnableMode {
    Single, Loop, Trigger
  }

  public enum ScriptRunnableState {
    Pending, Running, Stopping, Finished
  }

  private static final String TAG = "ScriptRunnable";
  private ScriptRunner scriptRunner;
  private ScriptContextFactory factory;
  private String source;
  private final Object[] tempArgs;
  private ScriptableObject scriptObjectScope;
  private ScriptContext context;
  private ScriptRunnableState state = ScriptRunnableState.Pending;
  public ScriptRunnableMode mode = ScriptRunnableMode.Single;


  public ScriptRunnable(String source, ScriptRunner scriptRunner) {
    this.source       = source;
    this.scriptRunner = scriptRunner;
    this.factory      = (ScriptContextFactory)ScriptContextFactory.getGlobal();
    tempArgs          = new Object[1];
  }

  @Override
  public void run() {
    this.context   = (ScriptContext)factory.enterContext();
    context.setOptimizationLevel(-1);

    Gdx.app.log(TAG, "Reseting scope");
    this.scriptObjectScope = context.initStandardObjects();
    context.setInterrputFlag(false);

    try {
      state = ScriptRunnableState.Running;
      Gdx.app.log(TAG, "Loading ENV");
      scriptRunner.prepareScriptEnv();
      Gdx.app.log(TAG, "Running code");

      for(ScriptRuntimeListener listener : scriptRunner.getListeners()) {
        listener.onScriptStart(scriptRunner);
      }

      if (mode == ScriptRunnableMode.Loop) {
        context.evaluateString(scriptObjectScope, source, getClass().toString(), 0, null);
        while(state == ScriptRunnableState.Running) {
          try {
            this.scriptRunner.onScriptLoop();
            Thread.sleep(this.scriptRunner.getLoopSleep());
          } catch (InterruptedException e) {
            e.printStackTrace();
            state = ScriptRunnableState.Stopping;
          }
        }
        Gdx.app.log(TAG, "Running last script loop");
        this.scriptRunner.onScriptEnd();
      } else {
        context.evaluateString(scriptObjectScope, source, getClass().toString(), 0, null);
        this.scriptRunner.onScriptEnd();
      }
    } catch (ScriptInterputException e) {
      state = ScriptRunnableState.Stopping;
      Gdx.app.log(TAG, "Recived Interput");
      for(ScriptRuntimeListener listener : scriptRunner.getListeners()) {
        listener.onScriptInterput(scriptRunner);
      }
    } catch (RhinoException e) {
      state = ScriptRunnableState.Stopping;
      Gdx.app.log(TAG, "Recived syntax exception");
      for(ScriptRuntimeListener listener : scriptRunner.getListeners()) {
        listener.onScriptError(scriptRunner, e);
      }
    } catch (Exception e) {
      Gdx.app.log(TAG, "Something very bad");
      throw new GdxRuntimeException(e);
    } finally {
      Context.exit();
      state = ScriptRunnableState.Finished;

      for(ScriptRuntimeListener listener : scriptRunner.getListeners()) {
        listener.onScriptFinish(scriptRunner);
      }

      Gdx.app.log(TAG, "Exiting context");
    }
  }


  public boolean isRunning() {
    return state == ScriptRunnableState.Running || state == ScriptRunnableState.Stopping;
  }

  public void stop() {
    if (state == ScriptRunnableState.Running) {
      state = ScriptRunnableState.Stopping;
      if (context != null) {
        context.interrputScript();
      }
    }
  }

  @Override
  public void dispose() {
    stop();
    context = null;
  }

  public ScriptableObject getScriptObjectScope() {
    return scriptObjectScope;
  }

  public void setScriptObjectScope(ScriptableObject scriptObjectScope) {
    this.scriptObjectScope = scriptObjectScope;
  }

  public ScriptContext getContext() {
    return context;
  }

  public void setContext(ScriptContext context) {
    this.context = context;
  }
}