/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.chertenok.tictactoe;

import java.util.Random;

/**
 * @author chertenokru
 */
public class PlayerComputer extends Player {

    class Point {
        int myLen;
        int youLen;
        int lenFree;
        int x;
        int y;

        public Point(int myLen, int youLen, int lenFree, int x, int y) {
            this.myLen = myLen;
            this.youLen = youLen;
            this.lenFree = lenFree;
            this.x = x;
            this.y = y;
        }
    }


    public PlayerComputer(Field field, int symbol) {
        super(field, symbol, "Компьютер");
        isComputer = true;
    }

    @Override
    public void NextMove() {
        if (field.getCellUsesCount() <= 1) NextRandom();
        else NextSearch();

    }

    private int[] checkVector(int x, int y, int dx1, int dy1, int dx2, int dy2, int maxLenMy, int maxLenYou) {
        Point len1;
        Point len2;
        len1 = getLineLen(x, y, dx1, dy1, getSymbolInt());
        len2 = getLineLen(x, y, dx2, dy2, getSymbolInt());


        // общая длина реальная в случае хода
        int my = len1.myLen + len2.myLen + 1;
        // общая возможная длина с учётом пустых клеток (пустые клетки присваиваем тому у кого есть заполненные рядом)
        int myFree = ((len1.youLen == 0) ? (len1.myLen + len1.lenFree) : len1.myLen) + ((len2.youLen == 0) ? (len2.myLen + len2.lenFree) : len2.myLen) + 1;
        // открытая ли позиция с одной стороны
        int priorityYou = ((len1.myLen == 0) && (len1.lenFree > 0)) ? 1 : 0;
        // и с другой
        priorityYou += ((len2.myLen == 0) && (len2.lenFree > 0)) ? 1 : 0;
        // открытая ли позиция с одной стороны
        int priorityMy = ((len1.youLen == 0) && (len1.lenFree > 0)) ? 1 : 0;
        // и с другой
        priorityMy += ((len2.youLen == 0) && (len2.lenFree > 0)) ? 1 : 0;


        int you = len1.youLen + len2.youLen + 1;
        // общая возможная длина с учётом пустых клеток (пустые клетки присваиваем тому у кого есть заполненные рядом)
        // если проверять свои >0 то бага если клетка пустые, ибо тогда надо всем прибавлять
        int youFree = ((len1.myLen == 0) ? (len1.youLen + len1.lenFree) : len1.youLen) + ((len2.myLen == 0) ? (len2.youLen + len2.lenFree) : len2.youLen) + 1;

        // и если только потенциальная длина поля больше или равна нужной для победы,
        // то только тогда сравниваем её с ранее максимальной
        if ((maxLenMy < my) && (myFree >= field.getCountToWin())) maxLenMy = my;
        if ((maxLenYou < you) && (youFree >= field.getCountToWin())) maxLenYou = you;

        return new int[]{maxLenMy, maxLenYou, priorityMy, priorityYou};
    }

    private void NextSearch() {
        // перебираем все пустые
        int maxLenMy = 0;
        int maxLenYou = 0;
        int maxLenMyPriority = 0;
        int maxLenYouPriority = 0;

        int myX = 0;
        int myY = 0;
        int youX = 0;
        int youY = 0;
        int[] p;
        for (int x = 0; x < field.getCountCol(); x++) {
            for (int y = 0; y < field.getCountRow(); y++) {
                if (field.getCellState(x, y) == Cell.SPACE) {
                    // и высчитываем свои и чужие позиции в результате такого хода
                    // todo добавить ещё один параметр для обоих игроков
                    // todo - сколько линий из 8 продолжится, учитывать при равной длине
                    int oldMaxMy = maxLenMy;
                    int oldMaxYou = maxLenYou;
                    int oldMaxMyPriority = maxLenMyPriority;
                    int oldMaxYouPriority = maxLenYouPriority;


                    // горизонт
                    p = checkVector(x, y, -1, 0, 1, 0, maxLenMy, maxLenYou);
                    // если новая длина больше
                    // или равна, но у новой клетки больше открытых сторон, то сохраняем
                    if (maxLenMy < p[0] || (maxLenMy == p[0] && maxLenMyPriority < p[2])) {
                        maxLenMy = p[0];
                        maxLenMyPriority = p[2];
                    }

                    if ((maxLenYou < p[1] || (maxLenYou == p[1] && maxLenYouPriority < p[3]))) {
                        maxLenYou = p[1];
                        maxLenMyPriority = p[3];
                    }


                    // вертикаль
                    p = checkVector(x, y, 0, 1, 0, -1, maxLenMy, maxLenYou);
                    // если новая длина больше то сохраняем
                    if (maxLenMy < p[0] || (maxLenMy == p[0] && maxLenMyPriority < p[2])) {
                        maxLenMy = p[0];
                        maxLenMyPriority = p[2];
                    }

                    if ((maxLenYou < p[1] || (maxLenYou == p[1] && maxLenYouPriority < p[3]))) {
                        maxLenYou = p[1];
                        maxLenMyPriority = p[3];
                    }

                    // диагональ 1
                    p = checkVector(x, y, 1, 1, -1, -1, maxLenMy, maxLenYou);
                    // если новая длина больше то сохраняем
                    if (maxLenMy < p[0] || (maxLenMy == p[0] && maxLenMyPriority < p[2])) {
                        maxLenMy = p[0];
                        maxLenMyPriority = p[2];
                    }

                    if ((maxLenYou < p[1] || (maxLenYou == p[1] && maxLenYouPriority < p[3]))) {
                        maxLenYou = p[1];
                        maxLenMyPriority = p[3];
                    }

                    // диагональ 2
                    p = checkVector(x, y, -1, +1, +1, -1, maxLenMy, maxLenYou);
                    // если новая длина больше то сохраняем
                    if (maxLenMy < p[0] || (maxLenMy == p[0] && maxLenMyPriority < p[2])) {
                        maxLenMy = p[0];
                        maxLenMyPriority = p[2];
                    }

                    if ((maxLenYou < p[1] || (maxLenYou == p[1] && maxLenYouPriority < p[3]))) {
                        maxLenYou = p[1];
                        maxLenMyPriority = p[3];
                    }

                    if ((oldMaxMy != maxLenMy) || (oldMaxMyPriority != maxLenMyPriority))    {
                        myX = x;
                        myY = y;
                    }

                    if ((oldMaxYou != maxLenYou) || (oldMaxYouPriority != maxLenYouPriority )) {
                        youX = x;
                        youY = y;
                    }


                }
            }
        }
        System.out.printf("%n  длина посл игрока = %d  длина посл комп = %d,  лучший ход компьютера (%d,%d), лучший ход игрока (%d,%d) ", maxLenYou, maxLenMy, myX, myY, youX, youY);
        // если можно выиграть то выигрываем
        if (field.getCountToWin() <= maxLenMy) {
            field.setCellState(myX, myY, getSymbolInt());
            System.out.print("если можно выиграть то выигрываем");
        } else
            // если можно помешать выиграть то мешаем
            if (field.getCountToWin() <= maxLenYou) {
                field.setCellState(youX, youY, getSymbolInt());
                System.out.println("если можно помешать выиграть то мешаем");
            } else
                // если макс комбинация меньше равно 2, то строим своё, иначе мешаем
                if (maxLenYou <= 2) {
                    field.setCellState(myX, myY, getSymbolInt());
                    System.out.println("если макс комбинация меньше равно 2, то строим своё");
                } else {
                    field.setCellState(youX, youY, getSymbolInt());
                    System.out.printf("если макс комбинация больше/равно 2,  мешаем");
                }
    }

    private Point getLineLen(int x, int y, int dx, int dy, int symbolInt) {
        int youLen = 0; // позиции игрока
        int myLen = 0;  // позиции компьютера
        int lenFree = 0; // кол-во свободных клеток
        int old_x = x;
        int old_y = y;

        boolean myStop = false;
        boolean youStop = false;

        int loopCount = 0;

        while ((x + dx >= 0 && y + dy >= 0 && x + dx < field.getCountCol() && y + dy < field.getCountRow())
                && loopCount < field.getCountToWin()) {

            // если обе последовательности прерваны и пустоты тоже то на выход
            if (myStop && youStop && field.getCellState(x + dx, y + dy) != Cell.SPACE) break;

            if (field.getCellState(x + dx, y + dy) == symbolInt) {
                // my
                myLen++;
                youStop = true;
            } else if (field.getCellState(x + dx, y + dy) != Cell.SPACE) {
                //you
                youLen++;
                myStop = true;
            } else {
                lenFree++;
                myStop = true;
                youStop = true;
            }

            x += dx;
            y += dy;
            loopCount++;

        }
        return new Point(myLen, youLen, lenFree, old_x, old_y);
    }

    private void NextRandom() {
        Random rand = new Random();
        int x;
        int y;
        do {
            x = rand.nextInt(field.getCountCol());
            y = rand.nextInt(field.getCountRow());
        } while (field.getCellState(x, y) != Cell.SPACE);
        field.setCellState(x, y, getSymbolInt());

    }

}
