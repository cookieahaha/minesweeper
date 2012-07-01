// $Id$

package me.kukkii.minesweeper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.synth.SynthLookAndFeel;

public class GridPanel extends JPanel implements ActionListener  {

  private static final int LEN = 24;

  private int height;
  private int width;
  private JButton[] buttons;

  private Minesweeper game;
  private Color[] colors;
  private Font boldFont;

  public GridPanel(int height, int width, int nBombs) {
    this.height = height;
    this.width = width;
    buttons = new JButton[height * width];
    setLayout(new GridLayout(height, width));
    try {
      // UIManager.setLookAndFeel(new MetalLookAndFeel());
      UIManager.setLookAndFeel(new SynthLookAndFeel());
    } catch (UnsupportedLookAndFeelException e) { }
    // setOpaque(true);
    // setForeground(Color.GRAY);
    // setBackground(Color.GRAY);
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        JButton button = new JButton("X");
        button.addActionListener(this);
        button.setBackground(Color.GRAY);
        button.setOpaque(true);
        // button.setBorderPainted(false);
        buttons[i * width + j] = button;
        add(button);
      }
    }
    setPreferredSize(new Dimension(width * LEN, height * LEN));
    initGame(height, width, nBombs);
    colors = new Color[9];
    colors[0] = Color.WHITE;
    colors[1] = Color.BLUE;
    // colors[2] = Color.GREEN;
    colors[2] = new Color(0, 128, 0);
    colors[3] = Color.RED;
    colors[4] = Color.BLACK;
    colors[5] = Color.BLACK;
    colors[6] = Color.BLACK;
    colors[7] = Color.BLACK;
    colors[8] = Color.BLACK;
    Font font = getFont();
    boldFont = new Font(font.getName(), Font.BOLD, font.getSize());
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
      for (j = 0; j < width; j++) {
         char c = matrix.get(i, j);
         JButton button = buttons[i * width + j];
         if (c >= '0' && c <= '8') {
           button.setForeground(colors[c - '0']);
           button.setFont(boldFont);
         }
         char x = (c == '0') ? ' ' : c;
         if (c == 'F') {
           x = 'P';
         }
         button.setText("" + x);
         if (c >= '0' && c <= '8') {
           button.setBackground(Color.WHITE);
           // button.setBackground(Color.YELLOW);
           // button.setOpaque(true);
           // button.setBorderPainted(true);
           button.removeActionListener(this);
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
