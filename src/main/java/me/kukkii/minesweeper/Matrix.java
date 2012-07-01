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

  //

  public static void main(String[] args) throws Exception {
    Matrix matrix = new Matrix(20,20, '.');
    for (int i = 0; i < 20; i++) {
      matrix.set(i, i, '\\');
      matrix.set(i, 19-i, '/');
    }
    matrix.print();
  }
}
