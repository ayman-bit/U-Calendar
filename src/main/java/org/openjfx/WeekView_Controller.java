package org.openjfx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class WeekView_Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private GridPane Grid;

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
        assert Grid != null : "fx:id=\"Grid\" was not injected: check your FXML file 'WeekView.fxml'.";

    }
}
