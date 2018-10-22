package de.macbury.botlogic.core;

import de.macbury.botlogic.core.audio.AudioManager;
import de.macbury.botlogic.core.config.ConfigManager;
import de.macbury.botlogic.core.entites.EntityManager;
import de.macbury.botlogic.core.graphics.managers.ModelManager;
import de.macbury.botlogic.core.graphics.managers.SpritesManager;
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
  public static ModelManager models;
  public static EntityManager entities;
  public static ScreenManager screens;
  public static SpritesManager sprites;
  public static FlatSkin skin;
  public static ConfigManager config;

  private BotLogic() {} // uniemożliwia utworzenie nowego obiektu
}
