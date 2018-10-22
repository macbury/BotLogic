package de.macbury.botlogic.core.screens.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.BotLogicDebug;
import de.macbury.botlogic.core.runtime.ScriptRunner;
import de.macbury.botlogic.core.runtime.ScriptRuntimeListener;
import de.macbury.botlogic.core.screens.level.file.LevelFile;
import de.macbury.botlogic.core.screens.menu.MenuBaseScreen;
import de.macbury.botlogic.core.ui.button.StateImageButton;
import de.macbury.botlogic.core.ui.code_editor.CodeEditorView;
import de.macbury.botlogic.core.ui.dialog.EndGameDialog;
import de.macbury.botlogic.core.ui.dialog.EndGameListener;
import de.macbury.botlogic.core.ui.labels.TimerLabel;
import de.macbury.botlogic.core.ui.stage.FlatStage;
import org.mozilla.javascript.RhinoException;

/**
 * Created by macbury on 05.04.14.
 */
public class GameLevelWithUIScreen extends GameLevelScreen implements EndGameListener, ScriptRuntimeListener {
  private TimerLabel timerLabel;
  private CodeEditorView codeEditorView;
  private Label levelSpeedLabel;
  private Slider speedSlider;
  private EndGameDialog endGameDialog;
  private ImageButton objectiveButton;
  private StateImageButton playPauseRobotButton;
  private ImageButton editCodeButton;
  private ImageButton exitButton;
  private InputMultiplexer multiplexer;
  private Table tableLayout;
  private FlatStage stage;
  private ShapeRenderer renderer = new ShapeRenderer();


  public GameLevelWithUIScreen(LevelFile levelDef) {
    super(levelDef);
    this.stage        = new FlatStage(new ScreenViewport());
    this.tableLayout  = new Table();

    if (BotLogicDebug.TABLE)
      tableLayout.debug();
    tableLayout.setFillParent(true);

    this.multiplexer = new InputMultiplexer();
    multiplexer.addProcessor(stage);
    multiplexer.addProcessor(cameraController);
    multiplexer.addProcessor(new InputAdapter() {
      @Override
      public boolean keyUp(int keycode) {
        if (Input.Keys.F5 == keycode) {
          GameLevelWithUIScreen.this.playPauseButtonClicked();
          return true;
        } else {
          return super.keyUp(keycode);
        }
      }
    });

    this.timerLabel     = BotLogic.skin.builder.timerLabel();
    this.endGameDialog  = BotLogic.skin.builder.endGameDialog();
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
    this.playPauseRobotButton = BotLogic.skin.builder.twoStateButton("icon_play", "icon_stop");
    this.objectiveButton      = BotLogic.skin.builder.blueImageButton("icon_objective");

    this.editCodeButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        GameLevelWithUIScreen.this.editCodeButtonClicked();
      }
    });

    this.playPauseRobotButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        GameLevelWithUIScreen.this.playPauseButtonClicked();
      }
    });

    this.speedSlider          = BotLogic.skin.builder.slider(0f, 9f, 1f, false);
    speedSlider.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent changeEvent, Actor actor) {
        levelSpeedLabel.setText((speedSlider.getValue() + 1) + "x");
        setSpeed((int)(speedSlider.getValue() + 1));
      }
    });
    this.levelSpeedLabel      = BotLogic.skin.builder.normalLabel("1.0x");

    String body = "var robot;\n";
    for(LevelFile.LevelFeature feature : levelDef.getFeatures()) {
      body += Gdx.files.internal("sketches/features/"+feature.toString()+".js").readString() + "\n";
    }

    body += "\n" + Gdx.files.internal("sketches/new/blank.js").readString();
    body += "\n";

    this.codeEditorView = new CodeEditorView();

    Table controlerTable = new Table();
    controlerTable.setBackground(BotLogic.skin.getDrawable("background_dark"));
    controlerTable.row();
      controlerTable.add(BotLogic.skin.builder.normalLabel("Czas trwania rundy:")).left().pad(10, 15, 0, 15).expandX();
      controlerTable.add(timerLabel).center().pad(10, 15, 0, 15).width(70).fill();
    controlerTable.row();
      controlerTable.add(BotLogic.skin.builder.normalLabel("Prędkość symulacji:")).left().pad(10, 15, 10, 15);
      controlerTable.add(levelSpeedLabel).center().pad(10, 15, 0, 15).width(70).fillX();
    controlerTable.row();
      controlerTable.add(speedSlider).width(MenuBaseScreen.TOP_TOOLBAR_BUTTON_WIDTH * 3).fill().pad(0).space(0).colspan(2);

    tableLayout.row().top().left().space(0);
      tableLayout.add(codeEditorView).colspan(4).expand().fill();
      tableLayout.add(exitButton).width(MenuBaseScreen.TOP_TOOLBAR_BUTTON_WIDTH).height(MenuBaseScreen.TOP_TOOLBAR_BUTTON_HEIGHT);
    tableLayout.row();
      tableLayout.add(controlerTable).fill().colspan(3);
      tableLayout.add().expandX().colspan(1);
    tableLayout.row();
      tableLayout.add(editCodeButton).width(MenuBaseScreen.TOP_TOOLBAR_BUTTON_WIDTH).height(MenuBaseScreen.TOP_TOOLBAR_BUTTON_HEIGHT);
      tableLayout.add(objectiveButton).width(MenuBaseScreen.TOP_TOOLBAR_BUTTON_WIDTH).height(MenuBaseScreen.TOP_TOOLBAR_BUTTON_HEIGHT);
      tableLayout.add(playPauseRobotButton).width(MenuBaseScreen.TOP_TOOLBAR_BUTTON_WIDTH).height(MenuBaseScreen.TOP_TOOLBAR_BUTTON_HEIGHT);
      tableLayout.add().expandX().colspan(1);
    this.stage.addActor(tableLayout);

    getController().getRobotScriptRunner().addListener(this);
    getController().getMissionScriptRunner().addListener(this);

    codeEditorView.setText(body);
    codeEditorView.getTextArea().pageDown();
    setEditorVisibility(false);


  }

  public void setEditorVisibility(boolean visibility) {
    codeEditorView.setVisible(visibility);

    if (!codeEditorView.isVisible()) {
      codeEditorView.unfocus();
    } else {
      codeEditorView.focus();
    }
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    timerLabel.tick(delta, getSpeed());
    stage.act(delta);
    stage.draw();

    if (BotLogicDebug.TABLE) {
      renderer.begin();
      stage.getRoot().debug().drawDebug(renderer);
      renderer.end();
    }
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width,height);
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void show() {
    super.show();
    BotLogic.audio.gameMusic.play();
    Gdx.input.setInputProcessor(multiplexer);
  }

  @Override
  public void hide() {
    getController().getRobotScriptRunner().removeListener(this);
    super.hide();
    BotLogic.audio.gameMusic.stop();
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

  private void editCodeButtonClicked() {
    BotLogic.audio.click.play();
    setEditorVisibility(!codeEditorView.isVisible());
  }

  private void playPauseButtonClicked() {
    BotLogic.audio.click.play();
    if (getController().isRunning()) {
      getController().stop();
    } else {
      getController().run(codeEditorView.getText());
    }
  }

  @Override
  public void onScriptStart(ScriptRunner runner) {
    if (getController().getRobotScriptRunner() == runner) {
      setEditorVisibility(false);
      playPauseRobotButton.setOn(true);
      editCodeButton.setDisabled(true);
      codeEditorView.getTextArea().clearError();

      timerLabel.setRunning(true);
    }

  }

  @Override
  public void onScriptInterput(ScriptRunner runner) {

  }

  @Override
  public void onScriptError(ScriptRunner runner, RhinoException error) {
    codeEditorView.getTextArea().setErrorLine(error.lineNumber(), error.columnNumber(), error.getLocalizedMessage());
    setEditorVisibility(true);

    error.printStackTrace();
    BotLogic.audio.codeError.play();
  }

  @Override
  public void onScriptFinish(ScriptRunner runner) {
    if (getController().getRobotScriptRunner() == runner) {
      timerLabel.setRunning(false);
      playPauseRobotButton.setOn(false);
      editCodeButton.setDisabled(false);
    }
  }
}
