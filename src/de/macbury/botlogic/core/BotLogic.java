package de.macbury.botlogic.core;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.macbury.botlogic.core.audio.AudioManager;
import de.macbury.botlogic.core.config.ConfigManager;
import de.macbury.botlogic.core.input.InputManager;
import de.macbury.botlogic.core.screens.ScreenManager;
import de.macbury.botlogic.core.ui.FlatSkin;

/**
 * Created by macbury on 28.03.14.
 */
public class BotLogic {
  public static GameManager game;
  public static InputManager inputManager;
  public static AudioManager audio;
  public static ScreenManager screens;
  public static FlatSkin skin;
  public static ConfigManager config;
}
