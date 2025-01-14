package com.util;

public class MatrixRotations {

  public static int[][] rotate90DegreesClockwise(int[][] matrix) {
    int[][] rotated = new int[matrix[0].length][matrix.length];
    
    for(int y = 0; y < matrix.length; y++) {
      for(int x = 0; x < matrix[0].length; x++) {
        rotated[x][matrix.length - 1 - y] = matrix[y][x];
      }
    }
    
    return rotated;
  }

  public static int[][] rotate90DegreesCounterClockwise(int[][] matrix) {
    int[][] rotated = new int[matrix[0].length][matrix.length];
    
    for(int y = 0; y < matrix.length; y++) {
      for(int x = 0; x < matrix[0].length; x++) {
        rotated[matrix[0].length - 1 - x][y] = matrix[y][x];
      }
    }
    
    return rotated;
  }

  public static int[][] rotate180DegreesClockwise(int[][] matrix) {
    int[][] rotated = new int[matrix.length][matrix[0].length];
    
    for(int y = 0; y < matrix.length; y++) {
      for(int x = 0; x < matrix[0].length; x++) {
        rotated[matrix.length - 1 - y][matrix[0].length - 1 - x] = matrix[y][x];
      }
    }
    
    return rotated;
  }

  public static int[][] flipHorizontal(int[][] matrix) {
    int[][] rotated = new int[matrix.length][matrix[0].length];
    
    for(int y = 0; y < matrix.length; y++) {
      for(int x = 0; x < matrix[0].length; x++) {
        rotated[y][matrix[0].length - 1 - x] = matrix[y][x];
      }
    }
    
    return rotated;
  }

  public static int[][] flipVertical(int[][] matrix) {
    int[][] rotated = new int[matrix.length][matrix[0].length];
    
    for(int y = 0; y < matrix.length; y++) {
      for(int x = 0; x < matrix[0].length; x++) {
        rotated[matrix.length - 1 - y][x] = matrix[y][x];
      }
    }
    
    return rotated;
  }

}
