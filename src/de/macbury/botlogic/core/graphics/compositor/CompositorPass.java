package de.macbury.botlogic.core.graphics.compositor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Created by macbury on 03.04.14.
 */
public abstract class CompositorPass implements Disposable {

  private static final String TAG = "CompositPass";
  protected RenderContext context;
  protected ShaderProgram shader;
  private int u_size;
  private int u_projection;
  private OrthographicCamera screenCamera;
  private Mesh screenQuad;

  public CompositorPass(RenderContext context, FileHandle vertexHandle, FileHandle fragmentHandle) {
    this.context = context;
    screenCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    createScreenQuad();
    shader = new ShaderProgram(vertexHandle, fragmentHandle);
    if (!shader.isCompiled()) {
      Gdx.app.error(TAG, shader.getLog());
      throw new GdxRuntimeException(shader.getLog());
    }

    u_size         = shader.getUniformLocation("u_size");
    u_projection   = shader.getUniformLocation("u_projection");
    setupUniforms();
  }

  protected abstract void setupUniforms();
  protected abstract void applyUniforms();

  private void createScreenQuad() {
    if (screenQuad != null)
      return;
    screenQuad = new Mesh(true, 4, 6, new VertexAttribute(VertexAttributes.Usage.Position, 3,
            "a_position"), new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, "a_color"),
            new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoords"));

    Vector3 vec0 = new Vector3(0, 0, 0);
    screenCamera.unproject(vec0);
    Vector3 vec1 = new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0);
    screenCamera.unproject(vec1);
    screenQuad.setVertices(new float[]{vec0.x, vec0.y, 0, 1, 0, 1,
            vec1.x, vec0.y, 0, 1, 1, 1,
            vec1.x, vec1.y, 0, 1, 1, 0,
            vec0.x, vec1.y, 0, 1, 0, 0});
    screenQuad.setIndices(new short[]{0, 1, 2, 2, 3, 0});
  }

  public void render() {
    shader.begin();
      context.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glViewport(0, 0, getWidth(), getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        shader.setUniformMatrix(u_projection, screenCamera.combined);
        shader.setUniformf(u_size, screenCamera.viewportWidth, screenCamera.viewportHeight);

        applyUniforms();

        screenQuad.render(shader, GL20.GL_TRIANGLES);
      context.end();
    shader.end();
  }

  public void resize(int width, int height) {
    screenCamera.setToOrtho(false, width, height);
    screenCamera.update(true);
    screenQuad = null;
    createScreenQuad();
  }

  public int getWidth() {
    return Gdx.graphics.getWidth();
  }

  public int getHeight() {
    return Gdx.graphics.getHeight();
  }

  @Override
  public void dispose() {
    screenQuad.dispose();
    shader.dispose();
  }
}
