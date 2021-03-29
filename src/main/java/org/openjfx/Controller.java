package org.openjfx;

import com.jfoenix.controls.JFXDrawer;

import com.jfoenix.controls.JFXHamburger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * @author Ayman Abu Awad
 */

public class Controller implements Initializable {

    @FXML
    private AnchorPane parent;

    @FXML
    private JFXDrawer sidepanel;

    @FXML
    private JFXDrawer MonthView;

    @FXML
    private JFXDrawer WeekView;

//    @FXML
//    private JFXHamburger options;

    public static void start(String url, MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(Controller.class.getResource(url));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("U-Calendar");
        window.setScene(scene);
        window.show();
    }

    private double xOffSet = 0;
    private double yOffSet = 0;
    private void makeStageDragable() {
        parent.setOnMousePressed((event) -> {
            xOffSet = event.getSceneX();
            yOffSet = event.getSceneY();
        });
        parent.setOnMouseDragged((event) -> {
            UCalendar.stage.setX(event.getScreenX() - xOffSet);
            UCalendar.stage.setY(event.getScreenY() - yOffSet);
            UCalendar.stage.setOpacity(0.8f);
        });
        parent.setOnDragDone((event) -> {
            UCalendar.stage.setOpacity(1.0f);
        });
        parent.setOnMouseReleased((event) -> {
            UCalendar.stage.setOpacity(1.0f);
        });
    }

    @FXML
    private void minimize_stage(MouseEvent event) {
        UCalendar.stage.setIconified(true);
    }

    @FXML
    private void close_app(MouseEvent event) {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeStageDragable();
        try {
            VBox box = FXMLLoader.load(getClass().getResource("SidePanel.fxml"));
            sidepanel.setSidePane(box);
            sidepanel.setDefaultDrawerSize(-30);

            AnchorPane MonView = FXMLLoader.load(getClass().getResource("MonthView.fxml"));
            MonthView.setSidePane(MonView);
            MonthView.setDefaultDrawerSize(-30);

            AnchorPane WkView = FXMLLoader.load(getClass().getResource("WeekView.fxml"));
            WeekView.setSidePane(WkView);
            WeekView.setDefaultDrawerSize(-30);

//            HamburgerBasicCloseTransition menu = new HamburgerBasicCloseTransition(options);
//            menu.setRate(-1);
//            options.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
//                menu.setRate(menu.getRate() * -1);
//                menu.play();
//
//                if (sidepanel.isOpened()) {
//                    sidepanel.close();
//                } else {
//                    sidepanel.open();
//                }
//            });
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
