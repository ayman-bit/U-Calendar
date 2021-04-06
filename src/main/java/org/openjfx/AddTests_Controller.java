package org.openjfx;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
    private JFXColorPicker testColour;

    @FXML
    void AddTest(MouseEvent event)throws IOException {
        if(testDate.getValue() == null||testNumber.getText() == null||testWeight.getText()==null || testStartTime.getValue() == null || testEndTime.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Required information was not entered");
            alert.showAndWait();
        }
        else{
            if(testEndTime.getValue().isBefore(testStartTime.getValue()) || testStartTime.getValue().isAfter(testEndTime.getValue())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Time input incorrect");
                alert.showAndWait();
            }
            else{
                String qu = "INSERT INTO subEvents(eventName,subeventName,subeventWeight,subeventDate,subStartTime,subEndTime,eventColour,user_id) VALUES ("
                        + "'" + this.className.getText() + "',"
                        + "'" + testNumber.getText() + "',"
                        + "'" + testWeight.getText() + "',"
                        + "'" + testDate.getValue().toString() + "',"
                        + "'" + testStartTime.getValue().toString() + "',"
                        + "'" + testEndTime.getValue().toString() + "',"
                        + "'" + testColour.getValue().toString() + "',"
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
        testColour.setValue(null);
    }

}