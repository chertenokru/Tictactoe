/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.chertenok.tictactoe.logic;

import ru.chertenok.tictactoe.SoundEngine;

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
    // храним выигрышную линию для отрисовки
    private java.util.List<Point> listWinPoint = new ArrayList<>(5);
    // временный список текущей линии
    private java.util.List<Point> temp = new ArrayList<>(5);

    //для упрощения игроки и логика внутри класса поле
    private Player[] players = new Player[2];
    // текущий игрок
    private Player currentPlayer;
    // игровое поле
    private Cell[][] cell;
    // последний ход
    private int[] lastTurn = new int[2];


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
        SoundEngine.stopSound();
        // инизиализируем ячейки на пусто
        for (int x = 0; x < countCol; x++) {
            for (int y = 0; y < countRow; y++) {
                cell[x][y] = new Cell();
                cell[x][y].setState(Cell.SPACE);
            }
        }
        isWin = false;
        isFinish = false;
        currentPlayer = players[0];
        cellUsesCount = 0;
        listWinPoint.clear();
        message = "Игра перезапущена, Ваш ход," + currentPlayer.getName() + " !";
    }

    public boolean isLastTurn(int x, int y) {
        return (lastTurn[0] == x && lastTurn[1] == y && cellUsesCount > 1);
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
        SoundEngine.playTurn();
    }

    //проверяем входит ли ячейка в выигрышную серию для отрисовки
    public boolean checkWinCell(int x, int y) {
        Point p = new Point(x, y);
        return (listWinPoint.contains(p));
    }

    // проверка победы
    public boolean checkWin() {

        for (int x = 0; x < countCol; x++) {
            for (int y = 0; y < countRow; y++) {
                // если не пусто, то проверяем ячейку на все исходящие линии
                if (cell[x][y].getState() != Cell.SPACE)
                    // если выигрыш, то не продолжаем
                    if (checkCell(x, y)) {
                        isWin = true;
                        isFinish = true;
                        break;
                    }
            }
        }


        if (isWin) {
            message = "Игра окончена, победа игрока " + currentPlayer.getName();
            if (currentPlayer.getIsComputer()) SoundEngine.playLoss();
            else SoundEngine.playWin();
        }

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
        temp.add(new Point(x, y));

        while ((x + dx >= 0 && y + dy >= 0 && x + dx < countCol && y + dy < countRow)
                && (getCellState(x + dx, y + dy) == symbol)) {
            count++;
            x += dx;
            y += dy;
            temp.add(new Point(x, y));
        }
        if (count >= countToWin) {
            listWinPoint.clear();
            listWinPoint.addAll(temp);
        }
        return count;
    }


    public boolean checkFinish() {
        if (!isFinish) isFinish = cellUsesCount == countCol * countRow;
        if (isFinish && !isWin) message = "Игра окончена, больше ходов нет. Ничья.";
        return isFinish;
    }


    public int getCellState(int x, int y) {
        return cell[x][y].getState();

    }

    public void setCellState(int x, int y, int state) {
        if (cell[x][y].getState() != Cell.SPACE) return;
        cell[x][y].setState(state);
        cellUsesCount++;
        //  если ход компа то сохраняем последний ход для отрисовки
        if ((players[0].getIsComputer() && players[0].getSymbolInt() == state) || (players[1].getIsComputer() && players[1].getSymbolInt() == state)) {
            lastTurn[0] = x;
            lastTurn[1] = y;
        }
    }

    public void onCellClick(int x, int y) {

        if (cell[x][y].getState() != Cell.SPACE) return;
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
                cell[x][y].setState(Cell.SPACE);
            }
        }

        // создаём игроков

        players[0] = new Player(this, Cell.O, "Игрок") {
            @Override
            public void NextMove() {
            }
        };
        players[1] = new PlayerComputer(this, Cell.X);

        currentPlayer = players[0];

        message = "Игра начата, Ваш ход " + currentPlayer.getName() + "!";
    }


}
