package com.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JPanel;

import com.controller.Game;
import com.model.Block;
import com.model.Board.BoardEventListener;
import com.model.Shape;

public class GameView extends JPanel {
  
  private Game game;
  
  public GameView(Game game) {
    this.game = game;
  }
  
  
  private void drawShapeBit(Graphics2D g, int bit, int x, int y) {
    if (bit == 0) {
      g.setColor(Color.black);
      g.drawRect(x, y, game.getBlockSize(), game.getBlockSize());

      return;
    }

    final BlockColors.BlockColor bc = BlockColors.getInstance().getColor(bit);

    if (bc == null) {
      System.err.println("Invalid Bit Value, " + bit);
      System.exit(-1);
    }

    g.setColor(bc.getBorderColor());
    g.fillRect(x, y, game.getBlockSize(), game.getBlockSize());

    final float borderSizeFactor = 0.9f;
    final int boxSize = (int)(game.getBlockSize() * borderSizeFactor);
    final int startX = (x + game.getBlockSize() / 2) - boxSize/2;
    final int startY = (y + game.getBlockSize() / 2) - boxSize/2;

    g.setColor(bc.getBaseColor());
    g.fillRect(startX,startY,boxSize,boxSize);

    final int highlightBoxSize = game.getBlockSize()/2;
    final int highlightStartX = x + game.getBlockSize()/4;
    final int highlightStartY = y + game.getBlockSize()/4;

    g.setColor(bc.getHighlightColor());
    g.fillRect(highlightStartX,highlightStartY,highlightBoxSize,highlightBoxSize);

    g.setColor(Color.black);
    g.drawRect(x, y, game.getBlockSize(), game.getBlockSize());


  }

  private void drawBitmap(Graphics2D g, int[][] bitmap, int sx, int sy) {
    int x = sx;
    int y = sy;
    for(int[] row : bitmap) {
      for(int bit : row) {
        drawShapeBit(g, bit, x, y);
        x += game.getBlockSize();
      }
      x = sx;
      y += game.getBlockSize();
    }
    
  }
  
  @Override
  public void paint(Graphics g) {
    super.paint(g);
    
    Graphics2D g2 = (Graphics2D) g;

    drawBitmap(g2, game.getGameBoard().getBoard(), 0, 0);

    Block mvBlock = game.getMovingBlock();
    
    if (mvBlock != null) {
      int mvBlockX = mvBlock.getX() * game.getBlockSize();
      int mvBlockY = mvBlock.getY() * game.getBlockSize();
      drawBitmap(g2, mvBlock.getBitmap(), mvBlockX,mvBlockY);
    }

  }

}
