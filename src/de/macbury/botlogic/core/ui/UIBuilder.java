package de.macbury.botlogic.core.ui;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import de.macbury.botlogic.core.ui.button.StateImageButton;
import de.macbury.botlogic.core.ui.dialog.EndGameDialog;
import de.macbury.botlogic.core.ui.dialog.ErrorDialog;
import de.macbury.botlogic.core.ui.dialog.SettingsDialog;
import de.macbury.botlogic.core.ui.labels.TimerLabel;
import de.macbury.botlogic.core.ui.scroll.ScrollPaneWithoutMouseScroll;
import de.macbury.botlogic.core.ui.tiles.TileScrollPane;
import de.macbury.botlogic.core.ui.tooltip.TooltipWidget;

/**
 * Created by macbury on 04.04.14.
 */
public class UIBuilder {
  private FlatSkin skin;

  public UIBuilder(FlatSkin skin) {
    this.skin = skin;
  }

  public CheckBox checkbox(String label){
    return new CheckBox(label, skin.checkBoxStyle);
  }

  public SettingsDialog settingsDialog() {
    return new SettingsDialog(skin.dialogStyle);
  }

  public EndGameDialog endGameDialog() {
    return new EndGameDialog(skin.dialogStyle);
  }

  public Dialog dialog(String title) {
    Dialog dialog = new Dialog(title, skin.dialogStyle);
    return dialog;
  }

  public Label titleLabel(String text) {
    Label label = new Label(text, skin.robotoBigLabelStyle);
    return label;
  }

  public Label normalLabel(String text) {
    Label label = new Label(text, skin.robotoThinLabelStyle);
    return label;
  }

  public TextButton redTextButton(String name) {
    return new TextButton(name, skin.redTextButton);
  }

  public ImageButton redImageButton(String iconName) {
    ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle(skin.redImageButton);
    style.imageUp = skin.getDrawable(iconName);
    return new ImageButton(style);
  }

  public ScrollPane getScrollPaneWithoutMouseScroll() {
    ScrollPaneWithoutMouseScroll pane = new ScrollPaneWithoutMouseScroll(null, skin.tileScrollPane);
    return pane;
  }

  public TileScrollPane getTileScrollPane() {
    TileScrollPane pane = new TileScrollPane(skin.tileScrollPane);
    return pane;
  }

  public Image image(String name) {
    return new Image(skin.getDrawable(name));
  }

  public ImageButton blueImageButton(String iconName) {
    ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle(skin.blueImageButton);
    style.imageUp = skin.getDrawable(iconName);
    return new ImageButton(style);
  }

  public StateImageButton twoStateButton(String first, String second) {
    ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle(skin.blueImageButton);
    StateImageButton button = new StateImageButton(style, skin.getDrawable(first), skin.getDrawable(second));
    return button;
  }

  public Slider slider(float min, float max, float step, boolean vertical) {
    return new Slider(min, max, step, vertical, skin.sliderStyle);
  }

  public SelectBox<String> stringSelectBox() {
    return new SelectBox<String>(skin.selectBoxStyle);
  }

  public TimerLabel timerLabel() {
    return new TimerLabel(skin.robotoThinLabelStyle);
  }

  public ErrorDialog errorDialog(String content) {
    return new ErrorDialog(skin.dialogStyle, content);
  }

  public TooltipWidget tooltip(String message) {
    return new TooltipWidget(skin.tooltipStyle, message);
  }
}
