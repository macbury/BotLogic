package de.macbury.botlogic.core.entites;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import de.macbury.botlogic.core.tween.ModelEntityAccessor;

/**
 * Created by macbury on 31.03.14.
 */
public abstract class ModelEntity extends Entity implements EntityModelRenderable {
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
    instance.transform.rotate(Vector3.Y, rotatation);
  }

  @Override
  public void renderModel(ModelBatch batch, Environment env) {
    batch.render(instance, env);
  }

  @Override
  public void reset() {
    this.rotatation = 0;
    this.position.set(startPosition);
  }

  @Override
  public void dispose() {

  }

}
