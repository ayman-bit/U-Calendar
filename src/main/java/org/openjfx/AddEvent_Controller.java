package org.openjfx;

import com.jfoenix.controls.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Ayman Abu Awad
 * @edited Mohammed Shahwan created the Done() function.
 */

public class  AddEvent_Controller {

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
    private Label finalLabel,finalStartLabel, finalEndLabel;

    @FXML
    private JFXRadioButton monday,tuesday, wednesday, thursday, friday;

    @FXML
    private AnchorPane drag;

    @FXML
    private AnchorPane labPane, testPane, assignPane;

    @FXML
    private JFXColorPicker eventColour,finalColour;

    @FXML
    void Next(MouseEvent e) throws IOException{
        tabPane.getSelectionModel().selectNext();
    }

    @FXML
    void hasLabs(MouseEvent e) throws IOException{
        if(haveLabs.isSelected())
        {
            labPane.setVisible(true);
        }
        else{
            labPane.setVisible(false);
        }
    }

    @FXML
    void hasAssigns(MouseEvent e) throws IOException{
        if(haveAssigns.isSelected())
        {
            assignPane.setVisible(true);
        }
        else{
            assignPane.setVisible(false);
        }
    }

    @FXML
    void hasTests(MouseEvent e) throws IOException{
        if(haveTests.isSelected())
        {
            testPane.setVisible(true);
        }
        else{
            testPane.setVisible(false);
        }
    }

    @FXML
    void hasFinal(MouseEvent e) throws IOException{
        if(haveFinal.isSelected())
        {
            FinalDate.setVisible(true);
            FinalStartTime.setVisible(true);
            FinalEndTime.setVisible(true);
            finalLabel.setVisible(true);
            finalStartLabel.setVisible(true);
            finalEndLabel.setVisible(true);
            finalWeight.setVisible(true);
            finalColour.setVisible(true);
        }
        else{
            FinalDate.setVisible(false);
            FinalStartTime.setVisible(false);
            FinalEndTime.setVisible(false);
            finalLabel.setVisible(false);
            finalStartLabel.setVisible(false);
            finalEndLabel.setVisible(false);
            finalWeight.setVisible(false);
            finalColour.setVisible(false);
        }
    }

    @FXML
    void Back(MouseEvent e) throws IOException{
        tabPane.getSelectionModel().selectPrevious();
    }

    @FXML
    void Cancel(MouseEvent event) throws IOException {
        Controller.start("Application.fxml" ,event);
    }

    @FXML
    void Done(MouseEvent event) throws IOException {
        try {
            if(date.getValue() == null||endDate.getValue() == null||startTime.getValue() == null
                || className.getText().isEmpty() || endTime.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Some required information was not entered");
                alert.showAndWait();
            }
            else if(date.getValue() != null||endDate.getValue() != null||startTime.getValue() != null
                    || className.getText() != null || endTime.getValue() != null){

                if(endTime.getValue().isBefore(startTime.getValue()) || startTime.getValue().isAfter(endTime.getValue())){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Time input incorrect");
                    alert.showAndWait();
                }
                else if(endDate.getValue().isBefore(date.getValue()) || date.getValue().isAfter(endDate.getValue())){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Date input incorrect");
                    alert.showAndWait();
                }
                else{
                    String reoccurrence = getReoccurence();

                    String qu = "INSERT INTO userData(eventName,date,endDate,startTime,endTime,reoccur,eventColour,user_id) VALUES ("
                            + "'" + className.getText() + "',"
                            + "'" + date.getValue().toString() + "',"
                            + "'" + endDate.getValue().toString() + "',"
                            + "'" + startTime.getValue().toString() + "',"
                            + "'" + endTime.getValue().toString() + "',"
                            + "'" + eventColour.getValue().toString() + "',"
                            + "'" + reoccurrence + "',"
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
            else if(haveFinal.isSelected() && (FinalDate.getValue()!= null||FinalStartTime.getValue()!= null||FinalEndTime.getValue() != null)
                    &&(date.getValue() != null||endDate.getValue() != null||startTime.getValue() != null
                    || className.getText() !=null || endTime.getValue() != null)){
                if(FinalEndTime.getValue().isBefore(FinalStartTime.getValue()) || FinalStartTime.getValue().isAfter(FinalEndTime.getValue())){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Time input incorrect");
                    alert.showAndWait();
                }
                else{
                    String subEventName= "Final";
                    String reoccurrence = getReoccurence();

                    String qu = "INSERT INTO subEvents(eventName,subeventName,subeventWeight,subeventDate,subStartTime,subEndTime,eventColour,user_id) VALUES ("
                            + "'" + className.getText() + "',"
                            + "'" + subEventName + "',"
                            + "'" + finalWeight.getText() + "',"
                            + "'" + FinalDate.getValue().toString() + "',"
                            + "'" + FinalStartTime.getValue().toString() + "',"
                            + "'" + FinalEndTime.getValue().toString() + "',"
                            + "'" + finalColour.getValue().toString() + "',"
                            + "'" + Login_Controller.uid + "'"
                            + ")";

                    DatabaseHandler.execAction(qu);

                    qu = "INSERT INTO userData(eventName,date,endDate,startTime,endTime,reoccur,eventColour,eventColour,user_id) VALUES ("
                            + "'" + className.getText() + "',"
                            + "'" + date.getValue().toString() + "',"
                            + "'" + endDate.getValue().toString() + "',"
                            + "'" + startTime.getValue().toString() + "',"
                            + "'" + endTime.getValue().toString() + "',"
                            + "'" + eventColour.getValue().toString() + "',"
                            + "'" + reoccurrence + "',"
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
        }
        catch (Exception e){
            System.out.println(e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all infomartion");
            alert.showAndWait();
        }

    }

    String getReoccurence(){
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

    @FXML
    void initialize() throws IOException {
        Controller.makeStageDragable(drag);

        assert date != null : "fx:id=\"date\" was not injected: check your FXML file 'AddEvent.fxml'.";
        assert startTime != null : "fx:id=\"startTime\" was not injected: check your FXML file 'AddEvent.fxml'.";
        assert endTime != null : "fx:id=\"endTime\" was not injected: check your FXML file 'AddEvent.fxml'.";
        assert className != null : "fx:id=\"className\" was not injected: check your FXML file 'AddEvent.fxml'.";

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
        Pane aPane = FXMLLoader.load(getClass().getResource("AddAssigns.fxml"));
        assignPane.getChildren().add(aPane);
        aPane = FXMLLoader.load(getClass().getResource("AddLabs.fxml"));
        labPane.getChildren().add(aPane);
        aPane = FXMLLoader.load(getClass().getResource("AddTests.fxml"));
        testPane.getChildren().add(aPane);
    }
}