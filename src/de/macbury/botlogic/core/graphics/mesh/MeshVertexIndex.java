package de.macbury.botlogic.core.graphics.mesh;

import com.badlogic.gdx.utils.Pool;

/**
 * Created by macbury on 15.03.14.
 */
public class MeshVertexIndex implements Pool.Poolable {
  public short n1;
  public short n2;
  public short n3;

  public MeshVertexIndex() {
    reset();
  }

  @Override
  public void reset() {
    n1 = 0;
    n2 = 0;
    n3 = 0;
  }

  public void set(int i1, int i2, int i3) {
    n1 = (short)i1;
    n2 = (short)i2;
    n3 = (short)i3;
  }
}
