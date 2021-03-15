package org.openjfx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ayman Abu Awad
 */

public class SidePanel_Conroller {



    @FXML
    void AddClass(MouseEvent event) throws IOException {
        Parent AddEvent = FXMLLoader.load(getClass().getResource("AddEvent.fxml"));
        Scene mainScene = new Scene(AddEvent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Add Class");
        window.setScene(mainScene);
        window.show();
    }

    @FXML
    void Quit(MouseEvent event) { System.exit(0);}


    @FXML
    void initialize() {

    }

}
