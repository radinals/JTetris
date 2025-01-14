package com.model;

import java.util.ArrayDeque;

public class Board {

  public interface BoardEventListener {
    public void onBlockMovementDownCollision();
    public void onNewBlockColliionAtStart();
  }

  private int[][] board;
  private BoardEventListener boardEventListener;
  private Block movingBlock;

  private int rows, columns;

  public Board(int columns, int rows) {
    this(columns, rows, null);
  }

  public Board(int columns, int rows, BoardEventListener boardEventListener) {
    initBoard(rows, columns);
    this.movingBlock = null;
    this.boardEventListener = boardEventListener;
  }
  
  public void resetBoard() {
    initBoard(rows, columns);
    this.movingBlock = null;
  }

  public int clearCompleteLines() {
    ArrayDeque<Integer> completedLines = new ArrayDeque<Integer>();

    int linesCompleted = 0;

    for (int y = 0; y < rows; y++) {

      boolean found = true;

      for (int bit : board[y]) {
        if (bit == 0) {
          found = false;
          break;
        }
      }

      if (found) {
        linesCompleted++;
        completedLines.add(y);
      }

    }

    while (!completedLines.isEmpty()) {
      clearLine(completedLines.poll());
    }

    return linesCompleted;

  }

  public int[][] getBoard() {
    return board;
  }

  public Integer getCell(int x, int y) {
    try {
      return board[y][x];
    } catch (Exception e) {
      return null;
    }
  }

  public int getColumns() {
    return columns;
  }

  public Block getMovingBlock() {
    return movingBlock;
  }

  public int getRows() {
    return rows;
  }
  
  public int getTotalBlockLineCount() {
    
    int lineCount = 0;
    
    for(int y = rows-1; y >= 0; y--) {

      boolean lineHasBlock = false;

      for (int bit : board[y]) {
        // break if part of a block is found
        if (bit != 0) {
          lineHasBlock = true;
          break;
        }
      }
      
      if (!lineHasBlock) {
        lineCount = (rows - y - 1);
      } else if(lineHasBlock && y == 0) {
        lineCount = rows;
      }

    }
    
    return lineCount;
    
  }

  public void moveBlockDown() {
    final int y = movingBlock.getY();

    movingBlock.setY(y + 1);

    if (movingBlockHasCollisionOnMovementDown()) {
      movingBlock.setY(y);
      
      if (boardEventListener != null)
        boardEventListener.onBlockMovementDownCollision();
    }

  }

  public void moveBlockLeft() {
    final int x = movingBlock.getX();

    movingBlock.setX(x - 1);

    if (movingBlockHasCollisionOnMovementLeft()) {
      movingBlock.setX(x);
    }

  }

  public void moveBlockRight() {
    final int x = movingBlock.getX();

    movingBlock.setX(x + 1);

    if (movingBlockHasCollisionOnMovementRight()) {
      movingBlock.setX(x);
    }

  }

  public void placeMovingBlock() {
    assert(movingBlock != null);
    drawMovingBlockOnBoard();
    movingBlock = null;
  }

  public void rotateBlock() {
    Block tmp = new Block(movingBlock);
    movingBlock.rotate();
    if (movingBlockHasCollisionOnRotating()) {
      movingBlock = tmp;
    }
  }

  public void setMovingBlock(final Block block) {
    assert(movingBlock == null);
    movingBlock = new Block(block);
    if (boardEventListener != null && movingBlockIntersectingStaticBlocks()) {
      movingBlock = null;
      boardEventListener.onNewBlockColliionAtStart();
    }

  }

  @Override
  public String toString() {
    String string = "";
    for (int[] l : board) {
      for (int b : l) {
        string += b + " ";
      }
      string += "\n";
    }
    return string;
  }

  // move everything down starting from the line
  private void clearLine(int line) {
    for (int y = line; y >= 0; y--) {
      for (int x = 0; x < columns; x++) {
        board[y][x] = (y == 0) ? 0 : board[y - 1][x];
      }
    }
  }

  private void initBoard(int rows, int columns) {
    this.board = new int[rows][columns];
    this.columns = columns;
    this.rows = rows;
    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < columns; x++)
        this.board[y][x] = 0;
    }
  }

  private boolean movingBlockHasCollisionOnRotating() {

    // check collision with bottom wall
    if (movingBlock.getY() + movingBlock.getBodyHeight() - 1 >= rows)
      return true;

    // check collision with right wall
    if (movingBlock.getX() + movingBlock.getBodyWidth() - 1 >= columns)
      return true;

    // check collision with left wall
    if (movingBlock.getX() < 0)
      return true;

    return movingBlockIntersectingStaticBlocks();
  }

  private boolean movingBlockHasCollisionOnMovementDown() {

    // check collision with bottom wall
    if (movingBlock.getY() + movingBlock.getBodyHeight() - 1 >= rows)
      return true;

    return movingBlockIntersectingStaticBlocks();
  }

  private boolean movingBlockHasCollisionOnMovementLeft() {

    // check collision with left wall
    if (movingBlock.getX() < 0)
      return true;

    return movingBlockIntersectingStaticBlocks();

  }

  private boolean movingBlockHasCollisionOnMovementRight() {

    // check collision with right wall
    if (movingBlock.getX() + movingBlock.getBodyWidth() - 1 >= columns)
      return true;

    return movingBlockIntersectingStaticBlocks();
  }

  private boolean movingBlockIntersectingStaticBlocks() {
    final int movingBlockX = movingBlock.getX();
    final int movingBlockY = movingBlock.getY();
    final int movingBlockMaxX = movingBlockX + movingBlock.getBodyWidth();
    final int movingBlockMaxY = movingBlockY + movingBlock.getBodyHeight();


    // iterate on the moving block bitmap position on the board
    for (int y = movingBlockY; y < movingBlockMaxY; y++) {
      for (int x = movingBlockX; x < movingBlockMaxX; x++) {

        // translate the moving block onto the board, and get the cell from the
        // block bitmap
        Integer movingBlockCell = movingBlock.getCell(x - movingBlockX,
            y - movingBlockY);

        Integer boardCell = getCell(x, y);

        // check if the cell at the bitmap is already filled
        if (movingBlockCell != null && boardCell != null && boardCell != 0 && movingBlockCell != 0) {
            if (boardEventListener != null && movingBlockY <= 0) {
              boardEventListener.onNewBlockColliionAtStart();
            }

          return true;
        }
      }
    }

    return false;

  }

  private void drawMovingBlockOnBoard() {
    
    if (movingBlock== null) return;

    final int movingBlockX = movingBlock.getX();
    final int movingBlockY = movingBlock.getY();
    final int movingBlockMaxX = movingBlockX + movingBlock.getBodyWidth();
    final int movingBlockMaxY = movingBlockY + movingBlock.getBodyHeight();

    // iterate on the moving block bitmap position on the board
    for (int y = movingBlockY; y < movingBlockMaxY; y++) {
      for (int x = movingBlockX; x < movingBlockMaxX; x++) {
        
        if (y < 0) return;

        // translate the moving block onto the board, and get the cell from the
        // block bitmap
        Integer movingBlockCell = movingBlock.getCell(x - movingBlockX,
            y - movingBlockY);

        // draw the block bitmap on the board
        if (movingBlockCell != null && movingBlockCell != 0)
          board[y][x] = movingBlockCell;

      }
    }
  }
}