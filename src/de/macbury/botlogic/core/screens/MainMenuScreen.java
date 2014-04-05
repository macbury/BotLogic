package de.macbury.botlogic.core.screens;

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
public class MainMenuScreen implements Screen, LevelTileListener {
  private static final float TOP_TOOLBAR_BUTTON_WIDTH   = 100;
  private static final float TOP_TOOLBAR_BUTTON_HEIGHT  = 64;
  private TileScrollPane tileScrollPane;
  private Stage stage;
  private Table tableLayout;

  public MainMenuScreen() {
    this.stage        = new Stage(new ScreenViewport());
    this.tableLayout  = new Table();

    //if (Debug.TABLE)
    //  tableLayout.debug();
    tableLayout.setFillParent(true);

    tableLayout.row().top().left();

    Image image = BotLogic.skin.builder.image("logo");
    tableLayout.add(image).pad(20, 20, 0, 0).expandX();

    ImageButton settingsButton = BotLogic.skin.builder.redImageButton("icon_settings");
    tableLayout.add(settingsButton).width(TOP_TOOLBAR_BUTTON_WIDTH).height(TOP_TOOLBAR_BUTTON_HEIGHT);
    ImageButton exitButton = BotLogic.skin.builder.redImageButton("icon_exit");
    tableLayout.add(exitButton).width(TOP_TOOLBAR_BUTTON_WIDTH).height(TOP_TOOLBAR_BUTTON_HEIGHT);

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
    tableLayout.setTransform(true);
    stage.addActor(tableLayout);
  }

  @Override
  public void render(float v) {
    Gdx.gl.glClearColor(FlatColors.MIDNIGHT_BLUE.r, FlatColors.MIDNIGHT_BLUE.g , FlatColors.MIDNIGHT_BLUE.b , FlatColors.MIDNIGHT_BLUE.a);
    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    stage.act();
    stage.draw();

    if (Debug.TABLE)
      Table.drawDebug(stage);
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);

    //tableLayout.setWidth(width);
    //tableLayout.setHeight(height);

    //tableLayout.invalidate();
  }


  @Override
  public void show() {
    Gdx.input.setInputProcessor(stage);
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
  public void dispose() {
    stage.dispose();
  }

  @Override
  public void onPlayClick(LevelFile level, LevelTile tile) {
    BotLogic.audio.click.play();
  }
}
