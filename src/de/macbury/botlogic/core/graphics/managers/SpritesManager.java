package de.macbury.botlogic.core.graphics.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.decals.Decal;

/**
 * Created by macbury on 12.04.14.
 */
public class SpritesManager {
  public TextureAtlas effectsAtlas;

  public SpritesManager() {
    this.effectsAtlas = new TextureAtlas(Gdx.files.internal("sprites/effects.atlas"));
  }

  public Decal sonarDecal() {
    Decal decal = Decal.newDecal(this.effectsAtlas.findRegion("sonar_ping"), true);
    decal.setWidth(1);
    decal.setHeight(1);
    decal.rotateX(90);
    return decal;
  }
}
