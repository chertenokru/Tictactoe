/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.chertenok.tictactoe;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.awt.*;

/**
 *
 * @author chertenokru
 */
public class FieldDraw {


    private Button[][] btn ;
    private TilePane root;
    private Field field;
    private HBox root1;
    private HBox root2;
    private final int cellSize;
    private Button butReset;
    private Label labMessage;
    private String defStyle;


    public void ReDraw() {
        labMessage.setText(field.getMessage());

        if (field.isWin())
            labMessage.setTextFill(Color.DARKRED);
        else
            labMessage.setTextFill(Color.BLUE);



        for (int x = 0; x < field.getCountCol(); x++) {
            for (int y = 0; y < field.getCountRow(); y++) {
                btn[x][y].setText(field.getCelltoString(x, y));
             //   if (field.isWin()) {
                    if (field.checkWinCell(x,y))
                    btn[x][y].setStyle("-fx-background-color: red");
                    else
                    btn[x][y].setStyle(defStyle);


            }
        }
    }

    public FieldDraw(TilePane root, Field field, int cellSize) {
        this.cellSize = cellSize;
        this.root = root;
        this.field = field;
        //TilePane root1;


        btn = new Button[field.getCountCol()][field.getCountRow()];

        root2 = new HBox();
        root2.setAlignment(Pos.BOTTOM_CENTER);

        butReset = new Button("RESET");
        defStyle = butReset.getStyle();
        butReset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                field.newGame();
                ReDraw();
            }
        });

        labMessage = new Label(field.getMessage());

        root2.getChildren().add(labMessage);
        root2.getChildren().add(butReset);
        root.getChildren().add(labMessage);
        BackgroundImage myBI= new BackgroundImage(new Image("file:resources/image/background.jpg",0,0,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
//then you set to your node
        root.setBackground(new Background(myBI));
       // root.setStyle("-fx-background-image: url(resources/image/background.jpg);");


        for (int x = 0; x < field.getCountCol(); x++) {
            root1 = new HBox();
            for (int y = 0; y < field.getCountRow(); y++) {
                btn[x][y] = new Button();
                btn[x][y].setText(field.getCelltoString(x, y));
                btn[x][y].setFont(new Font(15));
                btn[x][y].setId(String.format("%02d",x) + String.format("%02d",y));
                btn[x][y].setPrefSize(this.cellSize, this.cellSize);
                btn[x][y].setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Button b = (Button) event.getSource();
                        int x = Integer.parseInt(b.getId().substring(0, 2));
                        int y = Integer.parseInt(b.getId().substring(2, 4));
                        field.onCellClick(x, y);
                        ReDraw();
                    }
                });
                root1.getChildren().add(btn[x][y]);

            }
            root.getChildren().add(root1);
        }
        //root1.getChildren().add(root2);
        root.getChildren().add(butReset);
        ReDraw();

    }




}
