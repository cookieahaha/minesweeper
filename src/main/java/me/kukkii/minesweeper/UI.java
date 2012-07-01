package me.kukkii.minesweeper;

import java.util.Scanner;

public class UI{
  
  public static void main(String[] args){
    Minesweeper game = new Minesweeper(10,20,20);
    game.setBombs();
    game.checkAll();
    System.out.println();
    game.getUser().print();
    
    Scanner scan = new Scanner(System.in);

    while(true){

      System.out.println("Enter i=");
      int i = scan.nextInt();
      if (i >= game.getHeight()){
        System.out.println("the number you entered is too big. re-enter the number less than game.getHeight()");
        continue;
      }

      System.out.println("enter j=");
      int j = scan.nextInt();      
      if (j >= game.getWidth()){
        System.out.println("the number you entered is too big. re-enter the number less than game.getWidth()");
        continue;
      }

      System.out.println("enter F=");
      int k = scan.nextInt();        
      if (k!=0 && k!=1){
        System.out.println("enter 0(open) or 1(frag)");
        continue;
      }
      
      int u = game.turn(i,j,k);
      if (u!=0){
        break;
      }

    }
   
  }

}
