package org.openjfx;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class WeekView_Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label hoursPanel;

    @FXML
    private GridPane mainPanel;

    @FXML
    void Today(MouseEvent event) {
        Date now = new Date();
        System.out.println(now);
    }

    @FXML
    void backwardButton(MouseEvent event) {

    }

    @FXML
    void forwardButton(MouseEvent event) {

    }

    @FXML
    void initialize() {
        assert hoursPanel != null : "fx:id=\"hoursPanel\" was not injected: check your FXML file 'WeekView.fxml'.";
        assert mainPanel != null : "fx:id=\"Grid\" was not injected: check your FXML file 'WeekView.fxml'.";

    }
}
