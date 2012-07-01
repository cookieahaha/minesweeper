package me.kukkii.minesweeper;

public class Matrix{

  private int[] line;
  private int height;
  private int width;
  private char chara;

  public Matrix(int height, int width, char chara){
    line = new int[height*width];
    this.height = height;
    this.width = width;
    this.chara = chara;

    for(int i=0; i<line.length; i++){
   
    }  
  }

  public void print(){
 
  }

  public int get(int i,int j){
    return 0;
  }

  public void set(int i, int j,int a){
    
  }

}
