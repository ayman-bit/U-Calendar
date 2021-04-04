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
    private JFXTimePicker startTime;

    @FXML
    private JFXTimePicker endTime;

    @FXML
    private JFXTextField className;

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
        }
        else{
            FinalDate.setVisible(false);
            FinalStartTime.setVisible(false);
            FinalEndTime.setVisible(false);
            finalLabel.setVisible(false);
            finalStartLabel.setVisible(false);
            finalEndLabel.setVisible(false);
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
            String startD= date.getValue().toString();
            String startT= startTime.getValue().toString();
            String endT= endTime.getValue().toString();
            String name= className.getText();
            String recourrence = getReoccurence();
            String DateFinal= FinalDate.getValue().toString();
            String startFT= FinalStartTime.getValue().toString();
            String endFT= FinalEndTime.getValue().toString();

            if(startD.isEmpty()||startT.isEmpty()||endT.isEmpty()||name.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Some required information was not entered");
                alert.showAndWait();
                return;
            }

            String qu = "INSERT INTO userData(eventName,date,startTime,endTime,reoccur,finalDate,finalStartTime,finalEndTime,user_id) VALUES ("
                    + "'" + name + "',"
                    + "'" + startD + "',"
                    + "'" + startT + "',"
                    + "'" + endT + "',"
                    + "'" + recourrence + "',"
                    + "'" + DateFinal + "',"
                    + "'" + startFT + "',"
                    + "'" + endFT + "',"
                    + "'" + Login_Controller.uid + "'"
                    + ")";
            System.out.println(qu);

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
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all infomartion");
            alert.showAndWait();
            return;
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


    }
}