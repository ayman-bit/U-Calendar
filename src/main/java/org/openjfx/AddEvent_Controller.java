package org.openjfx;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
        import com.jfoenix.controls.JFXTimePicker;
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
        import javafx.scene.input.MouseEvent;
        import javafx.stage.Stage;

/**
 * @author Ayman Abu Awad
 */

public class AddEvent_Controller {

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
    private JFXTextField numAssign;

    @FXML
    private JFXTextField numLabs;

    @FXML
    private JFXTextField numTests;

    @FXML
    private JFXDatePicker FinalDate;

    @FXML
    private JFXTimePicker FinalStartTime;

    @FXML
    private JFXTimePicker FinalEndTime;

    @FXML
    void Next(MouseEvent e) throws IOException{
        tabPane.getSelectionModel().selectNext();
    }

    @FXML
    void Back(MouseEvent e) throws IOException{
        tabPane.getSelectionModel().selectPrevious();
    }

    @FXML
    void Cancel(MouseEvent event) throws IOException {
        Parent MainApp = FXMLLoader.load(getClass().getResource("Application.fxml"));
        Scene mainScene = new Scene(MainApp);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("UCalendar");
        window.setScene(mainScene);
        window.show();
    }

    @FXML
    void Done(ActionEvent event) throws IOException {
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

            System.out.println(Login_Controller.uname);

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
            System.out.println(qu);

            if(DatabaseHandler.execAction(qu)){ //Success
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Success");
                alert.showAndWait();
                Parent MainApp1 = FXMLLoader.load(getClass().getResource("Application.fxml"));
                Scene mainScene = new Scene(MainApp1);
                Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
                window.setTitle("U-Calendar");
                window.setScene(mainScene);
                window.show();
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
    void initialize() {
        assert date != null : "fx:id=\"date\" was not injected: check your FXML file 'AddEvent.fxml'.";
        assert startTime != null : "fx:id=\"startTime\" was not injected: check your FXML file 'AddEvent.fxml'.";
        assert endTime != null : "fx:id=\"endTime\" was not injected: check your FXML file 'AddEvent.fxml'.";
        assert className != null : "fx:id=\"className\" was not injected: check your FXML file 'AddEvent.fxml'.";

    }
}