package org.openjfx;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mohammed Shahwan
 */

public class DeleteEvent_Controller {

    List<Map<String, Object>> QU = DatabaseHandler.execQuery("SELECT * FROM userData");

    @FXML
    private ChoiceBox<String> eventMenu;


    @FXML
    void Cancel(MouseEvent event) throws IOException {
        Controller.start("Application.fxml",event);
    }

    @FXML
    void Delete(MouseEvent event) throws IOException {
        for (Map<String, Object> stringObjectMap : QU) {
            if (stringObjectMap.get("eventName").toString().equals(eventMenu.getValue())) {
                String qu = "DELETE FROM userData WHERE eventName=" + "'" + stringObjectMap.get("eventName").toString() + "'";
                if (DatabaseHandler.execAction(qu)) { //Success
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Success");
                    alert.showAndWait();
                    Controller.start("Application.fxml", event);
                } else { // Error
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("This Data Already Deleted");
                    alert.showAndWait();
                }
            }
        }
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
    @FXML
    void initialize() {
        populateChoice();
    }

}
