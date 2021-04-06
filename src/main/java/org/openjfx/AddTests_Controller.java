package org.openjfx;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class AddTests_Controller {

    @FXML
    private JFXTextField testNumber,testWeight;

    @FXML
    private JFXDatePicker testDate;

    @FXML
    private JFXTimePicker testStartTime, testEndTime;

    @FXML
    private JFXButton addTest, Cancel;

    @FXML
    private Label className;

    @FXML
    void AddTest(MouseEvent event)throws IOException {
        if(testDate.getValue() == null||testNumber.getText() == null||testWeight.getText()==null || testStartTime.getValue() == null || testEndTime.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Required information was not entered");
            alert.showAndWait();
            return;
        }
        else{
            if(testEndTime.getValue().isBefore(testStartTime.getValue())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("End time must be after start time");
                alert.showAndWait();
            }
            else{
                String eventName = className.getText();
                String subeventDate= testDate.getValue().toString();
                String subEventName= testNumber.getText();
                String subeventWeight = testWeight.getText();
                String subStartTime = testStartTime.getValue().toString();
                String subEndTime = testEndTime.getValue().toString();

                String qu = "INSERT INTO subEvents(eventName,subeventName,subeventWeight,subeventDate,subStartTime,subEndTime,user_id) VALUES ("
                        + "'" + eventName + "',"
                        + "'" + subEventName + "',"
                        + "'" + subeventWeight + "',"
                        + "'" + subeventDate + "',"
                        + "'" + subStartTime + "',"
                        + "'" + subEndTime + "',"
                        + "'" + Login_Controller.uid + "'"
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
                    alert.setContentText("This Data Already Exists");
                    alert.showAndWait();
                }

                clearSelection();
            }
        }

    }

    void setClassName(String text){
        className.setText(text);
    }

    void clearSelection(){
        testNumber.setText("");
        testWeight.setText("");
        testDate.getEditor().clear();
        testDate.setValue(null);
        testStartTime.setValue(null);
        testEndTime.setValue(null);
    }

    @FXML
    void Cancel(MouseEvent e) throws IOException{
        Stage stage = (Stage) Cancel.getScene().getWindow();
        stage.close();
    }
}