package de.macbury.botlogic.core.ui.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.BotLogicDebug;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.ArrayList;
import java.util.Collections;

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
  private Slider musicVolumeSlider;
  private SelectBox<String> outlineQualitySelectBox;
  private ArrayList<TempDisplayMode> resModes;
  private CheckBox fullScreenCheckBox;
  private TextButton cancelButton;
  private SelectBox<String> resolutionSelectBox;
  private TextButton saveButton;

  public SettingsDialog(WindowStyle windowStyle) {
    super("", windowStyle);

    this.musicVolumeSlider = BotLogic.skin.builder.slider(0,1,0.1f, false);
    musicVolumeSlider.setValue(BotLogic.config.getMusicVolume());
    this.outlineQualitySelectBox = BotLogic.skin.builder.stringSelectBox();
    Array<String> outlineQualityArray = new Array<String>();
    outlineQualityArray.add("Najlepsza");
    outlineQualityArray.add("Słaba");
    outlineQualityArray.add("Żałosna");

    outlineQualitySelectBox.setItems(outlineQualityArray);
    outlineQualitySelectBox.setSelectedIndex(BotLogic.config.getOutlineQuality()-1);

    this.saveButton   = BotLogic.skin.builder.redTextButton("Zapisz zmiany");
    this.cancelButton = BotLogic.skin.builder.redTextButton("Anuluj");
    this.fullScreenCheckBox = BotLogic.skin.builder.checkbox("");
    this.fullScreenCheckBox.setChecked(Gdx.graphics.isFullscreen());

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

    this.resolutionSelectBox = BotLogic.skin.builder.stringSelectBox();

    Array<String> resArrays = new Array<String>();


    this.resModes = new ArrayList<TempDisplayMode>();

    TempDisplayMode currentMode = new TempDisplayMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    try {
      for(DisplayMode mode : Display.getAvailableDisplayModes()) {
        TempDisplayMode newMode = new TempDisplayMode(mode);

        if(resModes.indexOf(newMode) == -1 && mode.isFullscreenCapable()) {
          resModes.add(newMode);
        }
      }
    } catch (LWJGLException e) {
      e.printStackTrace();
    }


    Collections.sort(resModes);

    for(TempDisplayMode mode : resModes) {
      resArrays.add(mode.toString());
    }

    resolutionSelectBox.setItems(resArrays);
    int selectedResIndex = Math.max(0, resModes.indexOf(currentMode));
    resolutionSelectBox.setSelectedIndex(selectedResIndex);

    this.row();
      this.add(BotLogic.skin.builder.titleLabel("Ustawienia")).fillX().expandX().pad(PADDING_VERITICAL, PADDING_HORIZONTAL, PADDING_HORIZONTAL, PADDING_VERITICAL).colspan(3);
    this.row().padTop(10);
      this.add(BotLogic.skin.builder.normalLabel("Rozdzielczość:")).width(LABEL_WIDTH).padLeft(PADDING_HORIZONTAL).right().top();
      this.add(resolutionSelectBox).colspan(2).fillX().padRight(PADDING_HORIZONTAL);

    this.row().padTop(10);
      this.add(BotLogic.skin.builder.normalLabel("Jakość obramowania")).width(LABEL_WIDTH).padLeft(PADDING_HORIZONTAL).right().top();
      this.add(outlineQualitySelectBox).colspan(2).left().fill().padRight(PADDING_HORIZONTAL);

    this.row().padTop(10);
      this.add(BotLogic.skin.builder.normalLabel("Głośność muzyki")).width(LABEL_WIDTH).padLeft(PADDING_HORIZONTAL).right().top();
      this.add(musicVolumeSlider).colspan(2).left().fill().padRight(PADDING_HORIZONTAL);

    this.row().padTop(10);
      this.add(BotLogic.skin.builder.normalLabel("Pełny ekran:")).width(LABEL_WIDTH).padLeft(PADDING_HORIZONTAL).right().top();
      this.add(fullScreenCheckBox).colspan(2).left();

    this.row();
      this.add().expandX();
      this.add(saveButton).width(180).height(52).padTop(PADDING_BUTTONS_BOTTOM).padBottom(20);
      this.add(cancelButton).width(180).height(52).padTop(PADDING_BUTTONS_BOTTOM).padBottom(20).padRight(20).padLeft(20);

    if (BotLogicDebug.TABLE)
      this.debug();
  }

  private void saveButtonClicked() {
    BotLogic.audio.click.play();

    TempDisplayMode mode = resModes.get(resolutionSelectBox.getSelectedIndex());
    BotLogic.config.setMusicVolume(musicVolumeSlider.getValue());
    BotLogic.config.putResolution(mode.width, mode.height, fullScreenCheckBox.isChecked());
    BotLogic.config.setOutlineQuality(outlineQualitySelectBox.getSelectedIndex() + 1);
    BotLogic.config.load();
    hide();
  }

  class TempDisplayMode implements Comparable<TempDisplayMode> {
    public final int width;
    public final int height;

    public TempDisplayMode(int width, int height) {
      this.width = width;
      this.height = height;
    }

    public TempDisplayMode(DisplayMode mode) {
      this.width = mode.getWidth();
      this.height = mode.getHeight();
    }

    @Override
    public boolean equals(Object obj) {
      TempDisplayMode mode = (TempDisplayMode)obj;
      return mode.width == this.width && mode.height == this.height;
    }

    public int sum() {
      return width + height;
    }

    @Override
    public String toString() {
      return width+"x"+height;
    }

    @Override
    public int compareTo(TempDisplayMode o) {
      if (o.sum() > this.sum()){
        return 1;
      } else if (o.sum() < this.sum()) {
        return -1;
      } else {
        return 0;
      }
    }
  }
}
