/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.awt.*;
import java.util.*;

/**
 * @author chertenokru
 */
public class Field {

    private final int countCol;
    private final int countRow;
    private final int countToWin;
    private String message = "";
    // занято ячеек, для определения конца игры по заполнению поля
    private int cellUsesCount = 0;
    private boolean isWin = false;
    private boolean isFinish = false;
    private java.util.List<Point> listWinPoint = new ArrayList<>(5);
    private java.util.List<Point> temp = new ArrayList<>(5);

    //для упрощения игроки и логика внутри класса поле
    private Player[] players = new Player[2];
    // текущий игрок
    public Player currentPlayer;

    private Cell[][] cell;


    public boolean isWin() {
        return isWin;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public int getCountCol() {
        return countCol;
    }

    public int getCountRow() {
        return countRow;
    }

    public int getCountToWin() {
        return countToWin;
    }

    public String getMessage() {
        return message;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void newGame() {
        // инизиализируем ячейки на пусто
        for (int x = 0; x < countCol; x++) {
            for (int y = 0; y < countRow; y++) {
                cell[x][y] = new Cell();
                cell[x][y].setState(Cell.SYM_P);
            }
        }
        isWin = false;
        isFinish = false;
        currentPlayer = players[0];
        listWinPoint.clear();
        message = "Игра перезапущена, Ваш ход !";
    }

    public int getCellUsesCount() {
        return cellUsesCount;
    }

    public String getCelltoString(int x, int y) {
        return cell[x][y].toString();
    }

    // переключает ход
    public void turnPlayer() {
        if (currentPlayer == players[0]) currentPlayer = players[1];
        else currentPlayer = players[0];
        message = "";
    }
    public boolean checkWinCell(int x, int y){
        Point p = new Point(x,y);
        return (listWinPoint.contains(p));
    }

    public boolean checkWin() {

        for (int x = 0; x < countCol; x++) {
            for (int y = 0; y < countRow; y++) {
                // если не пусто, то проверяем ячейку на все исходящие линии
                if (cell[x][y].getState() != Cell.SYM_P)
                    // если выигрыш, то не продолжаем
                    if (checkCell(x, y)) {
                        isWin = true;
                        isFinish = true;
                        break;
                    }
            }
        }


        if (isWin) message = "Игра окончена, победа ";
        return isWin;
    }

    private boolean checkCell(int x, int y) {
        // считаем длину одинаковых символов в заданном направлении
        if (checkLine(x, y, 0, 1) >= countToWin) return true;
        if (checkLine(x, y, 1, 1) >= countToWin) return true;
        if (checkLine(x, y, 1, 0) >= countToWin) return true;
        if (checkLine(x, y, 0, -1) >= countToWin) return true;
        if (checkLine(x, y, -1, 1) >= countToWin) return true;
        if (checkLine(x, y, -1, -1) >= countToWin) return true;
        if (checkLine(x, y, 0, -1) >= countToWin) return true;
        if (checkLine(x, y, 1, -1) >= countToWin) return true;

        return false;
    }

    // считаем длину одинаковых символов в заданном направлении
    private int checkLine(int x, int y, int dx, int dy) {
        boolean stop = false;
        int symbol = cell[x][y].getState();
        int count = 1;
        temp.clear();
        temp.add(new Point(x,y));

        while ((x + dx >= 0 && y + dy >= 0 && x + dx < countCol && y + dy < countRow)
                && (getCellState(x + dx, y + dy) == symbol)) {
            count++;
            x += dx;
            y += dy;
            temp.add(new Point(x,y));
        }
        if (count >= countToWin) {
            listWinPoint.clear();
            listWinPoint.addAll(temp);
        }
        return count;
    }


    public boolean checkFinish() {
        if (!isFinish) isFinish = cellUsesCount == countCol * countRow;
        if (isFinish && !isWin) message = "Игра окончена, больше ходов нет";
        return isFinish;
    }


    public int getCellState(int x, int y) {
        return cell[x][y].getState();

    }

    public void setCellState(int x, int y, int state) {
        if (cell[x][y].getState() != Cell.SYM_P) return;
        cell[x][y].setState(state);
        cellUsesCount++;
    }

    public void onCellClick(int x, int y) {

        if (cell[x][y].getState() != Cell.SYM_P) return;
        if (currentPlayer.getIsComputer()) return;
        if (isFinish || isWin) return;
        setCellState(x, y, currentPlayer.getSymbolInt());
        checkWin();
        checkFinish();
        if (!isFinish && !isWin)
            turnPlayer();
        currentPlayer.NextMove();
        checkWin();
        checkFinish();
        if (!isFinish && !isWin)
            turnPlayer();

    }

    public Field(int col, int row, int winCount) {
        countCol = col;
        countRow = row;
        countToWin = winCount;
        // создаём массив
        cell = new Cell[countCol][countRow];
        // инизиализируем ячейки на пусто
        for (int x = 0; x < countCol; x++) {
            for (int y = 0; y < countRow; y++) {
                cell[x][y] = new Cell();
                cell[x][y].setState(Cell.SYM_P);
            }
        }

        // создаём игроков

        players[0] = new Player(this, Cell.SYM_O, "Игрок") {
            @Override
            public void NextMove() {
            }
        };
        players[1] = new PlayerComputer(this, Cell.SYM_X);

        currentPlayer = players[0];

        message = "Игра начата, Ваш ход !";
    }


}
