package org.openjfx;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class AddAssigns_Controller {
    @FXML
    private JFXTextField assignNumber,assignWeight;

    @FXML
    private JFXDatePicker assignDate;

    @FXML
    private JFXButton addAssign, Cancel;

    @FXML
    private Label className;

    @FXML
    private Pane drag;

    @FXML
    void AddAssign(MouseEvent event)throws IOException {
        if(assignDate.getValue() == null||assignNumber.getText() == null||assignWeight.getText()==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Required information was not entered");
            alert.showAndWait();
        }
        else{
            String qu = "INSERT INTO subEvents(eventName,subeventName,subeventWeight,subeventDate,user_id) VALUES ("
                    + "'" + className.getText() + "',"
                    + "'" + assignNumber.getText() + "',"
                    + "'" + assignWeight.getText() + "',"
                    + "'" + assignDate.getValue().toString() + "',"
                    + "'" + Login_Controller.uid + "'"
                    + ")";

            if (DatabaseHandler.execAction(qu)) { //Success
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Success");
                alert.showAndWait();
            } else { // Error
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("This Data Already Exists");
                alert.showAndWait();
            }

            clearSelection();
        }
    }

    void setClassName(String text){
        className.setText(text);
    }

    void clearSelection(){
        assignNumber.setText("");
        assignWeight.setText("");
        assignDate.getEditor().clear();
        assignDate.setValue(null);
    }

    @FXML
    void Cancel(MouseEvent e) throws IOException{
        Stage stage = (Stage) Cancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize() {
        //Controller.makeStageDragable(drag);
    }
}
