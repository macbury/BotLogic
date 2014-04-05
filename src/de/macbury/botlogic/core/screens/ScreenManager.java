package de.macbury.botlogic.core.screens;

import com.badlogic.gdx.Gdx;
import de.macbury.botlogic.core.GameManager;
import de.macbury.botlogic.core.screens.level.GameLevelScreen;
import de.macbury.botlogic.core.screens.level.GameLevelWithUIScreen;
import de.macbury.botlogic.core.screens.level.file.LevelFile;
import de.macbury.botlogic.core.screens.menu.MainMenuScreen;

/**
 * Created by macbury on 04.04.14.
 */
public class ScreenManager {
  private static final String TAG = "ScreenManager";
  private GameManager game;
  private MainMenuScreen mainMenu;
  private GameLevelScreen levelScreen;
  public ScreenManager(GameManager game) {
    mainMenu      = new MainMenuScreen();
    this.game     = game;
  }

  public void goToMainMenu() {
    Gdx.app.log(TAG, "Opening main screen");
    if (levelScreen != null) {
      levelScreen.dispose();
      levelScreen = null;
    }
    this.game.setScreen(mainMenu);
  }

  public void startLevel(LevelFile levelFile) {
    levelScreen = new GameLevelWithUIScreen(levelFile);
    this.game.setScreen(levelScreen);
  }
}
