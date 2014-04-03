package de.macbury.botlogic.core.graphics.skybox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;


/**
 * Created by macbury on 03.04.14.
 */
public class SkyBox implements Disposable {
  private int s_cubemap;
  private int u_mvpMatrix;
  private ShaderProgram shader;
  private Pixmap texture;
  private Cubemap cubeMap;
  private Mesh mesh;
  private Matrix4 invView;
  private Matrix4 mvp;

  public SkyBox(String name) {
    this.mesh = genSkyBoxMesh();
    this.texture = new Pixmap(Gdx.files.internal(name));
    this.cubeMap = new Cubemap(texture, texture, texture, texture, texture, texture);
    this.shader  = new ShaderProgram(Gdx.files.internal("shaders/skybox.vert"), Gdx.files.internal("shaders/skybox.frag"));

    if (!this.shader.isCompiled()) {
      throw new GdxRuntimeException(shader.getLog());
    }

    u_mvpMatrix = shader.getUniformLocation("u_mvpMatrix");
    s_cubemap   = shader.getUniformLocation("s_cubemap");

    invView = new Matrix4();
    mvp     = new Matrix4();
  }

  public static Mesh genSkyBoxMesh() {
    Mesh mesh = new Mesh(true, 8, 14, new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));

    float[] cubeVerts = { -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, };

    short[] indices = { 0, 1, 2, 3, 7, 1, 5, 4, 7, 6, 2, 4, 0, 1 };

    mesh.setVertices(cubeVerts);
    mesh.setIndices(indices);

    return mesh;
  }

  public void render(RenderContext context, PerspectiveCamera camera) {
    invView.set(camera.view);

    invView.val[Matrix4.M03] = 0;
    invView.val[Matrix4.M13] = 0;
    invView.val[Matrix4.M23] = 0;

    invView.inv().tra();

    mvp.set(camera.projection);
    mvp.mul(invView);

    context.begin();
      context.setCullFace(GL20.GL_FRONT);
      context.setDepthMask(false);
      shader.begin();
        shader.setUniformMatrix(u_mvpMatrix, mvp);
        shader.setUniformi(s_cubemap, context.textureBinder.bind(cubeMap));

        mesh.render(shader, GL20.GL_TRIANGLE_STRIP);
      shader.end();
    context.end();
  }

  @Override
  public void dispose() {
    mesh.dispose();
    cubeMap.dispose();
    texture.dispose();
    shader.dispose();
  }
}
