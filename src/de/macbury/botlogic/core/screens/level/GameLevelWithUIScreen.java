package de.macbury.botlogic.core.screens.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.Debug;
import de.macbury.botlogic.core.screens.level.file.LevelFile;
import de.macbury.botlogic.core.screens.menu.MenuBaseScreen;
import de.macbury.botlogic.core.ui.dialog.EndGameDialog;
import de.macbury.botlogic.core.ui.dialog.EndGameListener;

/**
 * Created by macbury on 05.04.14.
 */
public class GameLevelWithUIScreen extends GameLevelScreen implements EndGameListener {
  private Slider speedSlider;
  private EndGameDialog endGameDialog;
  private ImageButton objectiveButton;
  private ImageButton playPauseRobotButton;
  private ImageButton editCodeButton;
  private ImageButton exitButton;
  private InputMultiplexer multiplexer;
  private Table tableLayout;
  private Stage stage;

  public GameLevelWithUIScreen(LevelFile levelDef) {
    super(levelDef);
    this.stage        = new Stage(new ScreenViewport());
    this.tableLayout  = new Table();
    this.stage.addActor(tableLayout);

    if (Debug.TABLE)
      tableLayout.debug();
    tableLayout.setFillParent(true);

    this.multiplexer = new InputMultiplexer();
    multiplexer.addProcessor(cameraController);
    multiplexer.addProcessor(stage);

    this.endGameDialog = BotLogic.skin.builder.endGameDialog();
    this.endGameDialog.setListener(this);
    this.exitButton = BotLogic.skin.builder.redImageButton("icon_exit");
    exitButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        BotLogic.audio.click.play();
        endGameDialog.show(stage);
      }
    });

    this.editCodeButton       = BotLogic.skin.builder.blueImageButton("icon_code");
    this.playPauseRobotButton = BotLogic.skin.builder.blueImageButton("icon_play");
    this.objectiveButton      = BotLogic.skin.builder.blueImageButton("icon_objective");

    this.speedSlider          = BotLogic.skin.builder.slider(0f, 10f, 1f, false);

    Table controlerTable = new Table();
    controlerTable.setBackground(BotLogic.skin.getDrawable("background_dark"));
    controlerTable.row();
      controlerTable.add(BotLogic.skin.builder.normalLabel("Czas trwania rundy:")).left().pad(10, 15, 0, 15).expandX();
      controlerTable.add(BotLogic.skin.builder.normalLabel("0:00")).center().pad(10, 15, 0, 15).width(70).fill();
    controlerTable.row();
      controlerTable.add(BotLogic.skin.builder.normalLabel("Prędkość symulacji:")).left().pad(10, 15, 10, 15);
      controlerTable.add(BotLogic.skin.builder.normalLabel("1x")).center().pad(10, 15, 0, 15).width(70).fill();
    controlerTable.row();
      controlerTable.add(speedSlider).width(MenuBaseScreen.TOP_TOOLBAR_BUTTON_WIDTH * 3).fill().pad(0).space(0).colspan(2);

    tableLayout.row().top().left().space(0);
      tableLayout.add().colspan(5);
      tableLayout.add(exitButton).width(MenuBaseScreen.TOP_TOOLBAR_BUTTON_WIDTH).height(MenuBaseScreen.TOP_TOOLBAR_BUTTON_HEIGHT);
    tableLayout.row();
      tableLayout.add().expand().colspan(5);
    tableLayout.row();
      tableLayout.add(controlerTable).fill().colspan(3);
      tableLayout.add().expandX().colspan(2);
    tableLayout.row();
      tableLayout.add(editCodeButton).width(MenuBaseScreen.TOP_TOOLBAR_BUTTON_WIDTH).height(MenuBaseScreen.TOP_TOOLBAR_BUTTON_HEIGHT);
      tableLayout.add(objectiveButton).width(MenuBaseScreen.TOP_TOOLBAR_BUTTON_WIDTH).height(MenuBaseScreen.TOP_TOOLBAR_BUTTON_HEIGHT);
      tableLayout.add(playPauseRobotButton).width(MenuBaseScreen.TOP_TOOLBAR_BUTTON_WIDTH).height(MenuBaseScreen.TOP_TOOLBAR_BUTTON_HEIGHT);
      tableLayout.add().width(200);
      tableLayout.add().expandX();
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    stage.act(delta);
    stage.draw();

    if (Debug.TABLE)
      Table.drawDebug(stage);
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width,height);
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void show() {
    super.show();
    Gdx.input.setInputProcessor(multiplexer);
  }

  @Override
  public void dispose() {
    super.dispose();
    stage.dispose();
  }

  @Override
  public void onEndGameDialogOk(EndGameDialog dialog) {
    BotLogic.screens.goToMainMenu();
  }

  @Override
  public void onEndGameDialogCancel(EndGameDialog dialog) {

  }
}
