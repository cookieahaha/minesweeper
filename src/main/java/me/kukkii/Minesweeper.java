public class Minesweeper{

  private int height = 1;
  private int width = 1;
  private int amountOfBomb = 1;
  private Matrix user;
  private Matrix admin;

  public Minesweeper(int height,int width,int amountOfBomb){
    this.height = height;
    this.width = width;
    this.amountOfBomb = amountOfBomb;
    
    user = new Matrix(height,width,'X');
    admin = new Matrix(height,width,'0');
  }

  public void setBomb(){

  }

  public int checl(int i, int j){
    return 0;
  }

  public void checkAll(){

  }

  public int turn(int i, int j){
    return 0;
  }

  public int win(){
    return 0;
  }

  public void open(int i, int j){

  }

}
