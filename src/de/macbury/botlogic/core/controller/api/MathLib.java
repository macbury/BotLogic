package de.macbury.botlogic.core.controller.api;

import de.macbury.botlogic.core.controller.GameController;

/**
 * Created by macbury on 01.04.14.
 */
public class MathLib extends BaseLib {

  public MathLib(GameController controller) {
    super(controller);
  }

  @Override
  public void reset() {

  }

  @Override
  public String getNamespace() {
    return "math";
  }

  public float random() {
    return (float)Math.random();
  }

  public int round(float value) {
    return Math.round(value);
  }

}
