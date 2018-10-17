package de.macbury.botlogic.core.entites;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;

/**
 * Created by macbury on 01.04.14.
 */
public interface EntityDecalRenderable {
  public abstract void renderBatch(DecalBatch batch, Environment env);
}
