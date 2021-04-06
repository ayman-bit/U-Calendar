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
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Ayman Abu Awad
 * @edited Mohammed Shahwan created the Done() function.
 */

public class  AddEvent_Controller {

    @FXML
    private JFXDatePicker date;

    @FXML
    private JFXDatePicker endDate;

    @FXML
    private JFXTimePicker startTime;

    @FXML
    private JFXTimePicker endTime;

    @FXML
    private JFXTextField className;

    @FXML
    private JFXTextField finalWeight;

    @FXML
    private JFXTabPane tabPane;

    @FXML
    private JFXDatePicker FinalDate;

    @FXML
    private JFXTimePicker FinalStartTime;

    @FXML
    private JFXTimePicker FinalEndTime;

    @FXML
    private CheckBox haveAssigns;

    @FXML
    private JFXButton addAssignBtn;

    @FXML
    private CheckBox haveTests;

    @FXML
    private JFXButton addTestBtn;

    @FXML
    private CheckBox haveLabs;

    @FXML
    private JFXButton addLabsBtn;

    @FXML
    private CheckBox haveFinal;

    @FXML
    private Label finalLabel;

    @FXML
    private Label finalStartLabel;

    @FXML
    private Label finalEndLabel;

    @FXML
    private JFXRadioButton monday;

    @FXML
    private JFXRadioButton tuesday;

    @FXML
    private JFXRadioButton wednesday;

    @FXML
    private JFXRadioButton thursday;

    @FXML
    private JFXRadioButton friday;

    @FXML
    private AnchorPane drag;



    @FXML
    void Next(MouseEvent e) throws IOException{
        tabPane.getSelectionModel().selectNext();
    }

    @FXML
    void hasLabs(MouseEvent e) throws IOException{
        if(haveLabs.isSelected())
        {
            addLabsBtn.setVisible(true);
        }
        else{
            addLabsBtn.setVisible(false);
        }

    }

    @FXML
    void hasAssigns(MouseEvent e) throws IOException{
        if(haveAssigns.isSelected())
        {
            addAssignBtn.setVisible(true);
        }
        else{
            addAssignBtn.setVisible(false);
        }
    }

    @FXML
    void hasTests(MouseEvent e) throws IOException{
        if(haveTests.isSelected())
        {
            addTestBtn.setVisible(true);
        }
        else{
            addTestBtn.setVisible(false);
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
        }
        else{
            FinalDate.setVisible(false);
            FinalStartTime.setVisible(false);
            FinalEndTime.setVisible(false);
            finalLabel.setVisible(false);
            finalStartLabel.setVisible(false);
            finalEndLabel.setVisible(false);
            finalWeight.setVisible(false);
        }


    }


    @FXML
    void AddAssign(MouseEvent e) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddAssigns.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        AddAssigns_Controller assignController = fxmlLoader.getController();
        assignController.setClassName(className.getText());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Add Assignments");
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void AddLabs(MouseEvent e) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddLabs.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        AddLabs_Controller labController = fxmlLoader.getController();
        labController.setClassName(className.getText());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void AddTests(MouseEvent e) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("AddTests.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        AddTests_Controller testController = fxmlLoader.getController();
        testController.setClassName(className.getText());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Add Tests");
        stage.setScene(scene);
        stage.show();
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

                if(endTime.getValue().isBefore(startTime.getValue())){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("End time must be after start time");
                    alert.showAndWait();
                }
                else if(endDate.getValue().isBefore(date.getValue())){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("End date must be after start date");
                    alert.showAndWait();
                }
                else{
                    String reoccurrence = getReoccurence();

                    String qu = "INSERT INTO userData(eventName,date,endDate,startTime,endTime,reoccur,user_id) VALUES ("
                            + "'" + className.getText() + "',"
                            + "'" + date.getValue().toString() + "',"
                            + "'" + endDate.getValue().toString() + "',"
                            + "'" + startTime.getValue().toString() + "',"
                            + "'" + endTime.getValue().toString() + "',"
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
                if(FinalEndTime.getValue().isBefore(FinalStartTime.getValue())){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("End time must be after start time for final");
                    alert.showAndWait();
                }
                else{
                    String subEventName= "Final";
                    String reoccurrence = getReoccurence();

                    String qu = "INSERT INTO subEvents(eventName,subeventName,subeventWeight,subeventDate,subStartTime,subEndTime,user_id) VALUES ("
                            + "'" + className.getText() + "',"
                            + "'" + subEventName + "',"
                            + "'" + finalWeight.getText() + "',"
                            + "'" + FinalDate.getValue().toString() + "',"
                            + "'" + FinalStartTime.getValue().toString() + "',"
                            + "'" + FinalEndTime.getValue().toString() + "',"
                            + "'" + Login_Controller.uid + "'"
                            + ")";

                    DatabaseHandler.execAction(qu);

                    qu = "INSERT INTO userData(eventName,date,endDate,startTime,endTime,reoccur,user_id) VALUES ("
                            + "'" + className.getText() + "',"
                            + "'" + date.getValue().toString() + "',"
                            + "'" + endDate.getValue().toString() + "',"
                            + "'" + startTime.getValue().toString() + "',"
                            + "'" + endTime.getValue().toString() + "',"
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
            else{
                //not sure what brings here 
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
    void initialize() {
        Controller.makeStageDragable(drag);

        assert date != null : "fx:id=\"date\" was not injected: check your FXML file 'AddEvent.fxml'.";
        assert startTime != null : "fx:id=\"startTime\" was not injected: check your FXML file 'AddEvent.fxml'.";
        assert endTime != null : "fx:id=\"endTime\" was not injected: check your FXML file 'AddEvent.fxml'.";
        assert className != null : "fx:id=\"className\" was not injected: check your FXML file 'AddEvent.fxml'.";


        addAssignBtn.setVisible(false);
        addLabsBtn.setVisible(false);
        addTestBtn.setVisible(false);
        FinalDate.setVisible(false);
        FinalStartTime.setVisible(false);
        FinalEndTime.setVisible(false);
        finalLabel.setVisible(false);
        finalStartLabel.setVisible(false);
        finalEndLabel.setVisible(false);
        finalWeight.setVisible(false);
    }
}