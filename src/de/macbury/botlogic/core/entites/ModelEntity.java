package de.macbury.botlogic.core.entites;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

/**
 * Created by macbury on 31.03.14.
 */
public abstract class ModelEntity extends Entity {
  public ModelInstance instance;

  public ModelEntity(Model model) {
    super();
    instance = new ModelInstance(model);
  }

  @Override
  public void update(double delta) {
    instance.transform.set(position, rotatation);
    instance.transform.scale(scale.x, scale.y, scale.z);
  }

  @Override
  public void render(ModelBatch batch, Environment env) {
    batch.render(instance, env);
  }

  @Override
  public void dispose() {

  }
}
