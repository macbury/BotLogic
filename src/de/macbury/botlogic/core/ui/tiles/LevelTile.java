package de.macbury.botlogic.core.ui.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.BotLogicDebug;
import de.macbury.botlogic.core.screens.level.file.LevelFile;
import de.macbury.botlogic.core.ui.FlatSkin;

/**
 * Created by macbury on 05.04.14.
 */
public class LevelTile extends Table {
  private Image levelImage;
  private TextButton playButton;
  private Label descriptionLabel;
  private LevelFile levelFile;
  private FlatSkin skin;
  private Label titleLabel;
  private Table descriptionTable;
  private ScrollPane descriptionScrollPane;
  private LevelTileListener listener;

  public LevelTile(LevelFile levelFile, FlatSkin skin) {
    super();
    this.skin      = skin;
    this.levelFile = levelFile;
    this.descriptionTable = new Table();
    this.descriptionScrollPane = skin.builder.getScrollPaneWithoutMouseScroll();
    descriptionScrollPane.setFlickScroll(false);

    if (BotLogicDebug.TABLE)
      descriptionTable.debug();

    this.setBackground(skin.getDrawable("background_light"));

    if (BotLogicDebug.TABLE)
      this.debug();

    this.row().top().left();
    this.levelImage = new Image(new Texture(Gdx.files.internal("maps/preview.png")));
    this.titleLabel = BotLogic.skin.builder.titleLabel(levelFile.getName());

    this.descriptionLabel = skin.builder.normalLabel(levelFile.getDescription());
    descriptionLabel.setWrap(true);
    descriptionTable.row().top().center();
    descriptionTable.add(levelImage).colspan(2).pad(0, 0, 15, 0);
    descriptionTable.row();
    descriptionTable.add(titleLabel).expandX().fillX().colspan(2).pad(30, 25, 25, 25);
    descriptionTable.row().top().left();
    descriptionTable.add(descriptionLabel).colspan(2).expandX().fillX().pad(0, 30, 25, 30);

    for(LevelFile.LevelFeature feature : levelFile.getFeatures()) {
      descriptionTable.row().top().left();
      Image iconImage = new Image(skin.getDrawable(LevelFile.getIconByFeature(feature)));
      descriptionTable.add(iconImage).left().padLeft(45);
      descriptionTable.add(skin.builder.normalLabel(LevelFile.getNameByFeature(feature))).padLeft(15).padRight(30).left().fillY().expandX();
    }

    descriptionTable.row();
    descriptionTable.add().colspan(2).expand();

    descriptionScrollPane.setWidget(descriptionTable);

    this.row().top().left();
    this.add(descriptionScrollPane).expand().fill().colspan(2);
    this.row();
    this.add().expandX();

    this.playButton = BotLogic.skin.builder.redTextButton("Zagraj");
    this.add(playButton).height(44).width(116).pad(20, 15, 20, 15);

    playButton.addListener(new ClickListener(){
      @Override
      public void clicked(InputEvent event, float x, float y) {
        if (listener != null)
          listener.onPlayClick(LevelTile.this.levelFile, LevelTile.this);
      }
    });
  }


  public void setListener(LevelTileListener listener) {
    this.listener = listener;
  }
}
