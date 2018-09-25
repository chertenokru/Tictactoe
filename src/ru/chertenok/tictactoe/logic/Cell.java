/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.chertenok.tictactoe.logic;

/**
 *
 * @author chertenokru
 */
public class Cell {

    private int state;
    public final static int SPACE = 0;
    public final static int X = 1;
    public final static int O = 2;

    public void setState(int state) {
        this.state = state;
    }
  

    public int getState() {
        return state;
    }

    @Override
    public String toString() {
        if (state == X) {
            return "X";
        } else if (state == O) {
            return "0";
        } else {
            return " ";
        }

    }

    public Cell() {
        state = SPACE;
    }
}
