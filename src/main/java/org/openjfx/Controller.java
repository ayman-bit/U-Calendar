package org.openjfx;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXDrawersStack;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
@Author Ayman Abu Awad
 */
public class Controller implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXDrawer sidepanel;

    @FXML
    private JFXDrawer mainPanel;

    @FXML
    private JFXHamburger options;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            VBox box = FXMLLoader.load(getClass().getResource("SidePanel.fxml"));
            sidepanel.setSidePane(box);
            sidepanel.setDefaultDrawerSize(150);

            AnchorPane YearView = FXMLLoader.load(getClass().getResource("YearView.fxml"));
            mainPanel.setSidePane(YearView);
            mainPanel.setDefaultDrawerSize(-30);


            HamburgerBasicCloseTransition menu = new HamburgerBasicCloseTransition(options);
            menu.setRate(-1);
            options.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
                menu.setRate(menu.getRate() * -1);
                menu.play();

                if (sidepanel.isOpened()) {
                    sidepanel.close();
                } else {
                    sidepanel.open();
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
