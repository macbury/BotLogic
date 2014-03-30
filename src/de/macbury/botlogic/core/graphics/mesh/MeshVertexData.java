package de.macbury.botlogic.core.graphics.mesh;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

public class MeshVertexData implements Pool.Poolable {

  public static enum AttributeType {
    Position, Normal, TextureCord, Color
  }

  public Vector3 position;
  public Vector3 normal;
  public Vector2 uv;
  public Color color;

  public MeshVertexData() {
    position = new Vector3();
    normal = new Vector3();
    uv = new Vector2();
    color = new Color(1,1,1,1);
    reset();
  }

  @Override
  public void reset() {
    position.set(0,0,0);
    normal.set(0,0,0);
    uv.set(0,0);
    color.set(Color.WHITE);
  }


  @Override
  public boolean equals(Object obj) {
    MeshVertexData other = (MeshVertexData)obj;
    return other.position.x == position.x && other.position.z == position.z && other.position.y == position.y;
  }
}