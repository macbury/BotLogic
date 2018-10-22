package de.macbury.botlogic.core.controller.actions;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.TweenCallback;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.controller.GameAction;

/**
 * Created by macbury on 31.03.14.
 */
public class RotateAction extends GameAction implements TweenCallback {
  private static final String TAG = "RotateAction";
  private final int direction;

  public RotateAction(int dir) {
    this.direction = dir;
  }

  @Override
  public void onStart() {
    BotLogic.audio.rotation.play();
    level.robot.setRotateLeftWheelsDirection(-direction);
    level.robot.setRotateRightWheelsDirection(direction);
    level.robot.getRotationTween(direction).setCallback(this).start(level.gameObjectsTweenManager);
  }

  @Override
  public void update(double delta) {

  }

  @Override
  public void onEnd() {
    level.robot.setRotateLeftWheelsDirection(0);
    level.robot.setRotateRightWheelsDirection(0);
  }


  @Override
  public void onEvent(int i, BaseTween<?> baseTween) {
    finish();
  }
}
