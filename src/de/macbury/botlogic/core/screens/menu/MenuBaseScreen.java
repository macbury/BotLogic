package de.macbury.botlogic.core.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.BotLogicDebug;
import de.macbury.botlogic.core.ui.colors.FlatColors;

/**
 * Created by macbury on 05.04.14.
 */
public abstract class MenuBaseScreen implements Screen {
  public static final float TOP_TOOLBAR_BUTTON_WIDTH   = 100;
  public static final float TOP_TOOLBAR_BUTTON_HEIGHT  = 64;
  protected ImageButton exitButton;
  protected ImageButton settingsButton;
  protected Stage stage;
  protected Table tableLayout;
  private ShapeRenderer renderer = new ShapeRenderer();

  public MenuBaseScreen() {
    this.stage        = new Stage(new ScreenViewport());
    this.tableLayout  = new Table();

    renderer.setAutoShapeType(true);

    if (BotLogicDebug.TABLE)
      tableLayout.debug();
    tableLayout.setFillParent(true);

    tableLayout.row().top().left();

    Image image = BotLogic.skin.builder.image("logo");
    tableLayout.add(image).pad(20, 20, 0, 0).expandX();

    this.settingsButton = BotLogic.skin.builder.redImageButton("icon_settings");
    tableLayout.add(settingsButton).width(TOP_TOOLBAR_BUTTON_WIDTH).height(TOP_TOOLBAR_BUTTON_HEIGHT);
    this.exitButton = BotLogic.skin.builder.redImageButton("icon_exit");
    tableLayout.add(exitButton).width(TOP_TOOLBAR_BUTTON_WIDTH).height(TOP_TOOLBAR_BUTTON_HEIGHT);
    exitButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        MenuBaseScreen.this.onExitButtonClicked();
      }
    });

    settingsButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        MenuBaseScreen.this.onSettingsButtonClicked();
      }
    });

    this.buildLayout();

    tableLayout.setTransform(true);
    stage.addActor(tableLayout);
  }

  private void onSettingsButtonClicked() {
    Dialog dialog = BotLogic.skin.builder.settingsDialog();

    dialog.show(stage);
    BotLogic.audio.click.play();
  }

  protected abstract void onExitButtonClicked();

  protected abstract void buildLayout();

  @Override
  public void render(float v) {
    Gdx.gl.glClearColor(FlatColors.MIDNIGHT_BLUE.r, FlatColors.MIDNIGHT_BLUE.g , FlatColors.MIDNIGHT_BLUE.b , FlatColors.MIDNIGHT_BLUE.a);
    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    stage.act();
    stage.draw();

    if (BotLogicDebug.TABLE) {
      renderer.begin();
      stage.getRoot().debug().drawDebug(renderer);
      renderer.end();
    }
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(stage);
  }

  @Override
  public void dispose() {
    stage.dispose();
  }
}
