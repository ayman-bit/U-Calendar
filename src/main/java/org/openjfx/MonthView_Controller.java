package org.openjfx;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.*;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

/**
 * @author Ayman Abu Awad
 * Kamal Ali (edited create grid loop to change font size of numbers)
 */

public class MonthView_Controller extends DatabaseHandler {
    int currentMonth;

    String today;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private GridPane mainPanel;

    @FXML
    private Label Month_Year;


    @FXML
    void CreateGrid (){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, currentMonth);

        Month_Year.setText(new SimpleDateFormat("MMMM, YYYY").format(calendar.getTime()));

        int date = 1;
        calendar.set(Calendar.DAY_OF_MONTH, date);
        int end = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        Calendar previous = Calendar.getInstance();
        previous.set(Calendar.MONTH, currentMonth-1);
        int prevEnd = previous.getActualMaximum(Calendar.DAY_OF_MONTH);

        int prev = prevEnd - calendar.get(Calendar.DAY_OF_WEEK)+2;

        Calendar nextMonth = Calendar.getInstance();
        nextMonth.set(Calendar.MONTH, currentMonth+1);

        int rows = 6;
        int cols = 7;
        boolean found = false;
        boolean nextM = false;

        mainPanel.getChildren().clear();

          for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                if (calendar.get(Calendar.DAY_OF_WEEK)==(rows*i + j+1) && !found) {

                    Text Data = new Text(String.valueOf(calendar.get(Calendar.DATE)));
                    Data.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 11));
                    VBox vbox = new VBox(Data); // Add today's date to the view

                    addEventToBox(vbox, calendar); // Add today's event to the view

                    mainPanel.add(vbox,j,i); // Add it to the grid
                    found = true;
                }
                else if (found && !nextM) {

                    date++;
                    calendar.set(Calendar.DAY_OF_MONTH, date);
                    Text Data = new Text(String.valueOf(calendar.get(Calendar.DATE)));
                    Data.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 10.5));

                    if (today.equals(new SimpleDateFormat("ddMMYYYY").format(calendar.getTime()))) {
                        Data.setFill(Color.RED);
                    }

                    VBox vbox = new VBox(Data);

                    addEventToBox(vbox, calendar);

                    mainPanel.add(vbox,j,i );
                }
                else if (nextM){

                    date++;
                    nextMonth.set(Calendar.DAY_OF_MONTH, date);
                    Text Data = new Text(String.valueOf(nextMonth.get(Calendar.DATE)));
                    Data.setFont(Font.font("system", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 10.5));
                    VBox vbox = new VBox(Data);

                    addEventToBox(vbox, nextMonth);

                    mainPanel.add(vbox,j,i);
                }
                else {

                    previous.set(Calendar.DAY_OF_MONTH, prev);
                    Text prevData = new Text(String.valueOf(previous.get(Calendar.DATE)));
                    prevData.setFont(Font.font("system", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 11));
                    VBox vbox = new VBox(prevData);

                    addEventToBox(vbox, previous);

                    mainPanel.add(vbox, j, i);
                    prev++;
                }
                if (date == end) {
                    date = 0;
                    nextM = true;
                }
            }
        }
    }

    private void addEventToBox(VBox vbox, Calendar calendar) {
        Date datee = calendar.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format1.format(datee);

        // Extract the events for this day from the database
        List<Map<String, Object>> classList = DatabaseHandler.execQuery(
                "SELECT eventName FROM userData WHERE " +
                        "user_id = " + Login_Controller.uid +
                        " AND date = '" + dateString + "'");
        if (classList.size()<=3){
            for (Map<String, Object> c : classList) {
                for (Map.Entry<String, Object> me : c.entrySet()) {


                    TextField textField =  new TextField(me.getValue().toString());
                    textField.setEditable(false);

                    /*Label label = new Label(me.getValue().toString());
                    label.getStyleClass().add("event");
                    label.setMaxWidth(100);
                    VBox.setVgrow(label, Priority.ALWAYS);*/
                    vbox.getChildren().add(textField);
                }
            }
        }

        if (classList.size()>3){
            int diff = classList.size()-3;
            int i=0;
            // TODO: sort
            for (Map<String, Object> c : classList) {
                for (Map.Entry<String, Object> me : c.entrySet()) {
                    TextField textField =  new TextField(me.getValue().toString());
                    textField.setEditable(false);
                    vbox.getChildren().add(textField);
                    i++;
                    System.out.println(i);
                }
                if (i==3){
                    break;
                }
            }
            TextField textField =  new TextField("and " + diff + " more");
            textField.setEditable(false);
            vbox.getChildren().add(textField);
        }
    }

    @FXML
    private JFXTextField CurrentMonth;

    @FXML
    void Today(MouseEvent event) {
        Calendar calendar = Calendar.getInstance();
        currentMonth = calendar.get(Calendar.MONTH);
        CreateGrid ();
    }

    @FXML
    void backwardButton(MouseEvent event) {
        currentMonth--;
        CreateGrid ();
    }

    @FXML
    void forwardButton(MouseEvent event) {
        currentMonth++;
        CreateGrid ();

    }


    @FXML
    void initialize() {
        assert mainPanel != null : "fx:id=\"Grid\" was not injected: check your FXML file 'YearView.fxml'.";
        assert CurrentMonth != null : "fx:id=\"CurrentMonth\" was not injected: check your FXML file 'YearView.fxml'.";
        Calendar calendar = Calendar.getInstance();
        currentMonth = calendar.get(Calendar.MONTH);
        today = new SimpleDateFormat("ddMMYYYY").format(calendar.getTime());
        CreateGrid ();
    }
}
