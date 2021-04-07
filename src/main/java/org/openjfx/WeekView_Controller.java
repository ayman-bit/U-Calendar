package org.openjfx;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
/**
 * @author Ayman Abu Awad
 * Kamal Ali (added display events functionalities)
 */

public class WeekView_Controller extends DatabaseHandler {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private GridPane hoursPanel;

    @FXML
    private GridPane mainPanel;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Label Month_Year;

    int currentWeek;
    String today;
    ArrayList<HashMap<String, Object>> reoccurences;


    @FXML
    void Today(MouseEvent event) throws IOException {
        Calendar calendar = Calendar.getInstance();
        currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        CreateGrid ();
    }

    @FXML
    void backwardButton(MouseEvent event) throws IOException {
        currentWeek--;
        CreateGrid ();
    }

    @FXML
    void forwardButton(MouseEvent event) throws IOException {
        currentWeek++;
        CreateGrid ();

    }

    @FXML
    void initialize() throws IOException {
        assert hoursPanel != null : "fx:id=\"hoursPanel\" was not injected: check your FXML file 'WeekView.fxml'.";
        assert mainPanel != null : "fx:id=\"Grid\" was not injected: check your FXML file 'WeekView.fxml'.";
        Calendar calendar = Calendar.getInstance();
        currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        //today = new SimpleDateFormat("ddMMYYYY").format(calendar.getTime());
        CreateGrid ();
    }

    @FXML
    void CreateGrid () throws IOException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, currentWeek);

        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(Calendar.WEEK_OF_YEAR, 2);
        String secondDayOnWeek2 = new SimpleDateFormat("dd").format(tempCalendar.getTime());
        int daysInWeek1 = Integer.parseInt(secondDayOnWeek2) - 2; // This helps determine the dates of the current week

        tempCalendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_WEEK) + 1);
        String beginDate = new SimpleDateFormat("MMMM-dd").format(tempCalendar.getTime()); // Get the date of first day of week
        tempCalendar.add(Calendar.DAY_OF_YEAR, 6);
        String endDate = new SimpleDateFormat("MMMM-dd").format(tempCalendar.getTime()); // Get the date of last day of week


        Month_Year.setText(beginDate + " to " + endDate);

        int rows = 24;
        int cols = 7;

        mainPanel.getChildren().clear();

        reoccurences = new ArrayList<>();
        loadReoccurences();

        for (int i = 0; i < cols; i++){
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_WEEK) + i + 1);
            String todaysDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

            // Find all events that occur on this day
            List<Map<String, Object>> todaysSubevents = DatabaseHandler.execQuery(
                    "SELECT subeventName, subStartTime, subEndTime, eventName FROM subEvents WHERE " +
                            "user_id = " + Login_Controller.uid +
                            " AND subeventDate = '" + todaysDate + "'");

            for (int j = 0; j < rows; j++){
                int currentHour = j;

                // Find if a subevent occurs in this hour
                for (Map<String, Object> subevent: todaysSubevents){
                    if(subevent.get("subStartTime") != null){
                        String[] startTimeOfEvent = subevent.get("subStartTime").toString().split(":");
                        int startHourOfEvent = Integer.parseInt(startTimeOfEvent[0]);
                        int startMinuteOfEvent = Integer.parseInt(startTimeOfEvent[1]);
                        startMinuteOfEvent = roundMinutes(startMinuteOfEvent);
                        LocalTime startTime = LocalTime.of(startHourOfEvent, startMinuteOfEvent);

                        String[] endTimeOfEvent = subevent.get("subEndTime").toString().split(":");
                        int endHourOfEvent = Integer.parseInt(endTimeOfEvent[0]);
                        int endMinuteOfEvent = Integer.parseInt(endTimeOfEvent[1]);
                        endHourOfEvent = roundEndHour(endHourOfEvent,endMinuteOfEvent);
                        endMinuteOfEvent = roundMinutes(endMinuteOfEvent);
                        LocalTime endTime = LocalTime.of(endHourOfEvent, endMinuteOfEvent);

                        double durationInMinutes = Duration.between(startTime, endTime).toMinutes();

                        String subeventName = subevent.get("subeventName").toString();

                        // To get color we need eventName
                        String eventName = subevent.get("eventName").toString();

                        List<Map<String, Object>> eventOfSubevent = DatabaseHandler.execQuery(
                                "SELECT eventColour FROM userData WHERE " +
                                        "user_id = " + Login_Controller.uid +
                                        " AND eventName = '" + eventName + "'");

                        String eventColour = "0x8a2be2"; //dummy value
                        for (Map<String, Object> r : eventOfSubevent){
                            eventColour = r.get("eventColour").toString();
                            break;
                        }

                        Color color =  Color.valueOf(eventColour);

                        if(startHourOfEvent == currentHour){
                            addToDisplay(subeventName, durationInMinutes, startMinuteOfEvent, i, j, color);
                        }
                    }
                }

                // Find if an event (or a reoccurence) occurs in this hour
                for (Map<String, Object> r: reoccurences){

                    // Generate today's date, start date, and end date
                    String[] t = todaysDate.split("-");
                    LocalDate today = LocalDate.of(Integer.parseInt(t[0]), Integer.parseInt(t[1]), Integer.parseInt(t[2]));
                    String[] startDateS = r.get("date").toString().split("-");
                    LocalDate startDate = LocalDate.of(Integer.parseInt(startDateS[0]), Integer.parseInt(startDateS[1]),Integer.parseInt(startDateS[2]));
                    String[] endDateS = r.get("endDate").toString().split("-");
                    LocalDate endDatee = LocalDate.of(Integer.parseInt(endDateS[0]), Integer.parseInt(endDateS[1]),Integer.parseInt(endDateS[2]));

                    // If today's date falls within the start and end dates of this class
                    if (today.compareTo(startDate) >= 0 && today.compareTo(endDatee) <= 0){
                        // Get the day of week
                        String day = today.getDayOfWeek().name();
                        char currentDayChar = day.charAt(0);
                        if (day.contains("THURSDAY")){
                            currentDayChar = 'R';
                        }

                        // if this class has a reoccurence in this time of the day, add it to class list: create a map with keys eventName and startTime
                        if (r.get("reoccur").toString().contains(String.valueOf(currentDayChar))){
                           if(r.get("startTime") != null){
                                String[] startTimeOfEvent = r.get("startTime").toString().split(":");
                                int startHourOfEvent = Integer.parseInt(startTimeOfEvent[0]);
                                int startMinuteOfEvent = Integer.parseInt(startTimeOfEvent[1]);
                                startMinuteOfEvent = roundMinutes(startMinuteOfEvent);
                                LocalTime startTime = LocalTime.of(startHourOfEvent, startMinuteOfEvent);

                                String[] endTimeOfEvent = r.get("endTime").toString().split(":");
                                int endHourOfEvent = Integer.parseInt(endTimeOfEvent[0]);
                                int endMinuteOfEvent = Integer.parseInt(endTimeOfEvent[1]);
                                endHourOfEvent = roundEndHour(endHourOfEvent, endMinuteOfEvent);
                                endMinuteOfEvent = roundMinutes(endMinuteOfEvent);
                                LocalTime endTime = LocalTime.of(endHourOfEvent, endMinuteOfEvent);

                               double durationInMinutes = Duration.between(startTime, endTime).toMinutes();

                               String subeventName = r.get("eventName").toString();

                               String eventColour = r.get("eventColour").toString();
                               Color color =  Color.valueOf(eventColour);

                                if(startHourOfEvent == currentHour){
                                    addToDisplay(subeventName, durationInMinutes, startMinuteOfEvent, i, j, color);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // TODO: event should move to next day if endhour goes from 23 to 24
    private int roundEndHour(int endHourOfEvent, int endMinuteOfEvent) {
        if (endMinuteOfEvent>=53 && endMinuteOfEvent<=59){
            return endHourOfEvent+1;
        }
        else{
            return endHourOfEvent;
        }
    }

    private int roundMinutes(int minuteOfEvent) {
        if(minuteOfEvent >= 0 && minuteOfEvent <= 7){
            minuteOfEvent = 0;
        }
        else if(minuteOfEvent >= 8 && minuteOfEvent <= 22){
            minuteOfEvent = 15;
        }
        else if(minuteOfEvent >= 23 && minuteOfEvent <= 37){
            minuteOfEvent = 30;
        }
        else if(minuteOfEvent >= 38 && minuteOfEvent <= 52){
            minuteOfEvent = 45;
        }
        else{
            minuteOfEvent = 0;
        }
        return minuteOfEvent;
    }


    private AnchorPane setUpEventBox(Color color, String eventOfThisHour, double durationInMinutes, double  startMinuteOfEvent) {
        if(durationInMinutes == 0){
            durationInMinutes = 15;
        }
        double ratio = durationInMinutes/60;

        AnchorPane a = new AnchorPane();
        a.prefWidth(97);
        a.prefHeight(31.25 * (ratio+1));
        Rectangle r = new Rectangle();
        r.setHeight(31.25 * ratio);
        r.setWidth(88); r.setArcWidth(10); r.setArcHeight(10);
        r.setLayoutY(startMinuteOfEvent/60 * 31.25);
        r.setLayoutX(5);
        r.setFill(color);
        a.getChildren().add(r);

        Label label = new Label(eventOfThisHour);
        label.setLayoutX(8);
        label.setLayoutY(startMinuteOfEvent/60 * 31.25);
        if (durationInMinutes != 15){

        }
        label.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 11));
        a.getChildren().add(label);
        return a;
    }

    private void loadReoccurences() {
        List<Map<String, Object>> classList = DatabaseHandler.execQuery(
                "SELECT eventName, reoccur, date, endDate, startTime, endTime, eventColour FROM userData WHERE " +
                        "user_id = " + Login_Controller.uid);

        if (classList != null) {
            // deep copy map to hash map using
            for (Map<String, Object> c : classList) {
                HashMap<String, Object> m = new HashMap<>(c);
                reoccurences.add(m);
            }
        }
    }

    private void addToDisplay(String eventName, double duration, double startMinuteOfEvent, int i, int j, Color color){

        AnchorPane a = setUpEventBox(color, eventName, duration, startMinuteOfEvent);

        int durationInt = (int) duration + 1;
        int colSpan = 1;
        mainPanel.add(a,i,j, colSpan, durationInt); // Add it to the grid
        mainPanel.setAlignment(Pos.TOP_RIGHT);
    }

}

