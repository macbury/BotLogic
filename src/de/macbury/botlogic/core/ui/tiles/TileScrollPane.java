package de.macbury.botlogic.core.ui.tiles;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.macbury.botlogic.core.BotLogicDebug;
import de.macbury.botlogic.core.ui.scroll.ScrollPaneWithoutMouseScroll;

/**
 * Created by macbury on 05.04.14.
 */
public class TileScrollPane extends ScrollPaneWithoutMouseScroll {

  private Table table;

  public TileScrollPane(ScrollPaneStyle style) {
    super(null, style);

    this.setFillParent(false);
    this.setFadeScrollBars(false);
    this.setFlickScroll(false);

    this.table = new Table();
    table.row().top().left();

    if (BotLogicDebug.TABLE)
      table.debug();
    setWidget(table);
  }

  public void addTile(Table cellTable) {
    table.add(cellTable).pad(20, 15, 50, 10).width(400).height(520);
  }
}
