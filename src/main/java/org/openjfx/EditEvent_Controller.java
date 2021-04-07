package org.openjfx;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EditEvent_Controller {

    List<Map<String, Object>> events = DatabaseHandler.execQuery("SELECT * FROM userData");
    List<Map<String, Object>> subevents = DatabaseHandler.execQuery("SELECT * FROM subEvents");

    @FXML
    private JFXDatePicker date,endDate,FinalDate;

    @FXML
    private JFXTimePicker startTime,endTime,FinalStartTime, FinalEndTime,labStartTime,labEndTime;

    @FXML
    private JFXTextField className,finalWeight;

    @FXML
    private JFXTabPane tabPane;

    @FXML
    private CheckBox haveAssigns,haveLabs,haveFinal;

    @FXML
    private Label finalLabel,finalStartLabel, finalEndLabel,colorLabel;

    @FXML
    private JFXTextField labNumber,labWeight;

    @FXML
    private JFXDatePicker labDate;

    @FXML
    private JFXRadioButton monday,tuesday, wednesday, thursday, friday;

    @FXML
    private AnchorPane drag;

    @FXML
    private AnchorPane labPane, testPane, assignPane;

    @FXML
    private ChoiceBox<String> eventMenu,assignMenu,labMenu,testMenu;

    @FXML
    private JFXColorPicker eventColour;

    @FXML
    void Back(MouseEvent event) {
        tabPane.getSelectionModel().selectPrevious();
    }

    @FXML
    void Cancel(MouseEvent event) throws IOException {
        Controller.start("Application.fxml",event);
    }

    @FXML
    void Done(MouseEvent event) throws IOException {
        if(!checkClassInfo()){
            String reoccurrence = getReoccurence();

            String qu = "INSERT INTO userData(eventName,date,endDate,startTime,endTime,reoccur,eventColour,user_id) VALUES ("
                    + "'" + className.getText() + "',"
                    + "'" + date.getValue().toString() + "',"
                    + "'" + endDate.getValue().toString() + "',"
                    + "'" + startTime.getValue().toString() + "',"
                    + "'" + endTime.getValue().toString() + "',"
                    + "'" + reoccurrence + "',"
                    + "'" + eventColour.getValue().toString() + "',"
                    + "'" + Login_Controller.uid + "'"
                    + ")";

            if(DatabaseHandler.execAction(qu)){ //Success
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Success");
                alert.showAndWait();
                Controller.start("Application.fxml", event);
            }
            else{ // Error
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("This Data Already Exists");
                alert.showAndWait();
            }
        }
    }


    boolean checkClassInfo() {
        boolean a = false;
        if (date.getValue() == null || endDate.getValue() == null || startTime.getValue() == null
                || className.getText().isEmpty() || endTime.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Some required information was not entered");
            alert.showAndWait();
            a = true;
            return a;
        } else{
            if (endTime.getValue().isBefore(startTime.getValue()) || startTime.getValue().isAfter(endTime.getValue())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Time input incorrect");
                alert.showAndWait();
                a = true;
                return a;
            } else if (endDate.getValue().isBefore(date.getValue()) || date.getValue().isAfter(endDate.getValue())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Date input incorrect");
                alert.showAndWait();
                a = true;
                return a;
            }
        }
        return a;
    }

    @FXML
    void Next(MouseEvent event) {
        tabPane.getSelectionModel().selectNext();
    }

    @FXML
    void NextPopulate(MouseEvent event) throws ParseException {
        DateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String color;
        assert events != null;
        assert subevents != null;
        for (Map<String, Object> stringObjectMap : events) {
            if(stringObjectMap.get("eventName").toString().equals(eventMenu.getValue())) {
                className.setText(stringObjectMap.get("eventName").toString());
                Date temp = simpleDateFormat.parse(stringObjectMap.get("date").toString());
                date.setValue(convertToLocalDateViaInstant(temp));
                startTime.setValue(LocalTime.parse(stringObjectMap.get("startTime").toString()));
                endTime.setValue(LocalTime.parse(stringObjectMap.get("endTime").toString()));
                temp = simpleDateFormat.parse(stringObjectMap.get("endDate").toString());
                endDate.setValue(convertToLocalDateViaInstant(temp));
                color = stringObjectMap.get("eventColour").toString();
                Color c = Color.web(color);
                eventColour.setValue(c);

                //After extracting the needed data delete event from database
                String qu = "DELETE FROM userData WHERE eventName=" + "'"+stringObjectMap.get("eventName").toString()+"'";
                DatabaseHandler.execAction(qu);
            }
        }
        tabPane.getSelectionModel().selectNext();
    }


    //converting Date to LocalDate using instant
    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private String getReoccurence(){
        String re = "";
        if (monday.isSelected()){
            re +="M";
        }
        if (tuesday.isSelected()){
            re +="T";
        }
        if (wednesday.isSelected()){
            re +="W";
        }
        if(thursday.isSelected()){
            re+="R";
        }
        if (friday.isSelected()){
            re +="F";
        }
        return re;
    }

    void populateChoice(){
        ArrayList<String> dates = new ArrayList<String>();
        if ( events == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("No events found please add event first");
            alert.showAndWait();
        }
        for (Map<String, Object> stringObjectMap : events) {
            dates.add(stringObjectMap.get("eventName").toString());
        }

        for(String q: dates)
        {
            eventMenu.getItems().add(q);
        }
    }

    @FXML
    void initialize() throws IOException {
        Controller.makeStageDragable(drag);
        populateChoice();
    }


    void loadClasses(){
        List<Map<String, Object>> classList = DatabaseHandler.execQuery("SELECT subeventName FROM subEvents WHERE eventName = " + className.getText());
        assert classList != null;
        if(classList.size() == 0)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("No assignment events found please add event first");
            alert.showAndWait();
        }
        else{
            for (Map<String, Object> i : classList) {
                for (Map.Entry<String, Object> me : i.entrySet()) {
                    assignMenu.getItems().add(me.getValue().toString());
                }
            }
        }
    }
}