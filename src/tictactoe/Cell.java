/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

/**
 *
 * @author chertenokru
 */
public class Cell {

    private int state;
    public final static int SYM_P = 0;
    public final static int SYM_X = 1;
    public final static int SYM_O = 2;

    public void setState(int state) {
        this.state = state;
    }
  

    public int getState() {
        return state;
    }

    public void setStateX() {
        state = SYM_X;
    }

      public void setStateInit() {
        state = SYM_P;
    }

    public void setStateO() {
        state = SYM_O;
    }

    @Override
    public String toString() {
        if (state == SYM_X) {
            return "x";
        } else if (state == SYM_O) {
            return "0";
        } else {
            return " ";
        }

    }

    public Cell() {
        state = SYM_P;
    }
}
