package de.macbury.botlogic.core.ui.stage;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.ui.tooltip.TooltipWidget;

/**
 * Created by macbury on 10.04.14.
 */
public class FlatStage extends Stage {
  private TooltipWidget tooltip;

  public FlatStage(ScreenViewport screenViewport) {
    super(screenViewport);
    tooltip = BotLogic.skin.builder.tooltip("...");

    addActor(tooltip);
    hideTooltip();
  }

  public void showTooltip(String content) {
    tooltip.setContent(content);
    tooltip.setVisible(true);
    tooltip.toFront();
  }

  public boolean isTooltipVisible() {
    return tooltip.isVisible();
  }

  public void hideTooltip() {
    tooltip.setVisible(false);
  }
}
