package de.macbury.botlogic.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.Debug;
import de.macbury.botlogic.core.levels.file.LevelFile;
import de.macbury.botlogic.core.ui.FlatColors;
import de.macbury.botlogic.core.ui.FlatSkin;

/**
 * Created by macbury on 03.04.14.
 */
public class MainMenuScreen implements Screen {
  private static final float TOP_TOOLBAR_BUTTON_WIDTH   = 100;
  private static final float TOP_TOOLBAR_BUTTON_HEIGHT  = 64;
  private Stage stage;
  private Table tableLayout;

  public MainMenuScreen() {
    this.stage        = new Stage(new ScreenViewport());
    this.tableLayout  = new Table();

    if (Debug.TABLE)
      tableLayout.debug();
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

    ScrollPane tileScrollPane = BotLogic.skin.builder.getTileScrollPane();
    tileScrollPane.setFillParent(false);
    tileScrollPane.setFadeScrollBars(false);
    tileScrollPane.setFlickScroll(false);

    Table table = new Table();
    table.row().top().left();
    if (Debug.TABLE)
      table.debug();

    for (LevelFile lf : LevelFile.list()) {
      Table cellTable = new Table();

      cellTable.setBackground(BotLogic.skin.getDrawable("background_light"));
      cellTable.row();
      cellTable.add(BotLogic.skin.builder.titleLabel(lf.getName())).expandX().fillX().pad(30,25,25,25).colspan(2);
      cellTable.row();

      ScrollPane descriptionScrollPane = BotLogic.skin.builder.getTileScrollPane();
      descriptionScrollPane.setFlickScroll(false);
      Label descriptionLabel = BotLogic.skin.builder.normalLabel(lf.getDescription());
      descriptionLabel.setWrap(true);

      descriptionScrollPane.setWidget(descriptionLabel);

      cellTable.add(descriptionScrollPane).padLeft(30).fill().colspan(2).expand();
      cellTable.row();
      cellTable.add().expandX();
      cellTable.add(BotLogic.skin.builder.redTextButton("Zagraj")).height(44).width(116).pad(20, 15, 20, 15);
      if (Debug.TABLE)
        cellTable.debug();
      table.add(cellTable).pad(20, 15, 50, 10).width(400).height(450);
    }

    tileScrollPane.setWidget(table);
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
}
