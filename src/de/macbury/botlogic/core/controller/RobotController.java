package de.macbury.botlogic.core.controller;

import com.badlogic.gdx.Gdx;
import de.macbury.botlogic.core.controller.actions.WaitAction;

/**
 * Created by macbury on 31.03.14.
 */
public class RobotController {
  private static final String TAG = "RobotController";
  private GameController controller;

  public RobotController(GameController controller) {
    this.controller = controller;
  }

  public void say(String text) {
    Gdx.app.log(TAG, text);
  }
  
  public void sleep(int miliseconds) {
    Gdx.app.log(TAG, "Will sleep for "+miliseconds);
    WaitAction action = new WaitAction(miliseconds);
    this.controller.setAction(action);
    action.waitUntilDone();
    Gdx.app.log(TAG, "Finished sleeping!");
  }
}
