package de.macbury.botlogic.core.ui;

import com.badlogic.gdx.scenes.scene2d.ui.*;
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
}
