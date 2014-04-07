package de.macbury.botlogic.core.screens.menu;

import com.badlogic.gdx.Gdx;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.screens.level.file.LevelFile;
import de.macbury.botlogic.core.ui.tiles.LevelTile;
import de.macbury.botlogic.core.ui.tiles.LevelTileListener;
import de.macbury.botlogic.core.ui.tiles.TileScrollPane;

/**
 * Created by macbury on 03.04.14.
 */
public class MainMenuScreen extends MenuBaseScreen implements LevelTileListener {
  private TileScrollPane tileScrollPane;

  @Override
  protected void buildLayout() {
    tableLayout.row();
    tableLayout.add().expandX().colspan(4);

    tableLayout.row();

    this.tileScrollPane = BotLogic.skin.builder.getTileScrollPane();

    for (LevelFile lf : LevelFile.list()) {
      LevelTile tile = new LevelTile(lf, BotLogic.skin);

      tile.setListener(this);
      tileScrollPane.addTile(tile);
    }

    tableLayout.add(tileScrollPane).expand().fill().colspan(4);
  }

  @Override
  public void show() {
    super.show();
    BotLogic.audio.mainMenuMusic.play();
    BotLogic.skin.setPointerCursor();
  }

  @Override
  public void hide() {
    BotLogic.audio.mainMenuMusic.stop();
  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void onPlayClick(LevelFile level, LevelTile tile) {
    BotLogic.audio.click.play();
    BotLogic.screens.startLevel(level);
  }

  @Override
  protected void onExitButtonClicked() {
    BotLogic.audio.click.play();
    Gdx.app.exit();
  }
}
