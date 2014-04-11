package de.macbury.botlogic.core.entites;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import de.macbury.botlogic.core.tween.ModelEntityAccessor;

/**
 * Created by macbury on 31.03.14.
 */
public class RobotEntity extends ModelEntity {
  private static final float ROTATE_SPEED    = 0.5f;
  private static final float WHEEL_SPEED    = 300f;
  private static final String ANIMATION_IDLE = "Idle";
  private Node backRightBone;
  private Node backLeftBone;
  private Node frontLeftBone;
  private Node frontRightBone;
  private AnimationController animation;
  public int steps = 0;
  public int rotations = 0;

  private float rotateLeftWheelsDirection;
  private float rotateRightWheelsDirection;

  public RobotEntity(Model model) {
    super(model);

    scale.set(0.35f, 0.35f, 0.35f);
    animation = new AnimationController(instance);
    animation.setAnimation(ANIMATION_IDLE);

    this.frontRightBone = instance.getNode("FrontRightWheel");
    this.frontLeftBone  = instance.getNode("FrontLeftWheel");

    this.backLeftBone   = instance.getNode("BackLeftWheel");
    this.backRightBone = instance.getNode("BackRightWheel");
  }

  @Override
  public void update(double delta) {
    super.update(delta);

    animation.update((float)delta);
    if (rotateLeftWheelsDirection != 0.0f) {
      setLeftWheelsRotate((float)( WHEEL_SPEED * delta) * rotateLeftWheelsDirection);
    }

    if (rotateRightWheelsDirection != 0.0f) {
      setRightWheelsRotate((float) (WHEEL_SPEED * delta) * rotateRightWheelsDirection);
    }

    instance.calculateTransforms();
    /*Node head = instance.getNode("Bone_001");
    head.globalTransform.setToLookAt(Vector3.Zero, Vector3.Z);
    head.calculateBoneTransforms(true);*/
  }

  public void setLeftWheelsRotate(float angle) {
    frontLeftBone.localTransform.rotate(Vector3.Y, angle);
    backLeftBone.localTransform.rotate(Vector3.Y, angle);
  }

  public void setRightWheelsRotate(float angle) {
    frontRightBone.localTransform.rotate(Vector3.Y, angle);
    backRightBone.localTransform.rotate(Vector3.Y, angle);
  }

  @Override
  public void reset() {
    super.reset();
    steps     = 0;
    rotations = 0;

    rotateLeftWheelsDirection  = 0f;
    rotateRightWheelsDirection = 0f;
  }

  public Tween getRotationTween(int direction) {
    float targetRotation = this.rotation + direction * 90f;
    return Tween.to(this, ModelEntityAccessor.ROTATE, ROTATE_SPEED).target(targetRotation);
  }

  public float isRotateLeftWheelsDirection() {
    return rotateLeftWheelsDirection;
  }

  public void setRotateLeftWheelsDirection(float rotateLeftWheelsDirection) {
    this.rotateLeftWheelsDirection = rotateLeftWheelsDirection;
  }

  public float isRotateRightWheelsDirection() {
    return rotateRightWheelsDirection;
  }

  public void setRotateRightWheelsDirection(float rotateRightWheelsDirection) {
    this.rotateRightWheelsDirection = rotateRightWheelsDirection;
  }
}
