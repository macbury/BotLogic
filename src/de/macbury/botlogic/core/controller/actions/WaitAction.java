package de.macbury.botlogic.core.controller.actions;

import com.badlogic.gdx.Gdx;
import de.macbury.botlogic.core.controller.GameAction;

/**
 * Created by macbury on 31.03.14.
 */
public class WaitAction extends GameAction {
  private static final String TAG = "WaitAction";
  private final float sleepTime;
  private float accumulatedTime = 0.0f;

  public WaitAction(int miliseconds) {
    this.sleepTime = miliseconds / 1000f;
  }

  @Override
  public void onStart() {
    Gdx.app.log(TAG, "Starting sleep");
  }

  @Override
  public void update(double delta) {
    accumulatedTime += delta;

    if (accumulatedTime >= sleepTime) {
      finish();
    }
  }

  @Override
  public void onEnd() {
    accumulatedTime = sleepTime;
    Gdx.app.log(TAG, "Sleep finished!");
  }

}
