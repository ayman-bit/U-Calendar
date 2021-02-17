package Application;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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
    private JFXHamburger options;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            VBox box = FXMLLoader.load(getClass().getResource("SidePanel.fxml"));
            sidepanel.setSidePane(box);

            for (Node node : box.getChildren()){
                node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    switch (node.getAccessibleText()){

                    }

                });
            }

            HamburgerBasicCloseTransition menu = new HamburgerBasicCloseTransition(options);
            menu.setRate(-1);
            options.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
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
