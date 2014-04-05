package de.macbury.botlogic.core.ui.tiles;

import de.macbury.botlogic.core.levels.file.LevelFile;

/**
 * Created by macbury on 05.04.14.
 */
public interface LevelTileListener {

  public void onPlayClick(LevelFile level, LevelTile tile);
}
