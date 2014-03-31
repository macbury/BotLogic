package de.macbury.botlogic.core.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Created by macbury on 28.03.14.
 */
public class PlaygroundLevel extends BaseLevel {

  public PlaygroundLevel(String path) {
    super(path);
  }

  public PlaygroundLevel() {
    super("playground");
  }

  @Override
  public boolean winCondition() {
    return false;
  }
}
