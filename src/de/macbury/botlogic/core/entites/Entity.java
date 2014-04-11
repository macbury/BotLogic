package de.macbury.botlogic.core.entites;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 31.03.14.
 */
public abstract class Entity implements Disposable {
  public Vector3 position;
  public float rotation;
  public Vector3 scale;
  public Entity() {
    position   = new Vector3();
    rotation    = 0.0f;
    scale      = new Vector3(1,1,1);
  }
  public float getRotation() {
    return rotation;
  }
  public abstract void update(double delta);

  public abstract void reset();
}
