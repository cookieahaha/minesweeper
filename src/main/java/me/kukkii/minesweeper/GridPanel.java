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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.synth.SynthLookAndFeel;

public class GridPanel extends JPanel implements ActionListener  {

  private static final int LEN = 24;

  private int height;
  private int width;
  private int nBombs;
  private long startTime;

  private JButton[] buttons;

  private Minesweeper game;
  private Color[] colors;
  private Color initColor;
  private Font boldFont;

  public GridPanel(int height, int width, int nBombs) {
    this.height = height;
    this.width = width;
    this.nBombs = nBombs;

    setPreferredSize(new Dimension(width * LEN, height * LEN));
    setLayout(new GridLayout(height, width));
    initColors();
    try {
      // UIManager.setLookAndFeel(new MetalLookAndFeel());
      UIManager.setLookAndFeel(new SynthLookAndFeel());
    } catch (UnsupportedLookAndFeelException e) { }
    // setOpaque(true);
    // setForeground(Color.GRAY);
    // setBackground(Color.GRAY);

    buttons = new JButton[height * width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        JButton button = new JButton();
        buttons[i * width + j] = button;
        add(button);
      }
    }
    initGame(height, width, nBombs);
    initButtons();
  }

  private void initColors() {
    colors = new Color[10];
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
    colors[9] = Color.BLACK;
    // initColor = Color.GRAY;
    initColor = new Color(192,192,192);

    Font font = getFont();
    boldFont = new Font(font.getName(), Font.BOLD, font.getSize());
  }

  private void initButtons() {
    Matrix matrix = game.getUser();
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        JButton button = buttons[i * width + j];
        // button.setText("" + matrix.get(i.j));
        button.setText("X");
        button.addActionListener(this);
        button.setForeground(Color.BLACK);
        button.setBackground(initColor);
        button.setOpaque(true);
        // button.setBorderPainted(false);
      }
    }
  }

  private void initGame(int height, int width, int nBombs) {
    game = new Minesweeper(height,width,nBombs);
    game.setBombs();
    game.checkAll();
    startTime = System.currentTimeMillis();
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

    int s = game.turn(i, j, k);
    updateButtons(game.getUser());

    if (s == 0) {
      return;
    }
    else if (s < 0) {
      updateButtons(game.getAdmin());
      JOptionPane.showMessageDialog(null, "Game Over!");
    }
    else {
      long time = System.currentTimeMillis() - startTime;
      time /= 1000;
      JOptionPane.showMessageDialog(null, "Congratulations! " + time + " seconds");
    }
    initGame(height, width, nBombs);
    initButtons();
  }

  private void updateButtons(Matrix matrix) {
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
         char c = matrix.get(i, j);
         JButton button = buttons[i * width + j];
         if (c >= '0' && c <= '9') {
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
