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
abstract public class Player {


    protected String name;
    protected char symbol;
    protected int symbolInt;

    /**
     * Опредяляет компьютер ходит или человек
     */
    protected boolean isComputer;
    protected Field field;

    abstract public void NextMove();

    public boolean getIsComputer() {
        return isComputer;
    }

    public char getSymbol() {
        return symbol;
    }

    public int getSymbolInt()
    { return symbolInt; }

    public  Player(Field field,int symbolInt, String name) {
        this.field = field;
        this.symbolInt = symbolInt;
        if (symbolInt == Cell.SYM_O) this.symbol = 'O'; else this.symbol = 'X';
        this.name = name;
        isComputer = false;
    }

}
