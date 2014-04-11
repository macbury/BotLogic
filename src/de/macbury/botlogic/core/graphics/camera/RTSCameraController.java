package de.macbury.botlogic.core.graphics.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.input.InputManager;

/**
 * Created by macbury on 05.03.14.
 */
public class RTSCameraController extends InputAdapter {
  private static final float LERP_SPEED = 15.0f;
  private static int CAMERA_MOVE_PADDING = 16;
  private PerspectiveCamera cam;

  private float currentZoom;
  private float maxZoom;
  private float minZoom;
  private int scrollAmount;
  private float scrollSpeed;

  private float rotationSpeed;
  private float rotation = (float)-Math.PI;

  private float tiltSpeed;
  private float tilt = 1.20f;
  private float minTilt;
  private float maxTilt;

  private float sideSpeed;
  private float forwardSpeed;

  private Vector3 position;
  private Vector3 center;
  private Vector3 oldPosition;
  private Vector3 oldCenter;
  private Vector2 mouseRotationDrag;

  private boolean tiltBackward;
  private boolean forwardPressed;
  private boolean backwardPressed;
  private boolean leftPressed;
  private boolean rightPressed;
  private boolean rotateLeftPressed;
  private boolean rotateRightPressed;
  private boolean tiltForward;

  private boolean leftHotCorent;
  private boolean rightHotCorent;
  private boolean topHotCorent;
  private boolean bottomHotCorent;

  private boolean enabled = true;

  private Vector3 tempVec = new Vector3();
  private boolean rotateMouseButtonPressed;
  private float alpha;

  public RTSCameraController() {
    position = new Vector3(0,0,0);
    center = new Vector3(0, 0, 0);
    oldPosition = new Vector3(0,0,0);
    oldCenter = new Vector3(0, 0, 0);
    mouseRotationDrag = new Vector2();

    sideSpeed = 35.0f;
    forwardSpeed = 30f;
    rotationSpeed = 2.0f;
    tiltSpeed = 1.0f;

    minZoom = 3;
    maxZoom = 20;
    scrollSpeed = 100.0f;

    minTilt = 0.6f;
    maxTilt = (float) (Math.PI / 2) - 0.006f;

    currentZoom = maxZoom;
  }

  public void setCamera(PerspectiveCamera camera) {
    this.cam = camera;
  }

  public void update(final float delta) {
    if (this.cam == null) {
      return;
    }

    double offX = 0;
    double offY = 0;

    currentZoom += scrollAmount * delta * scrollSpeed;

    if (currentZoom > maxZoom)
      currentZoom = maxZoom;
    else if (currentZoom < minZoom)
      currentZoom = minZoom;

    if (leftPressed || leftHotCorent)
      offX -= sideSpeed * delta;
    else if (rightPressed || rightHotCorent)
      offX += sideSpeed * delta;

    if (forwardPressed || topHotCorent)
      offY -= forwardSpeed * delta;
    else if (backwardPressed || bottomHotCorent)
      offY += forwardSpeed * delta;

    if (rotateLeftPressed || rotateRightPressed) {
      if (rotateLeftPressed)
        rotation += delta * rotationSpeed;
      else
        rotation -= delta * rotationSpeed;
    }

    if (tiltForward || tiltBackward) {
      if (tiltForward)
        tilt += delta * tiltSpeed;
      else
        tilt -= delta * tiltSpeed;
    }

    if (rotateMouseButtonPressed) {
      tempVec.set(mouseRotationDrag.x, mouseRotationDrag.y, 0).sub(Gdx.input.getX(), Gdx.input.getY(), 0).nor();
      tilt += delta * -tempVec.y;
      rotation += delta * tempVec.x;
    }

    if (tilt > maxTilt)
      tilt = maxTilt;
    else if (tilt < minTilt)
      tilt = minTilt;

    center.x += offX * Math.cos(-rotation) + offY * Math.sin(rotation);
    center.z += offX * Math.sin(-rotation) + offY * Math.cos(rotation);

    position.x = center.x + (float) (currentZoom * Math.cos(tilt) * Math.sin(rotation));
    position.y = center.y + (float) (currentZoom * Math.sin(tilt));
    position.z = center.z + (float) (currentZoom * Math.cos(tilt) * Math.cos(rotation));

    if (!position.equals(oldPosition)) {
      oldPosition.set(position);
      alpha = 0;
    }
    if (!center.equals(oldCenter)) {
      oldCenter.set(center);
      alpha = 0;
    }

    alpha += LERP_SPEED * delta;
    alpha = Math.min(alpha, 1);

    this.cam.position.set(position);
    tempVec.set(center).sub(position).nor();

    cam.direction.set(tempVec);
    scrollAmount = 0;

  }

  public PerspectiveCamera getCamera() {
    return cam;
  }

  public void setCenter(float x, float z) {
    this.position.set(x,0,z);
    this.center.x = x;
    this.center.z = z;
  }

  @Override
  public boolean scrolled(int amount) {
    if (!enabled)
      return false;
    if (amount == 0 || rotateMouseButtonPressed) {
      return false;
    } else {
      scrollAmount = amount;
      return true;
    }
  }

  @Override
  public boolean keyDown(int keycode) {
    if (!enabled)
      return false;
    return changePressStateFor(keycode, true);
  }

  @Override
  public boolean keyUp(int keycode) {
    if (!enabled)
      return false;
    return changePressStateFor(keycode, false);
  }

  private boolean changePressStateFor(int keycode, boolean state) {
    boolean acted = false;

    if (BotLogic.inputManager.isEqual(InputManager.Action.CameraRotateLeft, keycode)) {
      rotateLeftPressed = state;
      acted = true;
    }

    if (BotLogic.inputManager.isEqual(InputManager.Action.CameraRotateRight, keycode)) {
      rotateRightPressed = state;
      acted = true;
    }

    if (BotLogic.inputManager.isEqual(InputManager.Action.CameraForward, keycode)) {
      forwardPressed = state;
      acted = true;
    }

    if (BotLogic.inputManager.isEqual(InputManager.Action.CameraBackward, keycode)) {
      backwardPressed = state;
      acted = true;
    }

    if (BotLogic.inputManager.isEqual(InputManager.Action.CameraLeft, keycode)) {
      leftPressed = state;
      acted = true;
    }

    if (BotLogic.inputManager.isEqual(InputManager.Action.CameraRight, keycode)) {
      rightPressed = state;
      acted = true;
    }

    if (BotLogic.inputManager.isEqual(InputManager.Action.CameraTiltBackward, keycode)) {
      tiltBackward = state;
      acted = true;
    }

    if (BotLogic.inputManager.isEqual(InputManager.Action.CameraTiltForward, keycode)) {
      tiltForward = state;
      acted = true;
    }

    return acted;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    //leftHotCorent = (screenX <= CAMERA_MOVE_PADDING);
    //rightHotCorent = (Gdx.graphics.getWidth() - CAMERA_MOVE_PADDING <= screenX);
    //topHotCorent = (screenY <= CAMERA_MOVE_PADDING);
    //bottomHotCorent = (Gdx.graphics.getHeight() - CAMERA_MOVE_PADDING <= screenY);

    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    if (!enabled)
      return false;
    if (button == Input.Buttons.RIGHT) {
      rotateMouseButtonPressed = true;
      mouseRotationDrag.set(screenX, screenY);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    if (!enabled)
      return false;
    if (button == Input.Buttons.RIGHT) {
      rotateMouseButtonPressed = false;
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return super.touchDragged(screenX, screenY, pointer);
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public float getZoom() {
    return currentZoom;
  }

  public void setZoom(float zoom) {
    this.currentZoom = zoom;
  }


  public void setRotation(float rotation) {
    this.rotation = rotation;
  }

  public void setTilt(float tilt) {
    this.tilt = tilt;
  }

  public Vector3 getCenter() {
    return center;
  }

  public float getTilt() {
    return tilt;
  }

  public float getRotation() {
    return rotation;
  }

  public float getCurrentZoom() {
    return currentZoom;
  }

  public void setCurrentZoom(float currentZoom) {
    this.currentZoom = currentZoom;
  }
}
