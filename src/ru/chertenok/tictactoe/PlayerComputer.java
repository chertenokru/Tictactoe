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
        int x;
        int y;

        private Point(int myLen, int youLen, int x, int y) {
            this.myLen = myLen;
            this.youLen = youLen;
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
        if (field.getCellUsesCount() < 3) NextRandom();
        else NextSearch();

    }

    private void NextSearch() {
        // перебираем все пустые

        Point len1;
        Point len2;
        int maxLenMy = 0;
        int maxLenYou = 0;
        int myX = 0;
        int myY = 0;

        int youX = 0;
        int youY = 0;


        for (int x = 0; x < field.getCountCol(); x++) {
            for(int y = 0; y <field.getCountRow(); y++) {
                if (field.getCellState(x,y) == Cell.SYM_P )
                {
                    //todo  добавить статус - открытая/полуоткрытая/закрытая линия,
                    //todo или может ли быть достигнута победа из этой точки в дальнейшем
                    //todo точки могущие принести победу должны иметь приоритет над другими такой же длины
                    // и высчитываем свои и чужие позиции в результате такого хода
                   int oldMaxMy = maxLenMy;
                   int oldMaxYou = maxLenYou;

                    // горизонт
                    len1 = getLineLen(x,y,-1,0, getSymbolInt());
                    len2 = getLineLen(x,y,1,0, getSymbolInt());
                    if (maxLenMy < (len1.myLen + len2.myLen+1) ) maxLenMy = len1.myLen + len2.myLen + 1;
                    if (maxLenYou < (len1.youLen + len2.youLen+1)) maxLenYou = len1.youLen + len2.youLen + 1;

                    // вертикаль
                    len1 = getLineLen(x,y,0,-1, getSymbolInt());
                    len2 = getLineLen(x,y,0,1, getSymbolInt());
                    if (maxLenMy < (len1.myLen + len2.myLen+1) ) maxLenMy = len1.myLen + len2.myLen + 1;
                    if (maxLenYou < (len1.youLen + len2.youLen+1)) maxLenYou = len1.youLen + len2.youLen + 1;

                    // диагональ 1
                    len1 = getLineLen(x,y,1,1, getSymbolInt());
                    len2 = getLineLen(x,y,-1,-1, getSymbolInt());
                    if (maxLenMy < (len1.myLen + len2.myLen+1) ) maxLenMy = len1.myLen + len2.myLen + 1;
                    if (maxLenYou < (len1.youLen + len2.youLen+1)) maxLenYou = len1.youLen + len2.youLen + 1;

                    // диагональ 2
                    len1 = getLineLen(x,y,-1,+1, getSymbolInt());
                    len2 = getLineLen(x,y,+1,-1, getSymbolInt());
                    if (maxLenMy < (len1.myLen + len2.myLen+1) ) maxLenMy = len1.myLen + len2.myLen + 1;
                    if (maxLenYou < (len1.youLen + len2.youLen+1)) maxLenYou = len1.youLen + len2.youLen + 1;

                    if (oldMaxMy != maxLenMy )
                    {
                        myX = x;
                        myY = y;
                    }

                    if (oldMaxYou != maxLenYou)
                    {
                        youX = x;
                        youY = y;
                    }


                }
            }
        }
        System.out.printf(" You = %d My = %d, win = %d  myx %d  myy %d , Youx %d Yoy y %d  %n",maxLenYou,maxLenMy,field.getCountToWin(),myX,myY,youX,youY);
        // если можно выиграть то выигрываем
        if (field.getCountToWin() <= maxLenMy) {
            field.setCellState(myX,myY,getSymbolInt());
            System.out.println("если можно выиграть то выигрываем");
        } else
            // если можно помешать выиграть то мешаем
        if (field.getCountToWin() <= maxLenYou) {
            field.setCellState(youX,youY,getSymbolInt());
            System.out.println("если можно помешать выиграть то мешаем");
        } else
            // если макс комбинация меньше равно 2, то строим своё, иначе мешаем
          if (maxLenYou <= 2) {
            field.setCellState(myX,myY,getSymbolInt());
              System.out.println("если макс комбинация меньше равно 2, то строим своё");
          } else
          {
              field.setCellState(youX,youY,getSymbolInt());
              System.out.printf("если макс комбинация больше/равно 2,  мешаем" );
          }
    }

    private Point getLineLen(int x, int y, int dx, int dy, int symbolInt) {
        int youLen = 0;
        int myLen = 0;
        int old_x = x;
        int old_y = y;

        boolean myStop = false;
        boolean youStop = false;

        while ((x + dx >= 0 && y + dy >= 0 && x + dx < field.getCountCol() && y + dy < field.getCountRow())
                && (field.getCellState(x + dx, y + dy) != Cell.SYM_P))
             {
            if (field.getCellState(x + dx, y + dy) == symbolInt)
            {
                // my
                myLen++;
                youStop = true;
            } else
            {
                //you
                youLen++;
                myStop = true;
            }
            // если обе последовательности прерваны то на выход
            if (myStop && youStop) break;
            else {
                x += dx; y += dy;
            }

            }


        return new Point(myLen, youLen,old_x,old_y);

    }

    private void NextRandom() {
        Random rand = new Random();
        int x;
        int y;
        do {
            x = rand.nextInt(field.getCountCol());
            y = rand.nextInt(field.getCountRow());
        } while (field.getCellState(x, y) != Cell.SYM_P);
        field.setCellState(x, y, getSymbolInt());

    }

}
