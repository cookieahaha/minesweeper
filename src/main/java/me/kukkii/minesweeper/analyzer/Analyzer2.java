// $Id$

package me.kukkii.minesweeper;

import java.util.HashSet;
import java.util.Set;

import me.kukkii.minesweeper.Matrix;

public class Analyzer2 {
  private Matrix matrix;
  private int height;
  private int width;

  public Analyzer2(Matrix matrix) {
    this.matrix = matrix;
    height = matrix.getHeight();
    width = matrix.getWidth();
  }

  private int encode(int i, int j) {
    return i * width + j;
  }

  private int[] decode(int n) {
    int i = n / width;
    int j = n % width;
    return new int[] { i, j };
  }

  private char get(int n) {
    int[] a = decode(n);
    return matrix.get(a[0], a[1]);
  }

  private void set(int n, char c) {
    int[] a = decode(n);
    matrix.set(a[0], a[1], c);
  }

  private Set<Integer> getNeibors(int n) {
    Set<Integer> neibors = new HashSet<Integer>();
    int[] a = decode(n);
    for (int i = -1; i <= 1; i++) {
      int ii = a[0] + i;
      if (ii < 0 || ii >= height) {
        continue;
      }
      for (int j = -1; j <= 1; j++) {
        int jj = a[1] + j;
        if (jj < 0 || jj >= width) {
          continue;
        }
        if (ii == 0 && jj == 0) {
          continue;
        }
        neibors.add( encode(ii, jj) );
      }
    }
    // printSet("neibors=", neibors);
    return neibors;
  }

  private Set<Integer> getUnknownNeibors(int n) {
    Set<Integer> unknowns = new HashSet<Integer>();
    for (int k : getNeibors(n)) {
      char c = get(k);
      if (c == 'X' || c == 'F') {
        unknowns.add(k);
      }
    }
    // printSet("unknowns=", unknowns);
    return unknowns;
  }

  private Set<Integer> getOpenNeibors(int n) {
    Set<Integer> cells = new HashSet<Integer>();
    for (int k : getNeibors(n)) {
      char c = get(n);
      if (c >= '1' && c <= '8') {
        cells.add(k);
      }
    }
    return cells;
  }

  private int countBombs(int n) {
    char c = get(n);
    if (c < '0' || c > '8') {
      return -1;
    }
    int nBombs = c - '0';
    for (int k : getNeibors(n)) {
      c = get(k);
      if (c == '9') {
        nBombs -= 1;
      }
    }
    return nBombs;
  }

  private Set<Integer> getOpenNeiborsNeibors(int n) {
    Set<Integer> cells = getOpenNeibors(n);
    for (int k : getNeibors(n)) {
      cells.addAll( getOpenNeibors(k) );
    }
    cells.remove(n);
    return cells;
  }

  public int check() {
    int count = 0;
    while (true) {
      int n = checkAroundAllOnce();
      System.err.println("checked -> " + n);
      if (n == 0) {
        n = checkAllDistance2();
        if (n == 0) {
          break;
        }
      }
      count += n;
    }
    return count;
  }

  private int checkAroundAllOnce() {
    int count = 0;
    for (int n = 0; n < height * width; n++) {
      count += checkAround(n);
    }
    return count;
  }

  private int checkAround(int n) {
    char c = get(n);
    // System.err.println("" + n + " -> " + c);
    if (c < '0') {
      return 0;
    }
    if (c > '8') {
      return 0;
    }
    Set<Integer> unknowns = getUnknownNeibors(n);
    int nUnknowns = unknowns.size();
    int nBombs = countBombs(n);
    if (nUnknowns == 0) {
      return 0;
    }
    int[] a = decode(n);
    System.err.printf("(%d,%d) nBombs=%d nUnknown=%d%n", a[0], a[1], nBombs, nUnknowns);
    if (nBombs == nUnknowns) {
      return setBombs(unknowns);
    }
    if (nBombs == 0) {
      return setNoBombs(unknowns);
    }
    return 0;
  }

  private int setBombs(Set<Integer> set) {
    return set(set, '9');
  }

  private int setNoBombs(Set<Integer> set) {
    return set(set, 'A');
  }

  private int set(Set<Integer> set, char c) {
    int count = 0;
    for (int n : set) {
      set(n, c);
      count += 1;
      int[] a = decode(n);
      System.err.printf("(%d,%d) -> %c%n", a[0], a[1], c);
    }
    return count;
  }

  private void printSet(String s, Set<Integer> set) {
    System.err.print(s);
    boolean first = true;
    for (int n : set) {
      if (first) {
        first = false;
      }
      else {
        System.err.print(", ");
      }
      System.err.print("" + n);
    }
    System.err.println();
  }

  //

  private int checkAllDistance2() {
    for (int n = 0; n < height * width; n++) {
      int count = checkDistance2(n);
      if (count > 0) {
        return count;
      }
    }
    return 0;
  }

  private int checkDistance2(int n) {
    char c = get(n);
    if (c <= '0' || c >= '9') {
      return 0;
    }
    Set<Integer> unknowns = getUnknownNeibors(n);
    if (unknowns.size() == 0) {
      return 0;
    }
    int nBombs = countBombs(n);
    if (nBombs == 0) {
      return 0;
    }
    for (int m : getOpenNeiborsNeibors(n)) {
      int count = checkDistance2(n, unknowns, nBombs, m);
      if (count > 0) {
        return count;   
      }
    }
    return 0;
  }

  private int checkDistance2(int n, Set<Integer> unknowns, int nBombs, int m) {
    int nBombs2 = countBombs(m);
    if (nBombs2 == 0) {
      return 0;
    }
    Set<Integer> unknowns2 = getUnknownNeibors(m);
    if (unknowns2.size() == 0) {
      return 0;
    }
    if (! unknowns2.containsAll(unknowns)) {
      return 0;
    }
    int nDiff = unknowns2.size() - unknowns.size();
    if (nDiff == 0) {
      return 0;
    }
    unknowns2.removeAll(unknowns);
    if (nDiff == nBombs2 - nBombs) {
      return setBombs(unknowns2);
    }
    if (nBombs2 == nBombs) {
      return setNoBombs(unknowns2);
    }
    return 0;
  }
}
