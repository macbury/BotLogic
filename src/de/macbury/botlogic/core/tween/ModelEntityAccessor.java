package de.macbury.botlogic.core.tween;

import aurelienribon.tweenengine.TweenAccessor;
import de.macbury.botlogic.core.entites.ModelEntity;

/**
 * Created by macbury on 31.03.14.
 */
public class ModelEntityAccessor implements TweenAccessor<ModelEntity> {
  public static final int POSITION_X  = 1;
  public static final int POSITION_Z  = 2;
  public static final int POSITION_XZ = 3;
  public static final int ROTATE      = 4;

  @Override
  public int getValues(ModelEntity modelEntity, int tweenType, float[] returnValues) {
    switch (tweenType) {
      case ROTATE: returnValues[0]     = modelEntity.rotation; return 1;
      case POSITION_X: returnValues[0] = modelEntity.position.x; return 1;
      case POSITION_Z: returnValues[0] = modelEntity.position.z; return 1;
      case POSITION_XZ:
        returnValues[0] = modelEntity.position.x;
        returnValues[1] = modelEntity.position.z;
        return 2;
      default: assert false; return -1;
    }

  }

  @Override
  public void setValues(ModelEntity modelEntity, int tweenType, float[] newValues) {
    switch (tweenType) {
      case ROTATE:     modelEntity.rotation = newValues[0]; break;
      case POSITION_X: modelEntity.position.x = newValues[0]; break;
      case POSITION_Z: modelEntity.position.z = newValues[0]; break;
      case POSITION_XZ:
        modelEntity.position.x = newValues[0];
        modelEntity.position.z = newValues[1];
        break;
      default: assert false; break;
    }

  }
}
