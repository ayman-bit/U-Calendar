package org.openjfx;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

        Month_Year.setText(new SimpleDateFormat("MMMM, YYYY", Locale.US ).format(calendar.getTime())); // Locale.US ensures language is english
        //Month_Year.setText(new SimpleDateFormat("MMMM, YYYY", new Locale("ar") ).format(calendar.getTime()));
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
                "SELECT eventName, startTime FROM userData WHERE " +
                        "user_id = " + Login_Controller.uid +
                        " AND date = '" + dateString + "'");

        // Get events and their time from db and add them to lists for size>=3
        if (classList.size()<=3){
            sortAndAddEvents(vbox, classList, true);
        }

        // Get events and their time from db and add them to lists
        else if (classList.size()>3){
            int diff = classList.size()-3;
            sortAndAddEvents(vbox, classList, false);
            TextField textField =  new TextField("and " + diff + " more");
            textField.setEditable(false);
            vbox.getChildren().add(textField);
        }

    }

    private void sortAndAddEvents(VBox vbox, List<Map<String, Object>> classList, boolean isLessThanThree){
        List<LocalTime> times =  new ArrayList<>();
        List<LocalTime> unsortedTimes =  new ArrayList<>();
        List<String> events =  new ArrayList<>();
        for (Map<String, Object> c : classList) {

            String eventName = c.get("eventName").toString(); //get event name and add it to list
            events.add(eventName);

            String[] timeS = c.get("startTime").toString().split(":"); //get event time and add it to list
            Integer[] time = convertToIntArray(timeS);
            LocalTime t = LocalTime.of(time[0], time[1], 0);
            times.add(t);
        }

        // copy the current times array into a new array for use later
        for (LocalTime lt : times) {
            unsortedTimes.add(lt);
        }

        // sort the original array
        times.sort(LocalTime::compareTo);


        LocalTime lt;
        int size = 3;
        if(isLessThanThree){
            size = times.size();
        }
        // Add events to vbox according to their time chronologically
        for (int x = 0; x<size; x++) {
            for (int y = 0; y< times.size(); y++) {
                if(times.get(x) == unsortedTimes.get(y)) {
                    TextField textField =  new TextField(events.get(y));
                    textField.setEditable(false);
                    // Another textField for times.get(y)
                    vbox.getChildren().add(textField);
                    //unsortedTimes.remove(y);
                    //times.remove(x);
                    break;
                }
            }
        }

    }

    private void print(List<LocalTime> times) {
        for (int i=0; i<times.size(); i++){
            System.out.println(times.get(i));
        }
    }

    private Integer[] convertToIntArray(String[] ymdS) {
        Integer ymd[] = new Integer[ymdS.length];
        for(int i=0; i<ymdS.length; i++){
            ymd[i] = Integer.parseInt(ymdS[i]);
        }
        return ymd;
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
