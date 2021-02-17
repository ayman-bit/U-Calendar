package Application;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;




/*
@author Ayman Abu Awad
 */


public class Login_Controller implements Initializable {

    private Label label;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO
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
