package org.openjfx;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
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

    ArrayList<HashMap<String, Object>> reoccurences;

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

        reoccurences = new ArrayList<>();
        loadReoccurences();

        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                if (calendar.get(Calendar.DAY_OF_WEEK)==(rows*i + j+1) && !found) {

                    Text Data = new Text(String.valueOf(calendar.get(Calendar.DATE)));
                    Data.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 11));
                    VBox vbox = new VBox(Data); // Add the first day of month to the view

                    addEventToBox(vbox, calendar); // Add its events to the vbox

                    mainPanel.add(vbox,j,i); // Add vbox to the view
                    found = true;
                }
                else if (found && !nextM) {

                    date++;
                    calendar.set(Calendar.DAY_OF_MONTH, date);
                    Text Data = new Text(String.valueOf(calendar.get(Calendar.DATE)));
                    Data.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 11));

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
                    Data.setFont(Font.font("system", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 11));
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

    private void loadReoccurences() {
        // TODO: finaldate will be v important here
        List<Map<String, Object>> classList = DatabaseHandler.execQuery(
                "SELECT eventName, reoccur, date, endDate, startTime FROM userData WHERE " +
                        "user_id = " + Login_Controller.uid);

        if(classList != null){
            // Cast map to hash map using deep copy
            for (Map<String, Object> c : classList) {
                HashMap<String, Object> m =  new HashMap<>(c);
                reoccurences.add(m);
            }
        }


    }

    private void addEventToBox(VBox vbox, Calendar calendar) {

        Date datee = calendar.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format1.format(datee);

        List<Map<String, Object>> todaysSubevents = DatabaseHandler.execQuery(
                "SELECT subeventName, subStartTime FROM subEvents WHERE " + //do i need to extract the date?
                        "user_id = " + Login_Controller.uid +
                        " AND subeventDate = '" + dateString + "' ");

        addReoccurToTodaysEvents(todaysSubevents, calendar);

        // Get events and their time from db and add them to lists for size>=3
        if (todaysSubevents.size()<=3){
            sortAndAddEvents(vbox, todaysSubevents, true);
        }

        // Get events and their time from db and add them to lists
        else if (todaysSubevents.size()>3){
            int diff = todaysSubevents.size()-3;
            sortAndAddEvents(vbox, todaysSubevents, false);
            TextField textField =  new TextField("and " + diff + " more");
            textField.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 11));
            textField.setEditable(false);
            vbox.getChildren().add(textField);
        }

    }

    private void addReoccurToTodaysEvents(List<Map<String, Object>> todaysSubevents, Calendar calendar) {
        for (Map<String, Object> r: reoccurences){

            // Generate today's date, start date, and end date
            String[] t = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()).split("-");
            LocalDate today = LocalDate.of(Integer.parseInt(t[0]), Integer.parseInt(t[1]), Integer.parseInt(t[2]));
            String[] startDateS = r.get("date").toString().split("-");
            LocalDate startDate = LocalDate.of(Integer.parseInt(startDateS[0]), Integer.parseInt(startDateS[1]),Integer.parseInt(startDateS[2]));
            String[] endDateS = r.get("endDate").toString().split("-");
            LocalDate endDate = LocalDate.of(Integer.parseInt(endDateS[0]), Integer.parseInt(endDateS[1]),Integer.parseInt(endDateS[2]));

            // If today's date falls within the start and end dates of this class
            if (today.compareTo(startDate) >= 0 && today.compareTo(endDate) <= 0){
                // Get the day of week
                String day = today.getDayOfWeek().name();
                char currentDayChar = day.charAt(0);
                if (day == "Thursday"){
                    currentDayChar = 'R';
                }

                // if this class has a reoccurence in this day of the week, add it to class list: create a map with keys eventName and startTime
                if (r.get("reoccur").toString().contains(String.valueOf(currentDayChar))){
                    Map<String, Object> m = new HashMap<>();
                    m.put("subeventName", r.get("eventName"));
                    m.put("subStartTime", r.get("startTime"));
                    todaysSubevents.add(m);
                }
            }

        }
    }

    private void sortAndAddEvents(VBox vbox, List<Map<String, Object>> todaysSubevents, boolean isLessThanThree){
        List<LocalTime> times =  new ArrayList<>();
        List<LocalTime> unsortedTimes =  new ArrayList<>();
        List<String> events =  new ArrayList<>();
        for (Map<String, Object> c : todaysSubevents) {

            String eventName = c.get("subeventName").toString(); //get event name and add it to list
            events.add(eventName);
            /*            System.out.println(c.getClass());*/

            String[] timeS = c.get("subStartTime").toString().split(":"); //get event time and add it to list
            Integer[] time = convertToIntArray(timeS);
            LocalTime t = LocalTime.of(time[0], time[1], 0);
            times.add(t);
        }

        // deep copy the current times array into a new array for use later
        for (LocalTime lt : times) {
            unsortedTimes.add(lt);
        }

        // sort the original array
        times.sort(LocalTime::compareTo);


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
                    textField.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 11));
                    // Another textField for times.get(y) //TODO to be done when implementing adding time to the month view
                    vbox.getChildren().add(textField);
                    //unsortedTimes.remove(y); // TODO: this is a bad mechanism for dealing with multiple events with same time ouputting only the first one
                    //times.remove(x);
                    break;
                }
            }
        }

    }

    private Integer[] convertToIntArray(String[] ymdS) {
        Integer ymd[] = new Integer[ymdS.length];
        for(int i=0; i<ymdS.length; i++){
            ymd[i] = Integer.parseInt(ymdS[i]);
        }
        return ymd;
    }

    private void print(List<LocalTime> times) {
        for (int i=0; i<times.size(); i++){
            System.out.println(times.get(i));
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
