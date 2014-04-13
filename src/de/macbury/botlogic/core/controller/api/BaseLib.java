package de.macbury.botlogic.core.controller.api;

import de.macbury.botlogic.core.controller.GameController;
import de.macbury.botlogic.core.entites.RobotEntity;
import de.macbury.botlogic.core.screens.level.GameLevelScreen;

/**
 * Created by macbury on 11.04.14.
 */
public abstract class BaseLib {
  protected RobotEntity robot;
  protected GameLevelScreen level;
  protected GameController controller;

  public BaseLib(GameController controller) {
    this.controller = controller;
    this.level      = controller.getLevel();
    this.robot      = level.robot;
  }

  public abstract void reset();
  public abstract String getNamespace();
}
