package org.openjfx;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/*
@Author Ayman Abu Awad
 */
public class SidePanel_Conroller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    @FXML
    void Quit(MouseEvent event) { System.exit(0);}


    @FXML
    void initialize() {

    }

}
