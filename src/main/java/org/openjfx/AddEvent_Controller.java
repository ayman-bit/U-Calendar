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
import javafx.fxml.Initializable;
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

public class AddEvent_Controller implements Initializable {

    @FXML
    private JFXDatePicker date,endDate,FinalDate;

    @FXML
    private JFXTimePicker startTime,endTime,FinalStartTime, FinalEndTime;

    @FXML
    public JFXTextField className;
    @FXML
    private JFXTextField finalWeight;
    @FXML
    private JFXTabPane tabPane;

    @FXML
    private JFXTextField labNumber,labWeight;

    @FXML
    private JFXDatePicker labDate;

    @FXML
    private JFXTimePicker labStartTime,labEndTime;

    @FXML
    private JFXColorPicker labColour,testColour,eventColour,finalColour,assignColour;

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
    private JFXTextField assignNumber,assignWeight;

    @FXML
    private JFXDatePicker assignDate;

    @FXML
    private JFXTextField testNumber,testWeight;

    @FXML
    private JFXDatePicker testDate;

    @FXML
    private JFXTimePicker testStartTime, testEndTime;


    @FXML
    private Label eventLabelAssign,eventLabelLab,eventLabelTest;

    @FXML
    void Next(MouseEvent e){
        tabPane.getSelectionModel().selectNext();
    }

    @FXML
    void hasLabs(MouseEvent e) throws IOException {
        labPane.setVisible(haveLabs.isSelected());
        eventLabelLab.setText(this.className.getText());

    }

    @FXML
    void hasAssigns(MouseEvent e){
        assignPane.setVisible(haveAssigns.isSelected());
        eventLabelAssign.setText(this.className.getText());

    }

    @FXML
    void hasTests(MouseEvent e){
        testPane.setVisible(haveTests.isSelected());
        eventLabelTest.setText(this.className.getText());
    }

    @FXML
    void hasFinal(MouseEvent e){
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
    private void AddAssign(MouseEvent event)throws IOException {
        if(assignDate.getValue() == null||assignNumber.getText() == null||assignWeight.getText()==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Required information was not entered");
            alert.showAndWait();
        }
        else{
            String qu = "INSERT INTO subEvents(eventName,subeventName,subeventWeight,subeventDate,eventColour,user_id) VALUES ("
                    + "'" + eventLabelAssign.getText() + "',"
                    + "'" + assignNumber.getText() + "',"
                    + "'" + assignWeight.getText() + "',"
                    + "'" + assignDate.getValue().toString() + "',"
                    + "'" + assignColour.getValue().toString() + "',"
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
            clearAssignSelection();
        }
    }

    void clearAssignSelection(){
        assignNumber.setText("");
        assignWeight.setText("");
        assignNumber.setPromptText("Assignment Name");
        assignWeight.setPromptText("Assignment Weight");
        assignDate.getEditor().clear();
        assignDate.setValue(null);
        assignColour.setValue(null);
        eventLabelAssign.setText(className.getText());
    }

    @FXML
    void AddLab(MouseEvent event)throws IOException {
        if(labDate.getValue() == null||labNumber.getText() == null||labWeight.getText()==null || labStartTime.getValue()==null || labEndTime.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Some information was not entered");
            alert.showAndWait();
        }
        else{
            if(labEndTime.getValue().isBefore(labStartTime.getValue())|| labStartTime.getValue().isAfter(labEndTime.getValue())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Time input incorrect");
                alert.showAndWait();
            }
            else{
                String qu = "INSERT INTO subEvents(eventName,subeventName,subeventWeight,subeventDate,subStartTime,subEndTime,eventColour,user_id) VALUES ("
                        + "'" + eventLabelLab.getText() + "',"
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
                clearLabSelection();
            }
        }
    }

    void clearLabSelection(){
        labNumber.setText("");
        labWeight.setText("");
        labDate.getEditor().clear();
        labDate.setValue(null);
        labStartTime.setValue(null);
        labEndTime.setValue(null);
        labColour.setValue(null);
        eventLabelLab.setText(className.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Controller.makeStageDragable(drag);
        FinalDate.setVisible(false);
        FinalStartTime.setVisible(false);
        FinalEndTime.setVisible(false);
        finalLabel.setVisible(false);
        finalStartLabel.setVisible(false);
        finalEndLabel.setVisible(false);
        finalWeight.setVisible(false);
        finalColour.setVisible(false);
        colorLabel.setVisible(false);
        labPane.setVisible(false);
        assignPane.setVisible(false);
        testPane.setVisible(false);
        eventLabelLab.setText(this.className.getText());
        eventLabelTest.setText(this.className.getText());
        eventLabelAssign.setText(this.className.getText());
    }

    void clearTestSelection(){
        testNumber.setText("");
        testWeight.setText("");
        testDate.getEditor().clear();
        testDate.setValue(null);
        testStartTime.setValue(null);
        testEndTime.setValue(null);
        testColour.setValue(null);
        eventLabelTest.setText(className.getText());
    }
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
                clearTestSelection();
            }
        }

    }

}