package de.macbury.botlogic.core.graphics.compositor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

/**
 * Created by macbury on 03.04.14.
 */
public abstract class BufferedCompositorPass extends CompositorPass {
  protected FrameBuffer fbo;

  public BufferedCompositorPass(RenderContext context, FileHandle vertexHandle, FileHandle fragmentHandle) {
    super(context, vertexHandle, fragmentHandle);
  }

  @Override
  public void render() {
    fbo.begin();
      Gdx.gl.glClearColor(0, 0, 0, 0);
      Gdx.gl.glViewport(0, 0, fbo.getWidth(), fbo.getHeight());
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
      super.render();
    fbo.end();
  }

  @Override
  public void dispose() {
    super.dispose();
    fbo.dispose();
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);
    this.fbo = new FrameBuffer(Pixmap.Format.RGBA8888, getWidth(), getHeight(), true);
  }

  public FrameBuffer getFbo() {
    return fbo;
  }

  public GLTexture getBufferedTexture() {
    return fbo.getColorBufferTexture();
  }
}
