package de.macbury.botlogic.core.screens.level.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by macbury on 30.03.14.
 */
public class BlockWall extends Block {
  public static final char MAP_CHAR = 'X';
  @Override
  public boolean isPassable() {
    return false;
  }

  @Override
  public TextureRegion getTopTextureRegion(TextureAtlas atlas) {
    return atlas.findRegion("celing");
  }

  @Override
  public TextureRegion getSideTextureRegion(TextureAtlas atlas) {
    return atlas.findRegion("wall");
  }

  public static boolean is(Color color) {
    return color.equals(Color.BLACK);
  }
}
