package org.openjfx;


import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author Ayman Abu Awad
 */

public class Login_Controller implements Initializable {

    @FXML
    private JFXTextField username;
    public static String uname;
    public static int uid;

    @FXML
    private JFXPasswordField password;

    @FXML
    private void handleLogin(MouseEvent event) throws IOException {

//        QU checks the information entered in the user text box and verifies in the database that it exists, It's also a checks the password and make sure that the password is the same as entered if the user exists.
        List<Map<String, Object>> QU = DatabaseHandler.execQuery("SELECT * FROM loginInfo WHERE (username = '" + username.getText() + "' AND password = '" + password.getText() + "')");
        List<Map<String, Object>> QUCheck = DatabaseHandler.execQuery("SELECT * FROM loginInfo WHERE (username = '" + username.getText() + "')");

//        This line of code converts objects to string so I can verify the information
        String pass = "";
        for (Map<String, Object> set : QU) {
            uname = (String) set.get("username");
            pass = (String) set.get("password");
            uid = (int) set.get("id");
        }
        String unameCheck = null;
        for (Map<String, Object> set : QUCheck) {
            unameCheck = (String) set.get("username");
            uid = (int) set.get("id");
        }
        System.out.println(unameCheck);
//        This if statement check info and verifies if credentials are correct
        if (username.getText().isEmpty()&&!password.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Username");
            alert.showAndWait();
            return;
        } else if (password.getText().isEmpty()&&!username.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Password");
            alert.showAndWait();
            return;
        }else if (password.getText().isEmpty()&&username.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Username and Password");
            alert.showAndWait();
            return;
        } else if ((uname != null && !uname.isEmpty()) && (pass != null && !pass.isEmpty())) {
            Controller.start("Application.fxml",event);
        } else if ((unameCheck != null && !unameCheck.isEmpty())) { // Error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("The password entered is wrong!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("The Username and Password Enter dont exist\nPlease Sign-Up!");
            alert.showAndWait();
            return;
        }
    }

    @FXML
    void handleDelete(MouseEvent event) {

        String id= username.getText();
        String psw= password.getText();

        if(id.isEmpty()||psw.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Username and Password");
            alert.showAndWait();
            return;
        }

        String qu = "DELETE FROM loginInfo WHERE (username = '" + id + "' AND password = '" + psw + "')";

        if(DatabaseHandler.execAction(qu)){ //Success
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();
        }
        else{ // Error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("This account has already been deleted\nPlease Try a Different Username");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleClose (MouseEvent event){
        System.exit(0);
    }

    @FXML
    private void handleMin (MouseEvent event){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setIconified(true);

        /*
        Trying to implement the minimize full screen and close button using Node..
        For some reason it does not seem to work on Mac  >.>
         */
        //stage.setFullScreen(true);
        //stage.close();
    }

    @FXML
    private void handleSignup (MouseEvent event){

        String id= username.getText();
        String psw= password.getText();

        if(id.isEmpty()&&!psw.isEmpty()){
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setHeaderText(null);
          alert.setContentText("Please Enter Username");
          alert.showAndWait();
          return;
        }

        if(psw.isEmpty()&&!id.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Password");
            alert.showAndWait();
            return;
        }

        if(id.isEmpty()||psw.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Username and Password");
            alert.showAndWait();
            return;
        }

        String qu = "INSERT INTO loginInfo (username,password) VALUES ("
                + "'" + id + "',"
                + "'" + psw + "'"
                + ")";

        if(DatabaseHandler.execAction(qu)){ //Success
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();
        }
        else{ // Error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("This Username Already Exists\nPlease Try a Different Username");
            alert.showAndWait();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
