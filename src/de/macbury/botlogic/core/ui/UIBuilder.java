package de.macbury.botlogic.core.ui;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import de.macbury.botlogic.core.ui.button.StateImageButton;
import de.macbury.botlogic.core.ui.dialog.EndGameDialog;
import de.macbury.botlogic.core.ui.dialog.SettingsDialog;
import de.macbury.botlogic.core.ui.labels.TimerLabel;
import de.macbury.botlogic.core.ui.scroll.ScrollPaneWithoutMouseScroll;
import de.macbury.botlogic.core.ui.tiles.TileScrollPane;

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
    ImageButton button = new ImageButton(skin.getDrawable(iconName));
    button.setStyle(skin.redImageButton);
    return button;
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
    ImageButton button = new ImageButton(skin.getDrawable(iconName));
    button.setStyle(skin.blueImageButton);
    return button;
  }

  public StateImageButton twoStateButton(String first, String second) {
    StateImageButton button = new StateImageButton(skin.blueImageButton, skin.getDrawable(first), skin.getDrawable(second));
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
}
