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
import javafx.scene.paint.Color;
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
    private JFXColorPicker eventColour;

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
        if(eventColour.getValue() == null){
            Color c = Color.web("0x039be5ff");
            eventColour.setValue((Color)c);
        }

        if(haveFinal.isSelected() && !checkFinalInfo() && !checkClassInfo()){
            String subEventName= "Final";
            String qu = "INSERT INTO subEvents(eventName,subeventName,subeventWeight,subeventDate,subStartTime,subEndTime,eventColour,user_id) VALUES ("
                    + "'" + className.getText() + "',"
                    + "'" + subEventName + "',"
                    + "'" + finalWeight.getText() + "',"
                    + "'" + FinalDate.getValue().toString() + "',"
                    + "'" + FinalStartTime.getValue().toString() + "',"
                    + "'" + FinalEndTime.getValue().toString() + "',"
                    + "'" + eventColour.getValue().toString() + "',"
                    + "'" + Login_Controller.uid + "'"
                    + ")";

            if(!DatabaseHandler.execAction(qu)){ //Success
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("This Data Already Exists");
                alert.showAndWait();
            }
        }
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

    private String getReoccurence() {
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


    private boolean checkFinalInfo(){
        boolean a = false;
        if(FinalEndTime.getValue() == null || FinalStartTime.getValue() == null || finalWeight.getText() ==null )
        {
            a=true;
            return a;
        }
        if(FinalEndTime.getValue().isBefore(FinalStartTime.getValue()) || FinalStartTime.getValue().isAfter(FinalEndTime.getValue())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Time input for final incorrect");
            alert.showAndWait();
            a=true;
        }
        if(finalWeight.getText() != null && !(finalWeight.getText().matches("[1-9]?\\d")) ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Final weight must be between 0-99");
            alert.showAndWait();
            a=true;
        }
        return a;
    }

    private boolean checkAssignInfo(){
        boolean a = false;
        if(assignWeight.getText() == null || assignNumber.getText() == null || assignWeight.getText() == null || assignDate.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Required information was not entered");
            alert.showAndWait();
            a=true;
        }
        else if(assignWeight.getText() != null && !(assignWeight.getText().matches("[1-9]?\\d")) ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Assignment weight must be between 0-99");
            alert.showAndWait();
            a=true;
        }
        return a;
    }

    private boolean checkLabInfo(){
        boolean a = false;
        if(labNumber.getText() == null || labWeight.getText() == null || labDate.getValue() == null  ||labStartTime.getValue()== null  || labEndTime== null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Required information was not entered");
            alert.showAndWait();
            a=true;

        }
        if(labEndTime.getValue().isBefore(labStartTime.getValue()) || labStartTime.getValue().isAfter(labEndTime.getValue())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Time input for Lab incorrect");
            alert.showAndWait();
            a=true;
        }
        else if(labWeight.getText() != null && !(labWeight.getText().matches("[1-9]?\\d")) ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Lab weight must be between 0-99");
            alert.showAndWait();
            a=true;
        }
        return a;
    }
    private boolean checkTestInfo(){
        boolean a = false;
        if(testNumber.getText() == null || testWeight.getText() == null || testDate.getValue() == null  ||testStartTime.getValue()== null  || testEndTime== null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Required information was not entered");
            alert.showAndWait();
            a=true;

        }
        if(testEndTime.getValue().isBefore(testStartTime.getValue()) || testStartTime.getValue().isAfter(testEndTime.getValue())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Time input for test incorrect");
            alert.showAndWait();
            a=true;
        }
        else if(testWeight.getText() != null && !(testWeight.getText().matches("[1-9]?\\d")) ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Lab weight must be between 0-99");
            alert.showAndWait();
            a=true;
        }
        return a;
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
    private void AddAssign(MouseEvent event)throws IOException {
        if(eventColour.getValue() == null){
            Color c = Color.web("0x039be5ff");
            eventColour.setValue((Color)c);
        }
        else if(haveAssigns.isSelected() && !checkAssignInfo())
        {
            String qu = "INSERT INTO subEvents(eventName,subeventName,subeventWeight,subeventDate,eventColour,user_id) VALUES ("
                    + "'" + eventLabelAssign.getText() + "',"
                    + "'" + assignNumber.getText() + "',"
                    + "'" + assignWeight.getText() + "',"
                    + "'" + assignDate.getValue().toString() + "',"
                    + "'" + eventColour.getValue().toString() + "',"
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
        eventLabelAssign.setText(className.getText());
        if(eventColour.getValue() == null){
            Color c = Color.web("0x039be5ff");
            eventColour.setValue((Color)c);
        }
    }

    @FXML
    void AddLab(MouseEvent event)throws IOException {
        if(eventColour.getValue() == null){
            Color c = Color.web("0x039be5ff");
            eventColour.setValue((Color)c);
        }
        //checking for for two numbers using regex
        if(haveLabs.isSelected() && !checkLabInfo())
        {
            String qu = "INSERT INTO subEvents(eventName,subeventName,subeventWeight,subeventDate,subStartTime,subEndTime,eventColour,user_id) VALUES ("
                    + "'" + eventLabelLab.getText() + "',"
                    + "'" + labNumber.getText() + "',"
                    + "'" + labWeight.getText() + "',"
                    + "'" + labDate.getValue().toString() + "',"
                    + "'" + labStartTime.getValue().toString() + "',"
                    + "'" + labEndTime.getValue().toString() + "',"
                    + "'" + eventColour.getValue().toString() + "',"
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

    void clearLabSelection(){
        labNumber.setText("");
        labWeight.setText("");
        labDate.getEditor().clear();
        labDate.setValue(null);
        labStartTime.setValue(null);
        labEndTime.setValue(null);
        eventLabelLab.setText(className.getText());
        if(eventColour.getValue() == null){
            Color c = Color.web("0x039be5ff");
            eventColour.setValue((Color)c);
        }
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
        eventLabelTest.setText(className.getText());
        Color c = Color.web("0x039be5ff");
        eventColour.setValue((Color)c);
    }
    @FXML
    void AddTest(MouseEvent event)throws IOException {
        if(eventColour.getValue() == null){
            Color c = Color.web("0x039be5ff");
            eventColour.setValue((Color)c);
        }
        if(haveTests.isSelected() && !checkTestInfo()) {
            String qu = "INSERT INTO subEvents(eventName,subeventName,subeventWeight,subeventDate,subStartTime,subEndTime,eventColour,user_id) VALUES ("
                    + "'" + this.className.getText() + "',"
                    + "'" + testNumber.getText() + "',"
                    + "'" + testWeight.getText() + "',"
                    + "'" + testDate.getValue().toString() + "',"
                    + "'" + testStartTime.getValue().toString() + "',"
                    + "'" + testEndTime.getValue().toString() + "',"
                    + "'" + eventColour.getValue().toString() + "',"
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