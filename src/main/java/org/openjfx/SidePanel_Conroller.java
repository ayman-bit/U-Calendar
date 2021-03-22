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
        Controller.start("AddEvent.fxml",event);
    }

    @FXML
    void Quit(MouseEvent event) throws IOException {
        Controller.start("Login.fxml",event);
    }


    @FXML
    void initialize() {

    }

}
