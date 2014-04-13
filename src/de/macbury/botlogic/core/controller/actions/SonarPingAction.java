package de.macbury.botlogic.core.controller.actions;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.TweenCallback;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.controller.GameAction;

/**
 * Created by macbury on 12.04.14.
 */
public class SonarPingAction extends GameAction implements TweenCallback {
  private int distance;

  public SonarPingAction(int distance) {
    this.distance = distance;
  }

  @Override
  public void onStart() {
    level.robot.getSonarPingTimeline(distance+2).setCallback(this).start(level.gameObjectsTweenManager);
    BotLogic.audio.sonar.play();
  }

  @Override
  public void update(double delta) {

  }

  @Override
  public void onEnd() {

  }

  @Override
  public void onEvent(int i, BaseTween<?> baseTween) {
    finish();
  }
}
