package de.macbury.botlogic.core.tween;

import aurelienribon.tweenengine.TweenAccessor;
import de.macbury.botlogic.core.entites.RobotEntity;

/**
 * Created by macbury on 12.04.14.
 */
public class RobotEntityAccessor implements TweenAccessor<RobotEntity> {
  public static final int POSITION_X  = 1;
  public static final int POSITION_Z  = 2;
  public static final int POSITION_XZ = 3;
  public static final int ROTATE      = 4;
  public static final int SONAR_SIZE = 5;

  @Override
  public int getValues(RobotEntity modelEntity, int tweenType, float[] returnValues) {
    switch (tweenType) {
      case ROTATE: returnValues[0]     = modelEntity.rotation; return 1;
      case POSITION_X: returnValues[0] = modelEntity.position.x; return 1;
      case POSITION_Z: returnValues[0] = modelEntity.position.z; return 1;
      case POSITION_XZ:
        returnValues[0] = modelEntity.position.x;
        returnValues[1] = modelEntity.position.z;
      return 2;
      case SONAR_SIZE:
        returnValues[0] = modelEntity.getSonarSprite().getWidth();
        returnValues[1] = modelEntity.getSonarSprite().getHeight();
        returnValues[2] = modelEntity.getSonarPingAlpha();
        return 3;
      default: assert false; return -1;
    }

  }

  @Override
  public void setValues(RobotEntity modelEntity, int tweenType, float[] newValues) {
    switch (tweenType) {
      case ROTATE:     modelEntity.rotation = newValues[0]; break;
      case POSITION_X: modelEntity.position.x = newValues[0]; break;
      case POSITION_Z: modelEntity.position.z = newValues[0]; break;
      case POSITION_XZ:
        modelEntity.position.x = newValues[0];
        modelEntity.position.z = newValues[1];
      break;

      case SONAR_SIZE:
        modelEntity.getSonarSprite().setWidth(newValues[0]);
        modelEntity.getSonarSprite().setHeight(newValues[1]);
        modelEntity.setSonarPingAlpha(newValues[2]);
      break;
      default: assert false; break;
    }

  }
}
