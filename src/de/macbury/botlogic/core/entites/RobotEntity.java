package de.macbury.botlogic.core.entites;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
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
    scale.set(0.2f, 0.2f, 0.2f);
  }
  float tempRot = 0;
  @Override
  public void update(double delta) {
    super.update(delta);
    /*Node node = instance.getNode("PraweKolo");
    node.rotation.setFromAxis(Vector3.X, tempRot++);
    instance.calculateTransforms();

    Node head = instance.getNode("Bone_001");
    head.globalTransform.setToLookAt(Vector3.Zero, Vector3.Z);
    head.calculateBoneTransforms(true);*/
  }

  public Tween getRotationTween(int direction) {
    float targetRotation = this.rotatation + direction * 90f;
    return Tween.to(this, ModelEntityAccessor.ROTATE, ROTATE_SPEED).target(targetRotation);
  }
}
