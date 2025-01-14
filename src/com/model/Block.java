package com.model;

import com.util.MatrixRotations;

public class Block {
  
  private Shape shape;
  private int[][] bitmap;
  private int x, y;
  private int rotation;
  
  public Block(final Block other) {
    this(
        other.getX(),
        other.getY(),
        other.getRotations(),
        other.getShape()
    );
  }
  
  public Block(int rotation, Shape shape) {
    this(0, 0, rotation, shape);
  }

  public Block(int x, int y, Shape shape) {
    this(x, y, 0, shape);
  }

  public Block(int x, int y, int rotation, Shape shape) {
    this.x = x;
    this.y = y;
    this.shape = shape;
    this.bitmap = getBitmap(shape);
    
    for(int i = 0; i < rotation; i++)
      rotate();

  }
  
  public int getRotations() {
    return rotation;
  }
  
  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }
  
  public void rotate() {
    bitmap = MatrixRotations.rotate90DegreesClockwise(bitmap);
    ++rotation;
  }
  
  public Shape getShape() {
    return shape;
  }
  
  public int[][] getBitmap() {
    return bitmap;
  }
  
  public Integer getCell(int x, int y) {
    try {
      return bitmap[y][x];
    } catch (Exception e) {
      return null;
    }
  }
  
  public int getBodyWidth() {
   return getBitmap()[0].length;
  }

  public int getBodyHeight() {
   return getBitmap().length;
  }

  private static int[][] getBitmap(Shape shape) {
    int v = shape.getValue();
    switch(shape) {
      case I: {
        int[][] bitmap = { 
            {v},
            {v},
            {v},
            {v},
        };
        return bitmap;
      }
      case L: {
        int[][] bitmap = { 
            {v,0,0},
            {v,v,v},
        };
        return bitmap;
      }
      case J: { 
        int[][] bitmap = { 
            {0,0,v},
            {v,v,v},
        };
        return bitmap;
      }
      case O: {
        int[][] bitmap = { 
            {v,v},
            {v,v},
        };
        return bitmap;
      }
      case S: {
        int[][] bitmap = { 
            {0,v,v},
            {v,v,0},
        };
        return bitmap;
      }
      case Z: { 
        int[][] bitmap = { 
            {v,v,0},
            {0,v,v},
        };
        return bitmap;

      }
      case T: {
        int[][] bitmap = { 
            {v,v,v},
            {0,v,0},
        };
        return bitmap;
      }
      default:
        return null;
    }
  }
  
  @Override
  public String toString() {
    String string = "";
    string += "SHAPE=" + shape;
    for(int[] l : getBitmap()) {
      for(int b : l) {
        System.out.print(b + " ");
      }
      System.out.println();
    }
    
    return string;
    
  } 
  
}
