package de.macbury.botlogic.core.controller.api;

import de.macbury.botlogic.core.controller.GameController;

/**
 * Created by macbury on 09.04.14.
 */
public class GameLib extends BaseLib{
  public GameLib(GameController controller) {
    super(controller);
  }

  @Override
  public void reset() {

  }

  @Override
  public String getNamespace() {
     return "game";
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
    return 0;
  }

  public int robotRotations() {
    return 0;
  }
}
