package de.macbury.botlogic.core.entites;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by macbury on 31.03.14.
 */
public class RobotEntity extends ModelEntity {
  public RobotEntity(Model model) {
    super(model);
    scale.set(0.15f, 0.15f, 0.15f);
  }

  @Override
  public void update(double delta) {
    rotatation.setFromAxis(Vector3.Y, 34);
    super.update(delta);
  }
}
