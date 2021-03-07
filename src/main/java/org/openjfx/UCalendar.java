package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.Calendar;

import java.io.IOException;

/*
@Author Ayman Abu Awad
@Mohammed Shahwan
 */
public class UCalendar extends Application {

    private static Scene scene;

    @Override
    public void start(Stage primaryStage) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);
        primaryStage.setTitle("U-Calendar");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}