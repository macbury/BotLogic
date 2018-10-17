package de.macbury.botlogic.core.controller.actions;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.math.Vector3;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.controller.GameAction;

/**
 * Created by macbury on 31.03.14.
 */
public class MoveAction extends GameAction implements TweenCallback {

  private Vector3 targetPosition;

  public MoveAction(Vector3 target) {
    super();
    this.targetPosition = target;
  }

  @Override
  public void onStart() {
    level.robot.setRotateLeftWheelsDirection(1);
    level.robot.setRotateRightWheelsDirection(1);


    level.robot.getTranslationTween(targetPosition).setCallback(this).start(level.gameObjectsTweenManager);
    BotLogic.audio.move.play();
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
