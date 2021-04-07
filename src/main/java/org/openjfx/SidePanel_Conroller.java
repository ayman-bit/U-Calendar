package org.openjfx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ayman Abu Awad
 */

public class SidePanel_Conroller {

    @FXML
    private ImageView icon;

    @FXML
    void AddClass(MouseEvent event) throws IOException {
        Controller.start("AddEvent.fxml",event);
    }

    @FXML
    void OpenCalculator(MouseEvent event) throws IOException{
        Controller.start("Calculator.fxml", event);
    }

    @FXML
    void Tasks(MouseEvent event) throws IOException {
        Controller.start("Tasks.fxml",event);
    }

    @FXML
    void Quit(MouseEvent event) throws IOException {
        Controller.start("Login.fxml",event);
    }

    @FXML
    void EditEvent(MouseEvent event) throws IOException {
        Controller.start("EditEvent.fxml", event);
    }

    @FXML
    void DeleteEvent(MouseEvent event) throws IOException {
        Controller.start("DeleteEvent.fxml", event);
    }

    @FXML
    void changeicon(MouseEvent event) {
    File file = new File("src/main/resources/org/openjfx/Images/egg.gif");
    Image image = new Image(file.toURI().toString());
    icon.setImage(image);
    }


    @FXML
    void initialize() {

    }

}
