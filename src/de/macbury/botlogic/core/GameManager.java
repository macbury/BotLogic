package de.macbury.botlogic.core;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.FPSLogger;
import de.macbury.botlogic.core.audio.AudioManager;
import de.macbury.botlogic.core.config.ConfigManager;
import de.macbury.botlogic.core.controller.GameController;
import de.macbury.botlogic.core.entites.ModelEntity;
import de.macbury.botlogic.core.graphics.camera.RTSCameraController;
import de.macbury.botlogic.core.input.InputManager;
import de.macbury.botlogic.core.levels.BaseLevel;
import de.macbury.botlogic.core.levels.PlaygroundLevel;
import de.macbury.botlogic.core.runtime.ScriptContextFactory;
import de.macbury.botlogic.core.runtime.ScriptRunner;
import de.macbury.botlogic.core.screens.ScreenManager;
import de.macbury.botlogic.core.tween.CameraAccessor;
import de.macbury.botlogic.core.tween.ModelEntityAccessor;
import de.macbury.botlogic.core.ui.FlatSkin;
import org.mozilla.javascript.ContextFactory;

/**
 * Created by macbury on 27.03.14.
 */
public class GameManager extends Game {
  private static final String TAG = "GameManager";
  private boolean loading = true;
  private FPSLogger fpsLogger;

  @Override
  public void create() {
    Gdx.app.log(TAG, "created");

    Tween.registerAccessor(ModelEntity.class, new ModelEntityAccessor());
    Tween.registerAccessor(RTSCameraController.class, new CameraAccessor());
    ContextFactory.initGlobal(new ScriptContextFactory());

    BotLogic.game         = this;
    BotLogic.inputManager = new InputManager();
    BotLogic.audio        = new AudioManager();
    BotLogic.skin         = new FlatSkin();
    BotLogic.config       = new ConfigManager();

    BotLogic.screens      = new ScreenManager(this);// always last!!!
    BotLogic.config.loadResolution();
    loading = false;
    fpsLogger = new FPSLogger();

    BotLogic.screens.goToMainMenu();
  }

  public BaseLevel getLevel() {
    return (BaseLevel)getScreen();
  }

  public boolean isLoading() {
    return loading;
  }

  @Override
  public void render() {
    super.render();
    fpsLogger.log();
  }

  public void newGame(String path) {
    Gdx.app.log(TAG, "New game");
    if (getLevel() != null) {
      getLevel().dispose();
      Gdx.app.log(TAG, "Disposing old level");
    }
    setScreen(new PlaygroundLevel(path));
  }

  public GameController getController() {
    return getLevel().getController();
  }

  public ScriptRunner getScriptRunner() {
    return getController().getScriptRunner();
  }
}
