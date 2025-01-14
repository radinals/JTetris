package com.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.random.RandomGenerator;

import javax.swing.SwingUtilities;

import com.model.Block;
import com.model.Board;
import com.model.Board.BoardEventListener;
import com.model.Shape;
import com.view.GameWindow;

public class Game {
  
  private GameWindow gameWindow;
  private Board gameBoard;
  private volatile boolean running;
  private int blockSize = (int)(16 * 1);
  private volatile long frameRate = 300;
  private final int baseLineClearScore = 100;
  private int totalScore;
  
  public Game() {
    gameBoard = new Board(12, 22, new BoardEventListener() {
      
      @Override
      public void onBlockMovementDownCollision() {
        spawnNewBlock();
        int lines = gameBoard.clearCompleteLines();
        if (lines > 0) addScore(lines);
      }

      @Override
      public void onNewBlockColliionAtStart() {
        stopGame();
        gameWindow.getSidebar().displayMessage("Game Over");
      }

    });

    this.running = false;

    // FIXME: Not Thread Safe
    gameWindow = new GameWindow(this);

    gameWindow.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        stopGame();
      }
    });
  }
  
  public int calculateScore(int linesCleared) {
    return linesCleared * baseLineClearScore;
  }
  
  public synchronized void increaseMovementSpeed() {
    if (frameRate < 100) {
      frameRate = 100;
    } else {
      frameRate-=10L;
    }
  }

  public synchronized void decreaseMovementSpeed() {
    frameRate+=100L;
  }
  
  public void spawnNewBlock() {
    gameBoard.placeMovingBlock();
    gameBoard.setMovingBlock(generateRandomBlock());
  }
  
  public int getBlockSize() {
    return blockSize;
  }
  
  public Board getGameBoard() {
    return gameBoard;
  }
  
  private void updateGame() {
    moveCurrentBlockDown();
  }

  public Block getMovingBlock() {
    return gameBoard.getMovingBlock();
  }
  public void moveCurrentBlockRight() {
    if(getMovingBlock() == null || !gameIsRunning()) return;
    try {
      gameBoard.moveBlockRight();
      repaint();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void moveCurrentBlockLeft() {
    if(getMovingBlock() == null || !gameIsRunning()) return;
    gameBoard.moveBlockLeft();
    repaint();
  }
  
  public void moveCurrentBlockDown() {
    if(getMovingBlock() == null || !gameIsRunning()) return;
    try {
      gameBoard.moveBlockDown();
      repaint();
    } catch (Exception e) {
      spawnNewBlock();
    }
  }

  public void repaint() {
   gameWindow.repaint();
  }

  public void rotateCurrentBlock() {
    if(getMovingBlock() == null || !gameIsRunning()) return;
    gameBoard.rotateBlock();
    repaint();
  }
  
  private void resetScore() {
    totalScore = 0;
    gameWindow.getSidebar().setScore(totalScore);
  }
  
  private void addScore(int linesCompleted) {
    totalScore += calculateScore(linesCompleted);
    gameWindow.getSidebar().setScore(totalScore);
  }

  private void initGame() { 
    gameWindow.getSidebar().clearMessageDisplay();
    resetScore();
    gameBoard.setMovingBlock(generateRandomBlock());;
  }
  
  public synchronized void stopGame() {
    this.running = false;
  }
  
  public synchronized boolean gameIsRunning() {
    return this.running;
  }
  
  public void resetGame() {
    stopGame();
    gameBoard.resetBoard();
    startGame();
  }
  
  public void startGame() {
    running = true;
    gameWindow.setVisible(true);
    initGame();
    runGameLoop();
  }

  public void quitGame() {
    stopGame();
    System.exit(0);
  }
  
  public void runGameLoop() {
    Thread gameThread = new Thread(() -> {
      while (running) {
        updateGame();

        SwingUtilities.invokeLater(this::repaint);

        try {
          Thread.sleep(frameRate);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
    });
    gameThread.start();
  }
  
  private int randomRange(int min, int max) {
    RandomGenerator randomGenerator = RandomGenerator.of("Random");
    return randomGenerator.nextInt(min, max); 
  }
  
  private int randomRotation() {
    return randomRange(0, 3);
  }
  
  private Shape randomShape() {
    return Shape.values()[randomRange(0, Shape.values().length)];
  }
  
  private Block generateRandomBlock() {
    // TODO: add position
    Block block =  new Block(randomRotation(), randomShape());
    block.setY(0 - block.getBodyHeight()/2);
    block.setX(gameBoard.getColumns() / 2 - block.getBodyWidth() / 2);
    return block;
  }
}
