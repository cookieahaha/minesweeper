// $Id$

package me.kukkii.minesweeper;

public class Matrix{

  private char[] cells;
  private int height;
  private int width;

  public Matrix(int height, int width, char initChar){
    cells = new char[height*width];
    this.height = height;
    this.width = width;

    for(int i=0; i<cells.length; i++){
      cells[i] = initChar;
    }  
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public char get(int i,int j){
    return cells[i * width + j];
  }

  public void set(int i, int j, char c){
    cells[i * width + j] = c;
  }

  public void print(){
    System.out.print("  |");
    for (int j = 0; j < width; j++) {
      String s = "" + j;
      System.out.print(s.substring(s.length()-1));
    }
    System.out.println();
    System.out.print("--+");
    for (int j = 0; j < width; j++) {
      System.out.print("-");
    }
    System.out.println();
    for (int i = 0; i < height; i++) {
      System.out.printf("%2d|", i);
      for (int j = 0; j < width; j++) {
        System.out.print(get(i,j));
      }
      System.out.println();
    }
    System.out.flush();
  }

  public Matrix copy() {
    Matrix copy = new Matrix(height, width, ' ');
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        copy.set(i, j, get(i,j));
      }
    }
    return copy;
  }

  //

  public static void main(String[] args) throws Exception {
    int height = 20;
    int width = 20;
    if (args.length > 0) {
      height = Integer.parseInt(args[0]);
      if (args.length > 1) {
        width = Integer.parseInt(args[1]);
      }
      else {
        width = height;
      }
    }

    Matrix matrix = new Matrix(height,width, '.');
    int n = height;
    if (width < n) {
      n = width;
    }
    for (int i = 0; i < n; i++) {
      matrix.set(i, i, '\\');
      char c = '/';
      if (matrix.get(i,n-1-i) == '\\') {
        c = 'X';
      }
      matrix.set(i, n-1-i, c);
    }
    matrix.print();
  }
}
