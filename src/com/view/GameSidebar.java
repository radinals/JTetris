package com.view;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;

import javax.swing.JPanel;

public class GameSidebar extends JPanel {
  
  private Label scoreTitleLabel, scoreValueLabel, messageDisplayLabel;
  
  private Font bigFont;
  private Font mediumFont;
//  private Font smallFont;
  
  public GameSidebar() {
    this.setLayout(new GridLayout(16,1));
    this.setAlignmentX(CENTER_ALIGNMENT);
    
    this.bigFont = new Font("arial", Font.BOLD, 30);
    this.mediumFont = new Font("arial", Font.PLAIN, 25);
    
    this.scoreTitleLabel = new Label();
    this.scoreValueLabel = new Label();
    this.messageDisplayLabel = new Label();

    this.scoreTitleLabel.setFont(bigFont);
    this.scoreValueLabel.setFont(mediumFont);
    this.messageDisplayLabel.setFont(mediumFont);
    
    this.scoreTitleLabel.setText("SCORE");
    this.scoreValueLabel.setText("0");
    this.messageDisplayLabel.setText("");
    
    this.scoreTitleLabel.setVisible(true);
    this.scoreValueLabel.setVisible(true);
    this.messageDisplayLabel.setVisible(true);

    this.scoreTitleLabel.setAlignment(Label.CENTER);
    this.scoreValueLabel.setAlignment(Label.CENTER);
    this.messageDisplayLabel.setAlignment(Label.CENTER);
    
    add(scoreTitleLabel);
    add(scoreValueLabel);
    add(messageDisplayLabel);
    
    revalidate();

  }
  
  public void setScore(int score) {
    this.scoreValueLabel.setText(String.valueOf(score));
  }
  
  public void displayMessage(String message) {
    this.messageDisplayLabel.setText(message);
  }
  
  public void clearMessageDisplay() {
    this.messageDisplayLabel.setText("");
  }

}
