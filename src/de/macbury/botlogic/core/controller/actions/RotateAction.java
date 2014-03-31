package de.macbury.botlogic.core.controller.actions;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.controller.GameAction;
import de.macbury.botlogic.core.tween.ModelEntityAccessor;

/**
 * Created by macbury on 31.03.14.
 */
public class RotateAction extends GameAction implements TweenCallback {
  private final int direction;

  public RotateAction(int dir) {
    this.direction = dir;
  }

  @Override
  public void onStart() {
    BotLogic.audio.rotation.play();
    level.robot.getRotationTween(direction).setCallback(this).start(level.tweenManager);
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
