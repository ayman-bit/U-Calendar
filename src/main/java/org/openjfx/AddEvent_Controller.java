package org.openjfx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AddEvent_Controller {


    @FXML
    void Done(MouseEvent event) {

    }

    @FXML
    void Cancel(MouseEvent event) throws IOException {
        Parent MainApp = FXMLLoader.load(getClass().getResource("Application.fxml"));
        Scene mainScene = new Scene(MainApp);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Add Class");
        window.setScene(mainScene);
        window.show();
    }

    @FXML
    void initialize() {

    }
}
