package org.openjfx;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @author Mohammed Shahwan
 */

public class EditEvent_Controller {

    List<Map<String, Object>> QU = DatabaseHandler.execQuery("SELECT * FROM userData");

    @FXML
    private JFXDatePicker date,FinalDate;

    @FXML
    private JFXTimePicker startTime,endTime,FinalStartTime, FinalEndTime;

    @FXML
    private JFXTabPane tabPane;

    @FXML
    private JFXTextField className, numAssign, numLabs, numTests;

    @FXML
    private ChoiceBox<String> eventMenu;

    @FXML
    void Next(MouseEvent e) throws IOException {
        tabPane.getSelectionModel().selectNext();
    }

    @FXML
    void Back(MouseEvent e) throws IOException{
        tabPane.getSelectionModel().selectPrevious();
    }

    @FXML
    void Cancel(MouseEvent event) throws IOException {
        Controller.start("Application.fxml",event);
    }

    @FXML
    void Done(MouseEvent event) throws IOException {
        try {
            String startD= date.getValue().toString();
            String startT= startTime.getValue().toString();
            String endT= endTime.getValue().toString();
            String name= className.getText();
            String assignNum = numAssign.getText();
            String quizNum = numTests.getText();
            String labNum = numLabs.getText();
            String DateFinal= FinalDate.getValue().toString();
            String startFT= FinalStartTime.getValue().toString();
            String endFT= FinalEndTime.getValue().toString();

            if(startD.isEmpty()||startT.isEmpty()||endT.isEmpty()||name.isEmpty() || assignNum.isEmpty()
                    || quizNum.isEmpty() || labNum.isEmpty() || DateFinal.isEmpty() || startFT.isEmpty() || endFT.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Please fill in all infomartion");
                alert.showAndWait();
                return;
            }

            String qu = "INSERT INTO userData(eventName,date,startTime,endTime,numAssign,numTest,numLabs,finalDate,finalStartTime,finalEndTime,user_id) VALUES ("
                    + "'" + name + "',"
                    + "'" + startD + "',"
                    + "'" + startT + "',"
                    + "'" + endT + "',"
                    + "'" + assignNum + "',"
                    + "'" + quizNum + "',"
                    + "'" + labNum + "',"
                    + "'" + DateFinal + "',"
                    + "'" + startFT + "',"
                    + "'" + endFT + "',"
                    + "'" + Login_Controller.uid + "'"
                    + ")";

            if(DatabaseHandler.execAction(qu)){ //Success
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Success");
                alert.showAndWait();
                Controller.start("Application.fxml",event);
            }
            else{ // Error
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("This Data Already Exists");
                alert.showAndWait();
            }

        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all infomartion");
            alert.showAndWait();
            return;
        }

    }

    @FXML
    void NextPopulate(MouseEvent event) throws IOException, ParseException {
        DateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        assert QU != null;
        for (Map<String, Object> stringObjectMap : QU) {
            if(stringObjectMap.get("eventName").toString().equals(eventMenu.getValue())) {
                System.out.println(eventMenu.getValue());
                className.setText(stringObjectMap.get("eventName").toString());
                Date temp = simpleDateFormat.parse(stringObjectMap.get("date").toString());
                date.setValue(convertToLocalDateViaInstant(temp));
                startTime.setValue(LocalTime.parse(stringObjectMap.get("startTime").toString()));
                endTime.setValue(LocalTime.parse(stringObjectMap.get("endTime").toString()));
                numAssign.setText(stringObjectMap.get("numAssign").toString());
                numTests.setText(stringObjectMap.get("numTest").toString());
                numLabs.setText(stringObjectMap.get("numLabs").toString());
                temp = simpleDateFormat.parse(stringObjectMap.get("finalDate").toString());
                FinalDate.setValue(convertToLocalDateViaInstant(temp));
                FinalStartTime.setValue(LocalTime.parse(stringObjectMap.get("finalStartTime").toString()));
                FinalEndTime.setValue(LocalTime.parse(stringObjectMap.get("finalEndTime").toString()));

                //After extracting the needed data delete event from database
                String qu = "DELETE FROM userData WHERE eventName=" + "'"+stringObjectMap.get("eventName").toString()+"'";
                DatabaseHandler.execAction(qu);
            }
        }
        tabPane.getSelectionModel().selectNext();
    }


    @FXML
    void initialize() {
        assert date != null : "fx:id=\"date\" was not injected: check your FXML file 'AddEvent.fxml'.";
        assert startTime != null : "fx:id=\"startTime\" was not injected: check your FXML file 'AddEvent.fxml'.";
        assert endTime != null : "fx:id=\"endTime\" was not injected: check your FXML file 'AddEvent.fxml'.";
        assert className != null : "fx:id=\"className\" was not injected: check your FXML file 'AddEvent.fxml'.";
        populateChoice();
    }

    // Find all user events
    void populateChoice(){
        ArrayList<String> dates = new ArrayList<String>();
        assert QU != null;
        for (Map<String, Object> stringObjectMap : QU) {
              dates.add(stringObjectMap.get("eventName").toString());
        }

        for(String q: dates)
        {
            eventMenu.getItems().add(q);
        }
    }

    //converting Date to LocalDate using instant
    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}

