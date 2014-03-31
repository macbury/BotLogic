package de.macbury.botlogic.core.entites;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 31.03.14.
 */
public abstract class Entity implements Disposable {
  public Vector3 position;
  public Quaternion rotatation;
  public Vector3 scale;
  public Entity() {
    position   = new Vector3();
    rotatation = new Quaternion();
    scale      = new Vector3(1,1,1);
  }

  public abstract void update(double delta);
  public abstract void render(ModelBatch batch, Environment env);

}
