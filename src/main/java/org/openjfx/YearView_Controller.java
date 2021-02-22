package org.openjfx;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class YearView_Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private GridPane mainPanel;

    @FXML
    void CreateGrid (){
        int rows = 6;
        int cols = 7;

        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                // Add VBox and style it
                Label vPane = new Label(String.valueOf(j*i));
                // Add it to the grid
                mainPanel.add(vPane,j, i);
            }
        }
    }


    @FXML
    private JFXTextField CurrentMonth;

    @FXML
    void Today(MouseEvent event) {

    }

    @FXML
    void backwardButton(MouseEvent event) {

    }

    @FXML
    void forwardButton(MouseEvent event) {

    }

    @FXML
    void initialize() {
        assert mainPanel != null : "fx:id=\"Grid\" was not injected: check your FXML file 'YearView.fxml'.";
        assert CurrentMonth != null : "fx:id=\"CurrentMonth\" was not injected: check your FXML file 'YearView.fxml'.";
        CreateGrid ();
    }
}
