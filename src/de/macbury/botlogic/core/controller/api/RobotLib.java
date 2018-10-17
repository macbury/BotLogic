package de.macbury.botlogic.core.controller.api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import de.macbury.botlogic.core.controller.GameController;
import de.macbury.botlogic.core.controller.actions.MoveAction;
import de.macbury.botlogic.core.controller.actions.RotateAction;
import de.macbury.botlogic.core.controller.actions.WaitAction;
import de.macbury.botlogic.core.entites.ModelEntity;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.RhinoException;
/**
 * Created by macbury on 31.03.14.
 */
public class RobotLib extends BaseLib{
  private static final String TAG = "RobotLib";

  public RobotLib(GameController controller) {
    super(controller);
  }

  @Override
  public void reset() {

  }

  @Override
  public String getNamespace() {
    return "robot";
  }

  public void say(String text) {
    Gdx.app.log(TAG, text);
  }

  public void moveForward() throws RhinoException {
    Vector3 targetPosition = new Vector3();
    ModelEntity.directionToVector(robot.getDirection(), targetPosition);
    targetPosition.add(robot.position);

    if (level.isPassable(targetPosition)) {
      MoveAction action = new MoveAction(targetPosition);
      this.controller.setAction(action);
      action.waitUntilDone();
    } else {
      Context.throwAsScriptRuntimeEx(new Exception("Cannot move forward"));
    }

  }

  public void rotateLeft() {
    RotateAction action = new RotateAction(1);
    this.controller.setAction(action);
    action.waitUntilDone();
  }

  public void rotateRight() {
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
