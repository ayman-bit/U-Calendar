package org.openjfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Mohammed Shahwan
 */

public class Tasks_Controller implements Initializable {


    @FXML
    private TableView<Events> table;

    @FXML
    private TableColumn<Events, Integer> IdCol;

    @FXML
    private TableColumn<Events, String> nameCol;

    @FXML
    private TableColumn<Events, String> dateCol;

    @FXML
    private TableColumn<Events, String> timeCol;



    @FXML
    void back(MouseEvent event) throws IOException {
    Controller.start("Application.fxml",event);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        IdCol.setCellValueFactory(new PropertyValueFactory<Events,Integer>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Events,String>("name"));
        dateCol.setCellValueFactory(new PropertyValueFactory<Events,String>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<Events,String>("time"));

        List<Map<String, Object>> QU = DatabaseHandler.execQuery("SELECT * FROM userData");


        ObservableList<Events> list = FXCollections.observableArrayList();


        for(int i = 0; i < QU.size(); i++)
        {
            Calendar c = Calendar.getInstance();
            // set the calendar to start of today
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            // and get that as a Date
            Date today = c.getTime();

            Map<String, Object> eventsFromDB = QU.get(i);
            String[] dates = eventsFromDB.get("date").toString().split("-");

            // let's say the components come from a form or something
            int year = Integer.parseInt(dates[0]);
            int month = Integer.parseInt(dates[1]);
            int dayOfMonth = Integer.parseInt(dates[2]);

            // reuse the calendar to set user specified date
            c.set(Calendar.YEAR, year);
            //added the negative 1 because all month was one ahead not sure why excatly
            c.set(Calendar.MONTH, month-1);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Date dateSpecified = c.getTime();

            System.out.println(today);
            System.out.println(dateSpecified);
            System.out.println(dates[0] + "," + dates[1] + "'" + dates[2]);

            //Check if events occurs after "today"
            if(dateSpecified.after(today)) {
                list.add(new Events((Integer) eventsFromDB.get("id"), eventsFromDB.get("eventName").toString(), eventsFromDB.get("date").toString(), eventsFromDB.get("startTime").toString()));
            }
        }
        table.setItems(list);
    }
}
