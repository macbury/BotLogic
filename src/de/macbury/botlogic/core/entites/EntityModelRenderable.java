package de.macbury.botlogic.core.entites;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

/**
 * Created by macbury on 01.04.14.
 */
public interface EntityModelRenderable {

  public abstract void renderModel(ModelBatch batch, Environment env);
}
