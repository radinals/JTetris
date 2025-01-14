package com.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.TextArea;

import javax.swing.JFrame;

import com.controller.Game;


/*
 * TODO: Keyboard Input
 */

public class GameWindow extends JFrame {
  private GameView gameView;
  private Game game;
  private GameSidebar sidebar;
  
  private Rectangle sidebarArea;
  private Rectangle boardArea;
  
  public GameWindow(Game game) {
    this.game = game;

    setTitle("Tetris");

    int vspace = 5;
    int hspace = 5;
    int areaSpacing = 10;

    int boardHeight = game.getBlockSize() * (game.getGameBoard().getRows() + 2);
    int boardWidth = game.getBlockSize() * (game.getGameBoard().getColumns() + 1);
    this.boardArea = new Rectangle(hspace,vspace,boardWidth,boardHeight);
    
    this.sidebarArea = new Rectangle(hspace + boardWidth + areaSpacing, vspace, boardWidth,boardHeight);

    Dimension winSize = new Dimension();
    winSize.width = hspace + boardArea.width + areaSpacing + sidebarArea.width + hspace;
    winSize.height = vspace + boardArea.height + vspace;

    setSize(winSize);
    setPreferredSize(winSize);
    setMinimumSize(winSize);

    setLayout(null);
    setLocationRelativeTo(null);
    setFocusable(true);
    requestFocus();

    addKeyListener(new InputHandler(game));

    addGameView();
    addSidebar();

    revalidate();
    pack();
  }
  
  public GameSidebar getSidebar() {
    return sidebar;
  }
  
  private void addSidebar() {
    this.sidebar = new GameSidebar();

    this.sidebar.setSize(sidebarArea.getSize());
    this.sidebar.setPreferredSize(sidebarArea.getSize());
    this.sidebar.setMinimumSize(sidebar.getSize());
    this.sidebar.setBounds(sidebarArea);
    
    this.sidebar.setVisible(true);
    this.sidebar.setFocusable(false);
    
    add(this.sidebar);
  }
  
  private void addGameView() {
    this.gameView = new GameView(this.game);

    this.gameView.setSize(boardArea.getSize());
    this.gameView.setPreferredSize(boardArea.getSize());
    this.gameView.setMinimumSize(boardArea.getSize());
    this.gameView.setBounds(boardArea);

    this.gameView.setVisible(true);
    this.gameView.setFocusable(false);

    add(this.gameView);
  }
  
  @Override
  public void repaint() {
    super.repaint();
    if (!game.gameIsRunning())
      return;
    gameView.repaint();
  }
}
