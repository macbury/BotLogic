package de.macbury.botlogic.core.entites;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by macbury on 31.03.14.
 */
public abstract class ModelEntity extends Entity implements EntityModelRenderable {
  private static final String TAG = "ModelEntity";

  public enum Direction {
    North, South, Weast, East
  }
  public ModelInstance instance;
  public Vector3 startPosition = new Vector3();

  public ModelEntity(Model model) {
    super();
    instance = new ModelInstance(model);
  }

  @Override
  public void update(double delta) {
    instance.transform.idt();
    instance.transform.translate(position);
    instance.transform.scale(scale.x, scale.y, scale.z);
    instance.transform.rotate(Vector3.Y, rotation);
  }

  @Override
  public void renderModel(ModelBatch batch, Environment env) {
    batch.render(instance, env);
  }

  @Override
  public void reset() {
    this.rotation = 0;
    this.position.set(startPosition);
    instance.transform.idt();
  }

  Quaternion tempDirectionRotation = new Quaternion();
  public Direction getDirection() {
    instance.transform.getRotation(tempDirectionRotation, true);
    int yaw = Math.round(tempDirectionRotation.getYaw());
    if (yaw == 0) {
      return Direction.South;
    } else if (yaw == -90) {
      return Direction.East;
    } else if (yaw == 90) {
      return Direction.Weast;
    } else if (yaw == 180 || yaw == -180)
      return Direction.North;
    return null;
  }

  public static void directionToVector(Direction dir, Vector3 vector) {
    switch (dir) {
      case South:
        vector.set(0,0,1);
      break;
      case North:
        vector.set(0,0,-1);
      break;
      case Weast:
        vector.set(1,0,0);
        break;
      case East:
        vector.set(-1,0,0);
      break;
    }
  }

  @Override
  public void dispose() {

  }

}
