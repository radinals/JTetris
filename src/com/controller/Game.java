package com.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
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
  private volatile long frameRate = 800L;
  private final int baseLineClearScore = 100;
  private int totalScore;
  private final int bonusScores = 2;
  private volatile boolean explicitRepaintCalled = false;
//  private int scoreLevelFactor = 1000;
  private final long frameRateSpeedModifier = 100L;
  private final long frameRateSpeedMin = 100L;
  
  private HashMap<String,Integer> shapeGenerationOdds;
  private int totalBlocksGenerated = 0;
  
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
    
    this.shapeGenerationOdds = new HashMap<String, Integer>();
    
    for(Shape shape : Shape.values()) {
      this.shapeGenerationOdds.put(shape.toString(), 0);
    }

    this.running = false;
    this.totalScore = 0;

    this.gameWindow = new GameWindow(this);

    this.gameWindow.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        stopGame();
      }
    });
  }
  
  public int calculateScore(int linesCleared) {
    return (baseLineClearScore * linesCleared) * (int)((linesCleared > 1) ? bonusScores : 1);
  }
  
  public synchronized void increaseMovementSpeed() {
    frameRate -= frameRateSpeedModifier;
    repaint();
    explicitRepaintCalled = true;

    if (frameRate < frameRateSpeedMin) 
      frameRate = frameRateSpeedMin;
  }

  public synchronized void decreaseMovementSpeed() {
    frameRate+=frameRateSpeedModifier;
    repaint();
    explicitRepaintCalled = true;
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
  
//  private void increaseGameSpeed() {
//    if (totalScore > 0 && totalScore % scoreLevelFactor == 0)
//      increaseGameSpeed();
//  }
  
  private void updateGame() {
//    increaseGameSpeed();
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

  public synchronized void repaint() {
   gameWindow.repaint();
   explicitRepaintCalled = true;
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

        if (!explicitRepaintCalled) {
          SwingUtilities.invokeLater(this::repaint);
        } else {
          explicitRepaintCalled = false;
        }

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
  
  private int getBoardCenterX() {
    return gameBoard.getColumns() / 2;
  }
  
  private int getBlockCenterX(final Block block) {
    return block.getBodyWidth() / 2;
  }

  private int getBlockCenterY(final Block block) {
    return block.getBodyHeight() / 2;
  }
  
  private Block generateRandomBlock() {
    // TODO: add position
    Block block =  new Block(randomRotation(), randomShape());

    block.setY(0 - getBlockCenterY(block)); // start offscreen;
    block.setX(getBoardCenterX() - getBlockCenterX(block));
    
    String shapeName = block.getShape().toString();
    
    int count = this.shapeGenerationOdds.get(shapeName);
    this.shapeGenerationOdds.put(shapeName, count+1);
    
    ++totalBlocksGenerated;

    for(Map.Entry<String, Integer> e : this.shapeGenerationOdds.entrySet()) {
     System.out.println(e.getKey() + ":" + e.getValue() + " ODDS:" + ((float)e.getValue() / totalBlocksGenerated)); 
    }
    System.out.println("TOTAL GENERATED: " + totalBlocksGenerated);
    
    return block;
  }
}
