package de.macbury.botlogic.core.controller.api;

import com.badlogic.gdx.Gdx;
import de.macbury.botlogic.core.controller.GameController;
import de.macbury.botlogic.core.controller.actions.MoveAction;
import de.macbury.botlogic.core.controller.actions.RotateAction;
import de.macbury.botlogic.core.controller.actions.WaitAction;

/**
 * Created by macbury on 31.03.14.
 */
public class RobotLib {
  private static final String TAG = "RobotLib";
  private GameController controller;

  public RobotLib(GameController controller) {
    this.controller = controller;
  }

  public void say(String text) {
    Gdx.app.log(TAG, text);
  }

  public void move_forward() {
    MoveAction action = new MoveAction();
    this.controller.setAction(action);
    action.waitUntilDone();
  }

  public void rotate_left() {
    RotateAction action = new RotateAction(1);
    this.controller.setAction(action);
    action.waitUntilDone();
  }

  public void rotate_right() {
    RotateAction action = new RotateAction(-1);
    this.controller.setAction(action);
    action.waitUntilDone();
  }
  
  public void sleep(int miliseconds) {
    WaitAction action = new WaitAction(miliseconds);
    this.controller.setAction(action);
    action.waitUntilDone();
  }
}
