package de.macbury.botlogic.core.controller.actions;

import de.macbury.botlogic.core.controller.GameAction;

/**
 * Created by macbury on 11.04.14.
 */
public class LedBlinkAction extends GameAction {
  private float accumulatedTime = 0.0f;
  private int times;
  private float pauseMili;

  public LedBlinkAction(int times, float pauseMili) {
    super();
    this.times = times;
    this.pauseMili = pauseMili / 1000f;
  }

  @Override
  public void onStart() {
  }

  @Override
  public void update(double delta) {
    accumulatedTime += delta;

    if (accumulatedTime >= pauseMili) {
      accumulatedTime = 0;
      if (times-- % 2 == 0) {
        level.robot.led.on();
      } else {
        level.robot.led.off();
      }
    }

    if (times < 0) {
      finish();
    }
  }

  @Override
  public void onEnd() {
    times = 0;
    level.robot.led.off();
  }
}
