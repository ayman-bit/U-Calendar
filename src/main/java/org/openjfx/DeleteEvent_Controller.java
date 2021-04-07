package org.openjfx;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mohammed Shahwan
 */

public class DeleteEvent_Controller {

    List<Map<String, Object>> events = DatabaseHandler.execQuery("SELECT * FROM userData");
    List<Map<String, Object>> subevents= DatabaseHandler.execQuery("SELECT * FROM subEvents");
    @FXML
    private ChoiceBox<String> eventMenu;

    @FXML
    private Pane drag;


    @FXML
    void Cancel(MouseEvent event) throws IOException {
        Controller.start("Application.fxml",event);
    }

    @FXML
    void Delete(MouseEvent event) throws IOException {
        if(events.size() == 0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("You must select and event to delete");
            alert.showAndWait();
        }
        for (Map<String, Object> stringObjectMap : events) {
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
                    alert.setContentText("This data is already deleted.");
                    alert.showAndWait();
                }

                qu = "DELETE FROM subEvents WHERE eventName=" + "'" + stringObjectMap.get("eventName").toString() + "'";
                DatabaseHandler.execAction(qu);
            }
        }
    }

    // Find all user events
    void populateChoice(){
        ArrayList<String> dates = new ArrayList<String>();
        assert events != null;
        for (Map<String, Object> stringObjectMap : events) {
            dates.add(stringObjectMap.get("eventName").toString());
        }

        for(String q: dates)
        {
            eventMenu.getItems().add(q);
        }
    }
    @FXML
    void initialize() {

        Controller.makeStageDragable(drag);
        populateChoice();
    }

}
