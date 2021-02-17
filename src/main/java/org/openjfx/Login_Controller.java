package org.openjfx;


import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;





/*
@author Ayman Abu Awad
 */


public class Login_Controller implements Initializable {

    private Label label;

    Preferences preferences;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    void handleLogin(ActionEvent event) {

    }

    @FXML
    void handleMin(MouseEvent event) {

    }

    @FXML
    void initialize() {

    }

    @FXML
    private void handleClose (MouseEvent event){
        System.exit(0);
    }

    @FXML
    private void handleMin (ActionEvent event){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
        /*
        Trying to implement the minimize full screen and close button using Node..
        For some reason it does not seem to work on Mac  >.>
         */
        //stage.setFullScreen(true);
        //stage.close();
    }

}
