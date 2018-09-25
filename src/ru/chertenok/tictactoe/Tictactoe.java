/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.chertenok.tictactoe;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import ru.chertenok.tictactoe.draw.FieldDraw;
import ru.chertenok.tictactoe.logic.Field;

/**
 * @author chertenokru
 *
 *
 *         Задача 2 - Field.checkWin()
 *
 *         Задача 3 - PlayerComputer.NextSearch()
 *
 *
 *
 *
 */
public class Tictactoe extends Application {

    private Field field;
    private FieldDraw fieldDraw;

    @Override
    public void start(Stage primaryStage) {
        field = new Field(15, 15, 5);
        TilePane root = new TilePane(Orientation.VERTICAL);
        root.setAlignment(Pos.CENTER);
        fieldDraw = new FieldDraw(root, field, 35);
        Scene scene = new Scene(root, 700, 640);
        primaryStage.setTitle("Крестики-Нолики");
        // Устанавливаем иконку приложения.
        primaryStage.getIcons().add(new Image( Tictactoe.class.getResource("/ru/chertenok/tictactoe/resources/image/ico.png").toString()));

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
