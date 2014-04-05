package de.macbury.botlogic.core.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.Debug;
import de.macbury.botlogic.core.audio.AudioManager;
import de.macbury.botlogic.core.levels.file.LevelFile;
import de.macbury.botlogic.core.ui.FlatColors;
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
    BotLogic.audio.music.play();
  }

  @Override
  public void hide() {

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
  }

  @Override
  protected void onExitButtonClicked() {
    BotLogic.audio.click.play();
    Gdx.app.exit();
  }
}
