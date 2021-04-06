package org.openjfx;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class AddLabs_Controller {
    @FXML
    private JFXTextField labNumber,labWeight;

    @FXML
    private JFXDatePicker labDate;

    @FXML
    private JFXTimePicker labStartTime,labEndTime;

    @FXML
    private Label className;

    @FXML
    private JFXColorPicker labColour;

    @FXML
    void AddLab(MouseEvent event)throws IOException {
        if(labDate.getValue() == null||labNumber.getText() == null||labWeight.getText()==null || labStartTime.getValue()==null || labEndTime.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Some information was not entered");
            alert.showAndWait();
            return;
        }
        else{
            if(labEndTime.getValue().isBefore(labStartTime.getValue())|| labStartTime.getValue().isAfter(labEndTime.getValue())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Time input incorrect");
                alert.showAndWait();
                return;
            }
            else{
                String qu = "INSERT INTO subEvents(eventName,subeventName,subeventWeight,subeventDate,subStartTime,subEndTime,eventColour,user_id) VALUES ("
                        + "'" + className.getText() + "',"
                        + "'" + labNumber.getText() + "',"
                        + "'" + labWeight.getText() + "',"
                        + "'" + labDate.getValue().toString() + "',"
                        + "'" + labStartTime.getValue().toString() + "',"
                        + "'" + labEndTime.getValue().toString() + "',"
                        + "'" + labColour.getValue().toString() + "',"
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
    }

    void setClassName(String text){
        className.setText(text);
    }

    void clearSelection(){
        labNumber.setText("");
        labWeight.setText("");
        labDate.getEditor().clear();
        labDate.setValue(null);
        labStartTime.setValue(null);
        labEndTime.setValue(null);
        labColour.setValue(null);
    }
}





