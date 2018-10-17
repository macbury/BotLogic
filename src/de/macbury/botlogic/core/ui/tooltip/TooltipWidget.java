package de.macbury.botlogic.core.ui.tooltip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;


/**
 * Created by macbury on 10.04.14.
 */
public class TooltipWidget extends Widget {

  private TooltipStyle style;
  private String content;
  private Vector2 temp = new Vector2();

  public TooltipWidget(TooltipStyle tooltipStyle, String message) {
    this.style = tooltipStyle;
    setContent(message);
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
    GlyphLayout glyphLayout = new GlyphLayout();
    glyphLayout.setText(style.font, content);
    this.setWidth(glyphLayout.width);
    this.setHeight(style.font.getLineHeight());
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    if (isVisible()) {
      this.setPosition(Gdx.input.getX() + 16, getStage().getHeight() - Gdx.input.getY() - 16);
    }
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    if (isVisible()) {
      style.background.draw(batch, getX(), getY(), getWidth(), getHeight());
      style.font.setColor(style.fontColor);
      style.font.draw(batch, content, getX(), getY() + getHeight() - getHeight()/4);
    }
  }

  public static class TooltipStyle {
    public BitmapFont font;
    public Color fontColor;
    public Drawable background;
  }
}
