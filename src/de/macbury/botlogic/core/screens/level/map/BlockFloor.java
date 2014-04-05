package de.macbury.botlogic.core.screens.level.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by macbury on 30.03.14.
 */
public class BlockFloor extends Block {
  public static final char MAP_CHAR = '0';
  @Override
  public boolean isPassable() {
    return true;
  }

  @Override
  public TextureRegion getTopTextureRegion(TextureAtlas atlas) {
    return atlas.findRegion("floor");
  }

  @Override
  public TextureRegion getSideTextureRegion(TextureAtlas atlas) {
    return atlas.findRegion("floor_end");
  }

  public static boolean is(Color color) {
    return color.equals(Color.WHITE);
  }
}
