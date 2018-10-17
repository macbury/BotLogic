package de.macbury.botlogic.core.graphics.compositor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import de.macbury.botlogic.core.BotLogic;

/**
 * Created by macbury on 03.04.14.
 */
public class OutlineCompositorPass extends BufferedCompositorPass {
  private int u_texture;
  private Texture texture;

  public OutlineCompositorPass(RenderContext nc) {
    super(nc, Gdx.files.internal("shaders/edge.vert"), Gdx.files.internal("shaders/edge.frag"));
  }

  @Override
  protected void setupUniforms() {
    u_texture = shader.getUniformLocation("u_texture");
  }

  @Override
  protected void applyUniforms() {
    shader.setUniformi(u_texture, context.textureBinder.bind(texture));
  }

  @Override
  public int getWidth() {
    return Math.round(((float) super.getWidth()) / BotLogic.config.getOutlineQuality());
  }

  @Override
  public int getHeight() {
    return Math.round(((float) super.getHeight()) / BotLogic.config.getOutlineQuality());
  }

  public Texture getTexture() {
    return texture;
  }

  public void setTexture(Texture texture) {
    this.texture = texture;
  }
}
