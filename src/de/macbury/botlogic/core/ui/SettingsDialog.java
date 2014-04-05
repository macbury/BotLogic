package de.macbury.botlogic.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.Debug;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * Created by macbury on 05.04.14.
 */
public class SettingsDialog extends Dialog {
  private static final String TAG = "SettingsDialog";
  private static final float PADDING_VERITICAL  = 10;
  private static final float PADDING_HORIZONTAL = 20;
  private static final float OPTION_WIDTH = 300;
  private static final float LABEL_WIDTH = 180;
  private static final float RESOLUTION_SCROLL_PANE_HEIGHT = 120;
  private static final float PADDING_BUTTONS_BOTTOM = 25;
  private DisplayMode[] resModes;
  private CheckBox fullScreenCheckBox;
  private TextButton cancelButton;
  private List<String> resolutionListView;
  private TextButton saveButton;

  public SettingsDialog(WindowStyle windowStyle) {
    super("", windowStyle);

    this.saveButton   = BotLogic.skin.builder.redTextButton("Zapisz zmiany");
    this.cancelButton = BotLogic.skin.builder.redTextButton("Anuluj");
    this.fullScreenCheckBox = BotLogic.skin.builder.checkbox("");
    fullScreenCheckBox.setChecked(Gdx.graphics.isFullscreen());

    saveButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        SettingsDialog.this.saveButtonClicked();
      }
    });

    cancelButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        BotLogic.audio.click.play();
        SettingsDialog.this.hide();
      }
    });

    ScrollPane scrollPane = BotLogic.skin.builder.getScrollPaneWithoutMouseScroll();
    this.resolutionListView = new List<String>(BotLogic.skin.listStyle);
    scrollPane.setFadeScrollBars(false);

    this.resModes = new DisplayMode[0];
    try {
      resModes = Display.getAvailableDisplayModes();
    } catch (LWJGLException e) {
      e.printStackTrace();
    }

    Array<String> resArrays = new Array<String>();
    int selectedResIndex = 0;

    for(int i = 0; i < resModes.length; i++) {
      DisplayMode displayMode = resModes[i];
      resArrays.add(displayMode.getWidth() + "x"+displayMode.getHeight());
      if (Gdx.graphics.getWidth() == displayMode.getWidth() && Gdx.graphics.getHeight() == displayMode.getHeight()) {
        selectedResIndex = i;
      }
    }

    resolutionListView.setItems(resArrays);
    resolutionListView.setSelectedIndex(selectedResIndex);

    scrollPane.setWidget(resolutionListView);

    this.row();
      this.add(BotLogic.skin.builder.titleLabel("Ustawienia")).fillX().expandX().pad(PADDING_VERITICAL, PADDING_HORIZONTAL, PADDING_HORIZONTAL, PADDING_VERITICAL).colspan(3);
    this.row().padTop(10);
      this.add(BotLogic.skin.builder.normalLabel("Rozdzielczość:")).width(LABEL_WIDTH).padLeft(PADDING_HORIZONTAL).right().top();
      this.add(scrollPane).width(OPTION_WIDTH).height(RESOLUTION_SCROLL_PANE_HEIGHT).colspan(2);

    this.row().padTop(10);
      this.add(BotLogic.skin.builder.normalLabel("Pełny ekran:")).width(LABEL_WIDTH).padLeft(PADDING_HORIZONTAL).right().top();
      this.add(fullScreenCheckBox).colspan(2).left();

    this.row();
      this.add().expandX();
      this.add(saveButton).width(180).height(52).padTop(PADDING_BUTTONS_BOTTOM).padBottom(20);
      this.add(cancelButton).width(180).height(52).padTop(PADDING_BUTTONS_BOTTOM).padBottom(20).padRight(20).padLeft(20);

    if (Debug.TABLE)
      this.debug();
  }

  private void saveButtonClicked() {
    BotLogic.audio.click.play();

    DisplayMode mode = resModes[resolutionListView.getSelectedIndex()];

    Gdx.graphics.setDisplayMode(mode.getWidth(), mode.getHeight(), fullScreenCheckBox.isChecked());

    BotLogic.config.putResolution(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Gdx.graphics.isFullscreen());
    hide();
  }
}
