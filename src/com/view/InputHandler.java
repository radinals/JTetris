package com.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.controller.Game;

public class InputHandler implements KeyListener {
  
  Game game;
  
  private boolean movementKeyPressed;
  
  public InputHandler(Game game) {
    this.game = game;
    this.movementKeyPressed = false;
  }

  @Override
  public void keyPressed(KeyEvent arg0) {
    if (movementKeyPressed)
      return;
    switch (arg0.getKeyCode()) { 
    case KeyEvent.VK_LEFT:
      game.moveCurrentBlockLeft();
      this.movementKeyPressed = true;
      break;

    case KeyEvent.VK_RIGHT:
      game.moveCurrentBlockRight();
      this.movementKeyPressed = true;
      break;
      
    case KeyEvent.VK_DOWN:
      game.moveCurrentBlockDown();
      this.movementKeyPressed = true;
      break;
      
    case KeyEvent.VK_ENTER:
      game.rotateCurrentBlock();
      this.movementKeyPressed = true;
      break;
      
    default:
      return;
    }
    game.repaint();

  }

  @Override
  public void keyReleased(KeyEvent arg0) {

    if(movementKeyPressed)
      this.movementKeyPressed = false;

    switch (arg0.getKeyCode()) { 
    case KeyEvent.VK_MINUS:
      game.decreaseMovementSpeed();
      break;

    case KeyEvent.VK_EQUALS:
      game.increaseMovementSpeed();
      break;

    case KeyEvent.VK_BACK_SPACE:
      game.resetGame();
      break;

    case KeyEvent.VK_ESCAPE:
      game.quitGame();
      break;

      default:
        return;
    }
    game.repaint();

  }

  @Override
  public void keyTyped(KeyEvent arg0) {
    // TODO Auto-generated method stub

  }

}
