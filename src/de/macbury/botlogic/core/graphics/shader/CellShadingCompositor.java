package de.macbury.botlogic.core.graphics.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 02.04.14.
 */
public class CellShadingCompositor implements Disposable {
  private int u_texture;
  private int u_projection;
  private final String TAG = "CellShadingCompositor";
  private final int u_size;
  private OrthographicCamera screenCamera;
  private Mesh screenQuad;
  private ShaderProgram detectEdgesShader;

  public CellShadingCompositor() {
    screenCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    createScreenQuad();
    detectEdgesShader = new ShaderProgram(Gdx.files.internal("shaders/edge.vert"), Gdx.files.internal("shaders/edge.frag"));
    if (!detectEdgesShader.isCompiled()) {
      Gdx.app.log(TAG, detectEdgesShader.getLog());
    }

    u_texture = detectEdgesShader.getUniformLocation("u_texture");
    u_size         = detectEdgesShader.getUniformLocation("u_size");
    u_projection   = detectEdgesShader.getUniformLocation("u_projection");
  }


  private void createScreenQuad() {
    if (screenQuad != null)
      return;
    screenQuad = new Mesh(true, 4, 6, new VertexAttribute(VertexAttributes.Usage.Position, 3,
            "a_position"), new VertexAttribute(VertexAttributes.Usage.Color, 4, "a_color"),
            new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoords"));

    Vector3 vec0 = new Vector3(0, 0, 0);
    screenCamera.unproject(vec0);
    Vector3 vec1 = new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0);
    screenCamera.unproject(vec1);
    screenQuad.setVertices(new float[]{vec0.x, vec0.y, 0, 1, 1, 1, 1, 0, 1,
            vec1.x, vec0.y, 0, 1, 1, 1, 1, 1, 1,
            vec1.x, vec1.y, 0, 1, 1, 1, 1, 1, 0,
            vec0.x, vec1.y, 0, 1, 1, 1, 1, 0, 0});
    screenQuad.setIndices(new short[]{0, 1, 2, 2, 3, 0});
  }


  public void render(FrameBuffer colorBuffer, FrameBuffer depthBuffer, RenderContext renderContext) {
    detectEdgesShader.begin();
      renderContext.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        detectEdgesShader.setUniformi(u_texture, renderContext.textureBinder.bind(depthBuffer.getColorBufferTexture()));
        detectEdgesShader.setUniformMatrix(u_projection, screenCamera.combined);
        detectEdgesShader.setUniformf(u_size, depthBuffer.getWidth(), depthBuffer.getHeight());
        screenQuad.render(detectEdgesShader, GL20.GL_TRIANGLES);
      renderContext.end();
    detectEdgesShader.end();
  }

  public void resize(int width, int height) {
    screenCamera.setToOrtho(false, width, height);
    screenCamera.update(true);
    screenQuad = null;
    createScreenQuad();
  }

  @Override
  public void dispose() {
    screenQuad.dispose();
    detectEdgesShader.dispose();
  }
}
