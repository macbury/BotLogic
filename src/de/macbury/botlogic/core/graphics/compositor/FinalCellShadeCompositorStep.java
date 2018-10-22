package de.macbury.botlogic.core.graphics.compositor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;

/**
 * Created by macbury on 03.04.14.
 */
public class FinalCellShadeCompositorStep extends BufferedCompositorPass {
  private int u_color_texture;
  private int u_outline_texture;
  private GLTexture outlineTexture;
  private GLTexture colorTexture;

  public FinalCellShadeCompositorStep(RenderContext context) {
    super(context, Gdx.files.internal("shaders/final.vert"), Gdx.files.internal("shaders/final.frag"));
  }

  @Override
  protected void setupUniforms() {
    u_outline_texture = shader.getUniformLocation("u_outline_texture");
    u_color_texture = shader.getUniformLocation("u_color_texture");
  }

  @Override
  protected void applyUniforms() {
    shader.setUniformi(u_outline_texture, context.textureBinder.bind(outlineTexture));
    shader.setUniformi(u_color_texture, context.textureBinder.bind(colorTexture));
  }

  public void setColorTexture(GLTexture colorTexture) {
    this.colorTexture = colorTexture;
  }

  public void setOutlineTexture(GLTexture outlineTexture) {
    this.outlineTexture = outlineTexture;
  }

  @Override
  public void dispose() {
    super.dispose();
    colorTexture = null;
    outlineTexture = null;
  }
}
