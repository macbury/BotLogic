package de.macbury.botlogic.core;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import de.macbury.botlogic.core.audio.AudioManager;
import de.macbury.botlogic.core.config.ConfigManager;
import de.macbury.botlogic.core.controller.GameController;
import de.macbury.botlogic.core.entites.EntityManager;
import de.macbury.botlogic.core.entites.ModelEntity;
import de.macbury.botlogic.core.entites.RobotEntity;
import de.macbury.botlogic.core.graphics.managers.ModelManager;
import de.macbury.botlogic.core.graphics.camera.RTSCameraController;
import de.macbury.botlogic.core.graphics.managers.SpritesManager;
import de.macbury.botlogic.core.input.InputManager;
import de.macbury.botlogic.core.screens.level.GameLevelScreen;
import de.macbury.botlogic.core.runtime.ext.ScriptContextFactory;
import de.macbury.botlogic.core.runtime.ScriptRunner;
import de.macbury.botlogic.core.screens.ScreenManager;
import de.macbury.botlogic.core.tween.CameraAccessor;
import de.macbury.botlogic.core.tween.ModelEntityAccessor;
import de.macbury.botlogic.core.tween.RobotEntityAccessor;
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
    Tween.registerAccessor(RobotEntity.class, new RobotEntityAccessor());
    ContextFactory.initGlobal(new ScriptContextFactory());

    BotLogic.game         = this;
    BotLogic.inputManager = new InputManager();
    BotLogic.audio        = new AudioManager();
    BotLogic.skin         = new FlatSkin();
    BotLogic.models       = new ModelManager();
    BotLogic.sprites      = new SpritesManager();
    BotLogic.entities     = new EntityManager();
    BotLogic.config       = new ConfigManager();

    BotLogic.screens      = new ScreenManager(this);// always last!!!
    BotLogic.config.load();
    loading = false;
    fpsLogger = new FPSLogger();

    BotLogic.screens.goToMainMenu();
  }

  public GameLevelScreen getLevel() {
    return (GameLevelScreen)getScreen();
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

  }

  public GameController getController() {
    return getLevel().getController();
  }

  public ScriptRunner getScriptRunner() {
    return getController().getRobotScriptRunner();
  }
}
