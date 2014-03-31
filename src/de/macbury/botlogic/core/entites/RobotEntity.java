package de.macbury.botlogic.core.entites;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import de.macbury.botlogic.core.tween.ModelEntityAccessor;

/**
 * Created by macbury on 31.03.14.
 */
public class RobotEntity extends ModelEntity {
  private static final float ROTATE_SPEED = 0.5f;
  public RobotEntity(Model model) {
    super(model);
    scale.set(0.15f, 0.15f, 0.15f);
  }

  @Override
  public void update(double delta) {
    super.update(delta);
  }

  public Tween getRotationTween(int direction) {
    float targetRotation = this.rotatation + direction * 90f;
    return Tween.to(this, ModelEntityAccessor.ROTATE, ROTATE_SPEED).target(targetRotation);
  }
}
