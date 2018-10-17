package de.macbury.botlogic.core.tween;

import aurelienribon.tweenengine.TweenAccessor;
import de.macbury.botlogic.core.graphics.camera.RTSCameraController;

/**
 * Created by macbury on 02.04.14.
 */
public class CameraAccessor implements TweenAccessor<RTSCameraController> {
  public static final int CENTER = 2;
  public static final int ROTATION_TILT_ZOOM = 3;

  @Override
  public int getValues(RTSCameraController camera, int tweenType, float[] returnValues) {
    switch (tweenType) {
      case CENTER:
        returnValues[0] = camera.getCenter().x;
        returnValues[1] = camera.getCenter().y;
        returnValues[2] = camera.getCenter().z;
      return 2;
      case ROTATION_TILT_ZOOM:
        returnValues[0] = camera.getCurrentZoom();
        returnValues[1] = camera.getRotation();
        returnValues[2] = camera.getTilt();
      return 3;
      default: assert false; return -1;
    }

  }

  @Override
  public void setValues(RTSCameraController camera, int tweenType, float[] newValues) {
    switch (tweenType) {
      case CENTER:
        camera.setCenter(newValues[0], newValues[2]);
      break;
      case ROTATION_TILT_ZOOM:
        camera.setZoom(newValues[0]);
        camera.setRotation(newValues[1]);
        camera.setTilt(newValues[2]);
      break;
      default: assert false; break;
    }

  }
}
