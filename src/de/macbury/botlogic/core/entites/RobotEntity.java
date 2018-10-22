package de.macbury.botlogic.core.entites;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.tween.ModelEntityAccessor;
import de.macbury.botlogic.core.tween.RobotEntityAccessor;


/**
 * Created by macbury on 31.03.14.
 */
public class RobotEntity extends ModelEntity implements EntityDecalRenderable {
  private static final float ROTATE_SPEED    = 0.5f;
  private static final float WHEEL_SPEED    = 300f;
  private static final String ANIMATION_IDLE = "Idle";
  private static final String TAG = "RobotEntity";
  private static final float SONAR_OFFSET_Y = 0.5f;
  private Decal sonarSprite;
  private Node headBone;
  private Node backRightBone;
  private Node backLeftBone;
  private Node frontLeftBone;
  private Node frontRightBone;
  private AnimationController animation;
  private float rotateLeftWheelsDirection;
  private float rotateRightWheelsDirection;
  private float sonarPingAlpha = 0.0f;
  public LedEntity led;

  private Vector3 tempVec = new Vector3();
  public RobotEntity(Model model) {
    super(model);

    //scale.set(0.35f, 0.35f, 0.35f);
    animation = new AnimationController(instance);
    animation.setAnimation(ANIMATION_IDLE);

    this.headBone       = instance.getNode("Head");

    this.frontRightBone = instance.getNode("FrontRightWheel");
    this.frontLeftBone  = instance.getNode("FrontLeftWheel");

    this.backLeftBone   = instance.getNode("BackLeftWheel");
    this.backRightBone  = instance.getNode("BackRightWheel");
    this.sonarSprite    = BotLogic.sprites.sonarDecal();

  }

  @Override
  public void renderBatch(DecalBatch batch, Environment env) {
    batch.add(sonarSprite);
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

    if (led != null) {
      headBone.localTransform.getTranslation(tempVec);
      led.position.set(position).add(0, 0.92f, 0);
      led.rotation = rotation;
    }


    sonarSprite.setPosition(position.x, position.y + SONAR_OFFSET_Y, position.z);
    sonarSprite.setColor(1,1,1, sonarPingAlpha);
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

    rotateLeftWheelsDirection  = 0f;
    rotateRightWheelsDirection = 0f;
    sonarSprite.setWidth(0);
    sonarSprite.setHeight(0);
  }

  public Tween getRotationTween(int direction) {
    float targetRotation = this.rotation + direction * 90f;
    return Tween.to(this, ModelEntityAccessor.ROTATE, ROTATE_SPEED).target(targetRotation);
  }

  public Tween getTranslationTween(Vector3 targetPosition) {
    return Tween.to(level.robot, ModelEntityAccessor.POSITION_XZ, 0.5f).target(targetPosition.x, targetPosition.z);
  }

  public Timeline getSonarPingTimeline(int distance) {
    sonarSprite.setWidth(0);
    sonarSprite.setHeight(0);
    sonarPingAlpha = 1;
    return Timeline.createSequence()
            .push(Tween.to(this, RobotEntityAccessor.SONAR_SIZE, 1f).target(distance, distance, 0));
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


  public Decal getSonarSprite() {
    return sonarSprite;
  }

  public float getSonarPingAlpha() {
    return sonarPingAlpha;
  }

  public void setSonarPingAlpha(float sonarPingAlpha) {
    this.sonarPingAlpha = sonarPingAlpha;
  }
}
