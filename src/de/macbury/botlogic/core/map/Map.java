package de.macbury.botlogic.core.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pool;
import de.macbury.botlogic.core.graphics.mesh.MeshAssembler;
import de.macbury.botlogic.core.graphics.mesh.MeshVertexData;

/**
 * Created by macbury on 30.03.14.
 */
public class Map extends Renderable implements Disposable {
  private TextureAtlas tileset;
  private Block[][] tiles;
  private int width;
  private int height;
  private MeshAssembler builder;

  public Map(FileHandle handle) {
    this.tileset = new TextureAtlas(Gdx.files.internal("tileset.atlas"));
    TextureAttribute textureAttribute = TextureAttribute.createDiffuse(this.tileset.getTextures().first());
    this.material = new Material(textureAttribute);
    this.primitiveType = GL20.GL_TRIANGLES;
    parse(handle.readString());
    build();
  }

  private void build() {
    this.builder = new MeshAssembler();

    builder.begin();
      builder.using(MeshVertexData.AttributeType.Position);
      builder.using(MeshVertexData.AttributeType.Normal);
      builder.using(MeshVertexData.AttributeType.TextureCord);
      for(int x = 0; x < width; x++) {
        for(int z = 0; z < height; z++) {
          Block block       = get(x,z);
          Block topBlock    = get(x, z + 1);
          Block bottomBlock = get(x, z - 1);
          Block leftBlock   = get(x - 1, z);
          Block rightBlock  = get(x + 1, z);

          TextureRegion region = block.getTopTextureRegion(tileset);
          builder.topFace(x * Block.BLOCK_SIZE, block.getY() + Block.BLOCK_SIZE, z * Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE,region.getU(),region.getV(),region.getU2(),region.getV2());

          region = block.getSideTextureRegion(tileset);
          if (topBlock == null || topBlock.getY() < block.getY()) {
            builder.frontFace(x * Block.BLOCK_SIZE, block.getY(), z * Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, region.getU(),region.getV(),region.getU2(),region.getV2());
          }

          if (bottomBlock == null || bottomBlock.getY() < block.getY()) {
            builder.backFace(x * Block.BLOCK_SIZE, block.getY(), z  * Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE,region.getU(),region.getV(),region.getU2(),region.getV2());
          }

          if (leftBlock == null || leftBlock.getY() < block.getY()) {
            builder.leftFace(x * Block.BLOCK_SIZE, block.getY(), z * Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, region.getU(),region.getV(),region.getU2(),region.getV2());
          }

          if (rightBlock == null || rightBlock.getY() < block.getY()) {
            builder.rightFace(x * Block.BLOCK_SIZE, block.getY(), z * Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, region.getU(),region.getV(),region.getU2(),region.getV2());
          }
        }
      }
    builder.end();

    mesh = new Mesh(true, builder.getVerties().length, builder.getIndices().length, builder.getVertexAttributes());
    mesh.setVertices(builder.getVerties());
    mesh.setIndices(builder.getIndices());
    mesh.setAutoBind(true);
    meshPartSize   = mesh.getNumIndices();
    meshPartOffset = 0;
  }

  public Block get(int x, int z) {
    if (x >= 0 && x < width && z >= 0 && z < height) {
      return tiles[x][z];
    } else {
      return null;
    }
  }

  private void parse(String content) {
    String[] lines = content.split("\n");
    this.width  = new Integer(lines[0]);
    this.height = new Integer(lines[1]);

    this.tiles = new Block[width][height];

    for(int row = 0; row < height; row++) {
      String line = lines[row+2];
      for(int col = 0; col < width; col++) {
        char blockType = line.charAt(col);

        if (blockType == BlockWall.MAP_CHAR) {
          BlockWall wall = new BlockWall();
          wall.setX(col);
          wall.setZ(row);
          wall.setY(1);
          tiles[col][row] = wall;
        } else if (blockType == BlockFloor.MAP_CHAR) {
          BlockFloor floor = new BlockFloor();
          floor.setX(col);
          floor.setZ(row);
          floor.setY(0);
          tiles[col][row] = floor;
        } else {
          throw new GdxRuntimeException("Undefined char for map: " + blockType);
        }
      }
    }
  }


  @Override
  public void dispose() {
    builder.dispose();
    tileset.dispose();
    mesh.dispose();
  }
}
