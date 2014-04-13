package de.macbury.botlogic.core.graphics.compositor;

import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 02.04.14.
 */
public class LevelCompositor implements Disposable {
  private final String TAG = "CellShadingCompositor";
  private BloomStep bloomStep;
  private DisplayStep displayStep;
  private OutlineCompositorPass outlineStep;
  private FinalCellShadeCompositorStep finalCellShadeStep;

  public LevelCompositor(RenderContext context) {
    outlineStep        = new OutlineCompositorPass(context);
    finalCellShadeStep = new FinalCellShadeCompositorStep(context);
    displayStep        = new DisplayStep(context);
    //bloomStep          = new BloomStep(context);
  }

  public void render(FrameBuffer colorBuffer, FrameBuffer depthBuffer, FrameBuffer decalBuffer) {
    outlineStep.setTexture(depthBuffer.getColorBufferTexture());
    outlineStep.render();

    finalCellShadeStep.setColorTexture(colorBuffer.getColorBufferTexture());
    finalCellShadeStep.setOutlineTexture(outlineStep.getBufferedTexture());
    finalCellShadeStep.render();

    //bloomStep.setDiffuseTexture(finalCellShadeStep.getBufferedTexture());
    //bloomStep.render();

    displayStep.setDiffuseTexture(finalCellShadeStep.getBufferedTexture());
    //displayStep.setEffectsTexture(decalBuffer.getColorBufferTexture());
    displayStep.render();
  }

  public void resize(int width, int height) {
    outlineStep.resize(width,height);
    finalCellShadeStep.resize(width, height);
    displayStep.resize(width, height);
    //bloomStep.resize(width,height);
  }

  @Override
  public void dispose() {
    outlineStep.dispose();
    finalCellShadeStep.dispose();
    displayStep.dispose();
    //bloomStep.dispose();
  }
}
