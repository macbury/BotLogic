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
public class MoveAction extends GameAction implements TweenCallback {
  @Override
  public void onStart() {
    Tween.to(level.robot, ModelEntityAccessor.POSITION_XZ, 0.5f).target(level.robot.position.x, level.robot.position.z+1).setCallback(this).start(level.gameObjectsTweenManager);
    BotLogic.audio.move.play();
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
