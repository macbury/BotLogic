package de.macbury.botlogic.core.screens.level.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.macbury.botlogic.core.graphics.mesh.MeshAssembler;
import de.macbury.botlogic.core.graphics.mesh.MeshVertexData;

/**
 * Created by macbury on 30.03.14.
 */
public class Map extends Renderable implements Disposable {
  private TextureAtlas.AtlasRegion floorEndRegion;
  private TextureAtlas tileset;
  private Block[][] tiles;
  private int width;
  private int height;
  private MeshAssembler builder;
  private Vector3 robotStartPosition;

  public Map(FileHandle handle) {
    this.tileset = new TextureAtlas(Gdx.files.internal("tileset.atlas"));
    this.floorEndRegion = this.tileset.findRegion("floor_end");
    TextureAttribute textureAttribute = TextureAttribute.createDiffuse(this.tileset.getTextures().first());
    this.material = new Material(textureAttribute);
    this.primitiveType = GL20.GL_TRIANGLES;
    this.robotStartPosition = new Vector3(0, 1.6f, 0);
    parse(handle);
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

          if (block != null) {
            TextureRegion region = block.getTopTextureRegion(tileset);
            builder.topFace(x * Block.BLOCK_SIZE, block.getY() + Block.BLOCK_SIZE, z * Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE,region.getU(),region.getV(),region.getU2(),region.getV2());

            region = block.getSideTextureRegion(tileset);

            if (block.getY() > 0) {

              if (topBlock != null && topBlock.getY() != block.getY()) {
                builder.frontFace(x * Block.BLOCK_SIZE, block.getY(), z * Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, region.getU(),region.getV(),region.getU2(),region.getV2());
              }

              if (bottomBlock != null && bottomBlock.getY() != block.getY()) {
                builder.backFace(x * Block.BLOCK_SIZE, block.getY(), z * Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, region.getU(), region.getV(), region.getU2(), region.getV2());
              }

              if (leftBlock != null && leftBlock.getY() != block.getY()) {
                builder.leftFace(x * Block.BLOCK_SIZE, block.getY(), z * Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, region.getU(),region.getV(),region.getU2(),region.getV2());
              }

              if (rightBlock != null && rightBlock.getY() != block.getY()) {
                builder.rightFace(x * Block.BLOCK_SIZE, block.getY(), z * Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, region.getU(),region.getV(),region.getU2(),region.getV2());
              }
            }

            if (topBlock == null) {
              builder.frontFace(x * Block.BLOCK_SIZE, 0, z * Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, floorEndRegion.getU(),floorEndRegion.getV(),floorEndRegion.getU2(),floorEndRegion.getV2());
            }

            if (bottomBlock == null) {
              builder.backFace(x * Block.BLOCK_SIZE, 0, z  * Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE,floorEndRegion.getU(),floorEndRegion.getV(),floorEndRegion.getU2(),floorEndRegion.getV2());
            }

            if (leftBlock == null) {
              builder.leftFace(x * Block.BLOCK_SIZE, 0, z * Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, floorEndRegion.getU(),floorEndRegion.getV(),floorEndRegion.getU2(),floorEndRegion.getV2());
            }

            if (rightBlock == null) {
              builder.rightFace(x * Block.BLOCK_SIZE, 0, z * Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE, floorEndRegion.getU(),floorEndRegion.getV(),floorEndRegion.getU2(),floorEndRegion.getV2());
            }
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

  private void parse(FileHandle content) {
    Pixmap mapPixmap = new Pixmap(content);

    this.width  = mapPixmap.getWidth();
    this.height = mapPixmap.getHeight();

    this.tiles = new Block[width][height];

    Color color = new Color();

    for(int row = 0; row < height; row++) {
      for(int col = 0; col < width; col++) {
        Color.rgba8888ToColor(color,  mapPixmap.getPixel(col, row));
        if (Color.RED.equals(color)) {
          robotStartPosition = new Vector3(col+0.5f, 1.05f, row+0.5f);
          BlockFloor floor = new BlockFloor();
          floor.setX(col);
          floor.setZ(row);
          floor.setY(0);
          tiles[col][row] = floor;
        } else if (BlockWall.is(color)) {
          BlockWall wall = new BlockWall();
          wall.setX(col);
          wall.setZ(row);
          wall.setY(1);
          tiles[col][row] = wall;
        } else if (BlockFloor.is(color)) {
          BlockFloor floor = new BlockFloor();
          floor.setX(col);
          floor.setZ(row);
          floor.setY(0);
          tiles[col][row] = floor;
        } else if (color.a != 0.0f) {
          throw new GdxRuntimeException("Undefined char for map: " + color.toString());
        }
      }
    }

    mapPixmap.dispose();
  }


  @Override
  public void dispose() {
    builder.dispose();
    tileset.dispose();
    mesh.dispose();
  }

  public float getCenterX() {
    return width / 2;
  }

  public float getCenterZ() {
    return height / 2;
  }

  public Vector3 getRobotStartPosition() {
    return robotStartPosition;
  }
}
