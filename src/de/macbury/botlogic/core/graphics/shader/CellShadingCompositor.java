package de.macbury.botlogic.core.graphics.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.botlogic.core.graphics.compositor.CompositorPass;
import de.macbury.botlogic.core.graphics.compositor.FinalCompositorStep;
import de.macbury.botlogic.core.graphics.compositor.OutlineCompositorPass;

import java.util.ArrayList;

/**
 * Created by macbury on 02.04.14.
 */
public class CellShadingCompositor implements Disposable {
  private final String TAG = "CellShadingCompositor";
  private OutlineCompositorPass outlineStep;
  private FinalCompositorStep finalStep;

  public CellShadingCompositor(RenderContext context) {
    outlineStep = new OutlineCompositorPass(context);
    finalStep   = new FinalCompositorStep(context);
  }

  public void render(FrameBuffer colorBuffer, FrameBuffer depthBuffer) {
    outlineStep.setTexture(depthBuffer.getColorBufferTexture());
    outlineStep.render();

    finalStep.setColorTexture(colorBuffer.getColorBufferTexture());
    finalStep.setOutlineTexture(outlineStep.getBufferedTexture());
    finalStep.render();
  }

  public void resize(int width, int height) {
    outlineStep.resize(width,height);
    finalStep.resize(width, height);
  }

  @Override
  public void dispose() {
    outlineStep.dispose();
    finalStep.dispose();
  }
}
