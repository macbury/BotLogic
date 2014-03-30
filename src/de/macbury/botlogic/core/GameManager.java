package de.macbury.botlogic.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import de.macbury.botlogic.core.input.InputManager;
import de.macbury.botlogic.core.levels.BaseLevel;
import de.macbury.botlogic.core.levels.PlaygroundLevel;
import de.macbury.botlogic.core.runtime.ScriptContextFactory;
import org.mozilla.javascript.ContextFactory;

/**
 * Created by macbury on 27.03.14.
 */
public class GameManager extends Game {
  private static final String TAG = "GameManager";
  private boolean loading = true;

  @Override
  public void create() {
    Gdx.app.log(TAG, "created");
    BotLogic.game         = this;
    BotLogic.inputManager = new InputManager();
    ContextFactory.initGlobal(new ScriptContextFactory());
    loading = false;
  }

  public BaseLevel getLevel() {
    return (BaseLevel)getScreen();
  }

  public boolean isLoading() {
    return loading;
  }

  @Override
  public void render() {
    Gdx.gl.glClearColor(0, 0, 0, 0);
    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    super.render();
  }

  public void newGame() {
    Gdx.app.log(TAG, "New game");
    if (getLevel() != null) {
      getLevel().dispose();
      Gdx.app.log(TAG, "Disposing old level");
    }
    setScreen(new PlaygroundLevel());
  }
}
