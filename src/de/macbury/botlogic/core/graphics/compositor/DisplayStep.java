package de.macbury.botlogic.core.graphics.compositor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;

/**
 * Created by macbury on 03.04.14.
 */
public class DisplayStep extends CompositorPass {
  private int u_texture;
  private GLTexture diffuseTexture;
  private Texture effectsTexture;
  private int u_effects_texture;

  public DisplayStep(RenderContext context) {
    super(context, Gdx.files.internal("shaders/simple.vert"), Gdx.files.internal("shaders/simple.frag"));
  }

  @Override
  protected void setupUniforms() {
    u_texture = shader.getUniformLocation("u_texture");
    //u_effects_texture = shader.getUniformLocation("u_effects_texture");
  }

  @Override
  protected void applyUniforms() {
    shader.setUniformi(u_texture, context.textureBinder.bind(diffuseTexture));
    //shader.setUniformi(u_effects_texture, context.textureBinder.bind(effectsTexture));
  }

  public void setDiffuseTexture(GLTexture diffuseTexture) {
    this.diffuseTexture = diffuseTexture;
  }

  public void setEffectsTexture(Texture effectsTexture) {
    this.effectsTexture = effectsTexture;
  }
}
