package de.macbury.botlogic.core.graphics.compositor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;

/**
 * Created by macbury on 03.04.14.
 */
public class BloomStep extends BufferedCompositorPass {
  private int u_texture;
  private GLTexture diffuseTexture;
  public BloomStep(RenderContext context) {
    super(context, Gdx.files.internal("shaders/simple.vert"), Gdx.files.internal("shaders/glow.frag"));
  }

  @Override
  protected void setupUniforms() {
    u_texture = shader.getUniformLocation("u_texture");
  }

  @Override
  protected void applyUniforms() {
    shader.setUniformi(u_texture, context.textureBinder.bind(diffuseTexture));
  }

  public void setDiffuseTexture(GLTexture diffuseTexture) {
    this.diffuseTexture = diffuseTexture;
  }
}
