package de.macbury.botlogic.core.screens.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.Debug;
import de.macbury.botlogic.core.screens.level.file.LevelFile;
import de.macbury.botlogic.core.screens.menu.MenuBaseScreen;

/**
 * Created by macbury on 05.04.14.
 */
public class GameLevelWithUIScreen extends GameLevelScreen {
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

    this.exitButton = BotLogic.skin.builder.redImageButton("icon_exit");
    exitButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        BotLogic.audio.click.play();
        BotLogic.screens.goToMainMenu();
      }
    });

    this.editCodeButton       = BotLogic.skin.builder.blueImageButton("icon_code");
    this.playPauseRobotButton = BotLogic.skin.builder.blueImageButton("icon_play");
    this.objectiveButton      = BotLogic.skin.builder.blueImageButton("icon_objective");

    Stack speedControl        = new Stack();
    speedControl.add(BotLogic.skin.builder.slider(0f, 10f, 0.1f, false));

    tableLayout.row().top().left();
      tableLayout.add().colspan(5);
      tableLayout.add(exitButton).width(MenuBaseScreen.TOP_TOOLBAR_BUTTON_WIDTH).height(MenuBaseScreen.TOP_TOOLBAR_BUTTON_HEIGHT);
    tableLayout.row();
      tableLayout.add().expand().colspan(5);
    tableLayout.row();
      tableLayout.add(speedControl).fill().colspan(3);
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
}
