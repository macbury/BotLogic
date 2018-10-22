package de.macbury.botlogic.core.controller.api;

import com.badlogic.gdx.math.Vector3;
import de.macbury.botlogic.core.controller.GameController;
import de.macbury.botlogic.core.controller.actions.SonarPingAction;
import de.macbury.botlogic.core.entites.ModelEntity;

/**
 * Created by macbury on 11.04.14.
 */
public class SonarLib extends BaseLib {
  private static byte MAX_DISTANCE = 10;
  private Vector3 aVec = new Vector3();
  private Vector3 bVec = new Vector3();
  public SonarLib(GameController controller) {
    super(controller);
  }

  @Override
  public void reset() {
    aVec.set(Vector3.Zero);
    bVec.set(Vector3.Zero);
  }

  @Override
  public String getNamespace() {
    return "sonar";
  }

  public boolean canMove() {
    return ping() > 0;
  }

  public int ping() {
    int distance = getFreeDistance();
    SonarPingAction action = new SonarPingAction(distance);
    this.controller.setAction(action);
    action.waitUntilDone();
    return distance;
  }

  private int getFreeDistance() {
    ModelEntity.directionToVector(robot.getDirection(), aVec);
    bVec.set(robot.position);
    int distance = 0;
    while(distance < MAX_DISTANCE) {
      bVec.add(aVec);
      if (!level.isPassable(bVec)) {
        break;
      }
      distance++;
    }
    return distance;
  }
}
