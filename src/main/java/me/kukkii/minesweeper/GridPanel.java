// $Id$

package me.kukkii.minesweeper;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GridPanel extends JPanel implements ActionListener  {

  private static final int LEN = 24;

  private int height;
  private int width;
  private JButton[] buttons;

  private Minesweeper game;

  public GridPanel(int height, int width, int nBombs) {
    this.height = height;
    this.width = width;
    buttons = new JButton[height * width];
    setLayout(new GridLayout(height, width));
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        JButton button = new JButton("X");
        button.addActionListener(this);
        buttons[i * width + j] = button;
        add(button);
      }
    }
    setPreferredSize(new Dimension(width * LEN, height * LEN));
    initGame(height, width, nBombs);
  }

  private void initGame(int height, int width, int nBombs) {
    game = new Minesweeper(height,width,nBombs);
    game.setBombs();
    game.checkAll();
  }

  public void actionPerformed(ActionEvent ae) {
    JButton source = (JButton)ae.getSource();
    int n = 0;
    for (JButton button : buttons) {
      if (button == source) {
        break;
      }
      n += 1;
    }
    int i = n / width;
    int j = n % width;
    int modifiers = ae.getModifiers();
    /*
     *  1 control
     *  2 shift
     *  4 command
     *  8 option
     * 16 (always?)
     */
    modifiers &= 15;
    int k = (modifiers==0)?0:1;
    System.err.println("i=" + i + " j=" + j + " k=" + k);
    //
    int s = game.turn(i, j, k);
    //
    Matrix matrix = game.getUser();
    for (i = 0; i < height; i++) {
      for (j = 0; j < height; j++) {
         char c = matrix.get(i, j);
         buttons[i * width + j].setText("" + c);
         if (c == ' ' || (c >= '1' && c <= '8')) {
           buttons[i * width + j].removeActionListener(this);
         }
      }
    }
  }
  
  public static void main(String[] args){
    int height = 16;
    int width = 30;
    int nBombs = 99;
    if (args.length == 1) {
      int level = Integer.parseInt(args[0]);
      if (level == 1) {
        height = 9;
        width  = 9;
        nBombs = 10;
      }
      else if (level == 2) {
        height = 16;
        width  = 16;
        nBombs = 40;
      }
    }
    else if (args.length == 2) {
      height = Integer.parseInt(args[0]);
      width  = height;
      nBombs = Integer.parseInt(args[1]);
    }
    else if (args.length >= 3) {
      height = Integer.parseInt(args[0]);
      width  = Integer.parseInt(args[1]);
      nBombs = Integer.parseInt(args[2]);
    }
    JFrame frame = new JFrame("minesweeper");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new GridPanel(height, width, nBombs));
    frame.pack();
    frame.setVisible(true);
  }
}
