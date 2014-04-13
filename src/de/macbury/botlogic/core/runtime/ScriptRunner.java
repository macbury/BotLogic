package de.macbury.botlogic.core.runtime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.macbury.botlogic.core.controller.GameController;
import de.macbury.botlogic.core.controller.api.BaseLib;
import de.macbury.botlogic.core.runtime.ext.ScriptRunnable;
import org.mozilla.javascript.ScriptableObject;

import java.util.ArrayList;

/**
 * Created by macbury on 28.03.14.
 */
public abstract class ScriptRunner implements Disposable {
  protected static final String TAG = "ScriptRunner";
  protected GameController gameController;

  protected ArrayList<ScriptRuntimeListener> listeners;
  protected ScriptRunnable currentScriptRunnable;

  public ScriptRunner(GameController gameController) {
    this.gameController = gameController;
    Gdx.app.log(TAG, "Initializing script runner");
    this.listeners = new ArrayList<ScriptRuntimeListener>();
  }

  public void execute(String source) {
    if (currentScriptRunnable != null) {
      if (currentScriptRunnable.isRunning()){
        throw new GdxRuntimeException("Already running!");
      } else {
        currentScriptRunnable.dispose();
      }
    }

    currentScriptRunnable = new ScriptRunnable(source, this);
    Thread thread = new Thread(currentScriptRunnable);
    thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
      @Override
      public void uncaughtException(Thread t, Throwable e) {
        throw new GdxRuntimeException(e);
      }
    });
    thread.start();
  }

  public boolean isRunning() {
    return (currentScriptRunnable != null && currentScriptRunnable.isRunning());
  }

  public void finish() {
    if (currentScriptRunnable != null) {
      currentScriptRunnable.stop();
    }
  }

  public void addListener(ScriptRuntimeListener listener) {
    if (listeners.indexOf(listener) == -1) {
      this.listeners.add(listener);
    }
  }

  public void removeListener(ScriptRuntimeListener listener) {
    if (listeners.indexOf(listener) != -1) {
      this.listeners.remove(listener);
    }
  }

  @Override
  public void dispose() {
    listeners.clear();
    Gdx.app.log(TAG, "Disposing");
    if (currentScriptRunnable != null) {
      currentScriptRunnable.dispose();
    }
  }

  protected void bindLib(BaseLib lib) {
    ScriptableObject scriptObjectScope = currentScriptRunnable.getScriptObjectScope();
    lib.reset();
    scriptObjectScope.put(lib.getNamespace(), scriptObjectScope, lib);
  }

  public ArrayList<ScriptRuntimeListener> getListeners() {
    return listeners;
  }

  public abstract void prepareScriptEnv();

  public abstract long getLoopSleep();

  public abstract void onScriptLoop();

  public abstract void onScriptEnd();
}
