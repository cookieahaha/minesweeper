// $Id$

package me.kukkii.minesweeper;

public class Analyzer {
  private Matrix matrix;

  public Analyzer(Matrix matrix) {
    this.matrix = matrix;
  }

  public int check() {
    int count = 0;
    while (true) {
      int n = checkAroundAllOnce();
      System.err.println("checked -> " + n);
      if (n == 0) {
        break;
      }
      count += n;
    }
    return count;
  }

  private int checkAroundAllOnce() {
    int count = 0;
    int h = matrix.getHeight();
    int w = matrix.getWidth();
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        count += checkAround(i,j);
      }
    }
    return count;
  }

  private int checkAround(int i, int j) {
    char c = matrix.get(i,j);
    if (c < '0') {
      return 0;
    }
    if (c > '8') {
      return 0;
    }
    int nBombs = c - '0';
    int nUnknown = 0;
    int h = matrix.getHeight();
    int w = matrix.getWidth();
    for (int ii = -1; ii <= 1; ii++) {
      for (int jj = -1; jj <= 1; jj++) {
        if (ii == 0 && jj == 0) {
          continue;
        }
        if (i+ii < 0 || i+ii >= h) {
          continue;
        }
        if (j+jj < 0 || j+jj >= w) {
          continue;
        }
        c = matrix.get(i+ii, j+jj);
        if (c == 'X' || c == 'F') {
          nUnknown += 1;
        }
        if (c == '9') {
          nBombs -= 1;
        }
      }
    }
    if (nUnknown == 0) {
      return 0;
    }
    System.err.printf("(%d,%d) nBombs=%d nUnknown=%d%n", i, j, nBombs, nUnknown);
    if (nBombs == nUnknown) {
      return setBombsAround(i,j);
    }
    if (nBombs == 0) {
      return setNoBombsAround(i,j);
    }
    return 0;
  }

  private int setBombsAround(int i, int j) {
    int count = 0;
    int h = matrix.getHeight();
    int w = matrix.getWidth();
    for (int ii = -1; ii <= 1; ii++) {
      for (int jj = -1; jj <= 1; jj++) {
        if (ii == 0 && jj == 0) {
          continue;
        }
        if (i+ii < 0 || i+ii >= h) {
          continue;
        }
        if (j+jj < 0 || j+jj >= w) {
          continue;
        }
        char c = matrix.get(i+ii, j+jj);
        if (c == 'X' || c == 'F') {
          matrix.set(i+ii, j+jj, '9');
          count += 1;
          System.err.printf("(%d,%d) -> 9%n", i+ii, j+jj);
        }
      }
    }
    return count;
  }

  private int setNoBombsAround(int i, int j) {
    int count = 0;
    int h = matrix.getHeight();
    int w = matrix.getWidth();
    for (int ii = -1; ii <= 1; ii++) {
      for (int jj = -1; jj <= 1; jj++) {
        if (ii == 0 && jj == 0) {
          continue;
        }
        if (i+ii < 0 || i+ii >= h) {
          continue;
        }
        if (j+jj < 0 || j+jj >= w) {
          continue;
        }
        char c = matrix.get(i+ii, j+jj);
        if (c == 'X' || c == 'F') {
          matrix.set(i+ii, j+jj, 'A');
          count += 1;
          System.err.printf("(%d,%d) -> A%n", i+ii, j+jj);
        }
      }
    }
    return count;
  }
}
