package de.macbury.botlogic.core.controller.api;

import de.macbury.botlogic.core.controller.GameController;

/**
 * Created by macbury on 09.04.14.
 */
public class GameLib {
  private GameController controller;

  public GameLib(GameController gameController) {
    this.controller = gameController;
  }

  public void stop() {
    controller.stop();
  }

  public void fail() {
    controller.reset();
  }

  public boolean timeElapsed(int timeInSeconds) {
    return controller.getRunningTime() >= timeInSeconds;
  }

  public int robotSteps() {
    return controller.getLevel().robot.steps;
  }

  public int robotRotations() {
    return controller.getLevel().robot.rotations;
  }
}
