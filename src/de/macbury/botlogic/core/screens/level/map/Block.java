package de.macbury.botlogic.core.screens.level.map;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by macbury on 30.03.14.
 */
public abstract class Block {
  public static int BLOCK_SIZE = 1;
  private int x = 0;
  private int y = 0;
  private int z = -1;

  public Block() {

  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public abstract boolean isPassable();
  public abstract TextureRegion getTopTextureRegion(TextureAtlas atlas);
  public abstract TextureRegion getSideTextureRegion(TextureAtlas atlas);
  public int getZ() {
    return z;
  }

  public void setZ(int z) {
    this.z = z;
  }
}
