package org.openjfx;

import com.jfoenix.controls.JFXDatePicker;
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
    void Cancel(MouseEvent event) throws IOException {
        Parent MainApp = FXMLLoader.load(getClass().getResource("Application.fxml"));
        Scene mainScene = new Scene(MainApp);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }

    @FXML
    void Done(ActionEvent event) {
        String startD= date.getValue().toString();
        String startT= startTime.getValue().toString();
        String endT= endTime.getValue().toString();
        String name= className.getText();
        System.out.println(Login_Controller.uname);

        if(startD.isEmpty()||startT.isEmpty()||endT.isEmpty()||name.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all infomartion");
            alert.showAndWait();
            return;
        }

        String qu = "INSERT INTO userData(eventName,date,startTime,endTime, user_id) VALUES ("
                + "'" + name + "',"
                + "'" + startD + "',"
                + "'" + startT + "',"
                + "'" + endT + "',"
                + "'" + Login_Controller.uid + "'"
                + ")";
        System.out.println(qu);

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
    }

    @FXML
    void initialize() {
        assert date != null : "fx:id=\"date\" was not injected: check your FXML file 'AddEvent.fxml'.";
        assert startTime != null : "fx:id=\"startTime\" was not injected: check your FXML file 'AddEvent.fxml'.";
        assert endTime != null : "fx:id=\"endTime\" was not injected: check your FXML file 'AddEvent.fxml'.";
        assert className != null : "fx:id=\"className\" was not injected: check your FXML file 'AddEvent.fxml'.";

    }
}