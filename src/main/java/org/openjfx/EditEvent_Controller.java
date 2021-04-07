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
    private JFXTimePicker startTime,endTime,FinalStartTime, FinalEndTime;

    @FXML
    private JFXTextField className,finalWeight;

    @FXML
    private JFXTabPane tabPane;

    @FXML
    private CheckBox haveAssigns,haveTests,haveLabs,haveFinal;

    @FXML
    private Label finalLabel,finalStartLabel, finalEndLabel,colorLabel;

    @FXML
    private JFXRadioButton monday,tuesday, wednesday, thursday, friday;

    @FXML
    private AnchorPane drag;

    @FXML
    private AnchorPane labPane, testPane, assignPane;

    @FXML
    private ChoiceBox<String> eventMenu;

    @FXML
    private JFXColorPicker eventColour,finalColour;


    @FXML
    void Back(MouseEvent event) {
        tabPane.getSelectionModel().selectPrevious();
    }

    @FXML
    void Cancel(MouseEvent event) throws IOException {
        Controller.start("Application.fxml",event);
    }

    @FXML
    void Done(MouseEvent event) {

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

    @FXML
    void hasAssigns(MouseEvent event) throws IOException {
        assignPane.setVisible(haveAssigns.isSelected());
        Pane aPane = FXMLLoader.load(getClass().getResource("AddAssigns.fxml"));
        assignPane.getChildren().add(aPane);
    }

    @FXML
    void hasFinal(MouseEvent event) {
        FinalDate.setVisible(haveFinal.isSelected());
        FinalStartTime.setVisible(haveFinal.isSelected());
        FinalEndTime.setVisible(haveFinal.isSelected());
        finalLabel.setVisible(haveFinal.isSelected());
        finalStartLabel.setVisible(haveFinal.isSelected());
        finalEndLabel.setVisible(haveFinal.isSelected());
        finalWeight.setVisible(haveFinal.isSelected());
        finalColour.setVisible(haveFinal.isSelected());
        colorLabel.setVisible(haveFinal.isSelected());
    }

    @FXML
    void hasLabs(MouseEvent event) throws IOException {
        Pane aPane = FXMLLoader.load(getClass().getResource("AddLabs.fxml"));
        labPane.getChildren().add(aPane);
    }

    @FXML
    void hasTests(MouseEvent event) throws IOException {
        Pane aPane = FXMLLoader.load(getClass().getResource("AddTests.fxml"));
        testPane.getChildren().add(aPane);
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
        assignPane.setVisible(false);
        labPane.setVisible(false);
        testPane.setVisible(false);
        FinalDate.setVisible(false);
        FinalStartTime.setVisible(false);
        FinalEndTime.setVisible(false);
        finalLabel.setVisible(false);
        finalStartLabel.setVisible(false);
        finalEndLabel.setVisible(false);
        finalWeight.setVisible(false);
        finalColour.setVisible(false);
        colorLabel.setVisible(false);
    }
}