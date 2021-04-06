package org.openjfx;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
        today = new SimpleDateFormat("ddMMYYYY").format(calendar.getTime());
        CreateGrid ();
    }

    @FXML
    void CreateGrid () throws IOException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, currentWeek);

        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(Calendar.WEEK_OF_YEAR, 2);
        String secondDayOnWeek2 = new SimpleDateFormat("dd").format(tempCalendar.getTime()); // Why does this return the day on Monday?
        int daysInWeek1 = Integer.parseInt(secondDayOnWeek2) - 2;

        tempCalendar.set(Calendar.DAY_OF_YEAR, (currentWeek-2)*7 + daysInWeek1);
        String beginDate = new SimpleDateFormat("MMMM-dd").format(tempCalendar.getTime());
        tempCalendar.set(Calendar.DAY_OF_YEAR, (currentWeek-2)*7 + daysInWeek1 + 6);
        String endDate = new SimpleDateFormat("MMMM-dd").format(tempCalendar.getTime());


        //Month_Year.setText("Week: " + new SimpleDateFormat("WW, MMMM, YYYY").format(calendar.getTime()));
        Month_Year.setText(beginDate + " to " + endDate);

        int rows = 24;
        int cols = 7;

        mainPanel.getChildren().clear();

        reoccurences = new ArrayList<>();
        loadReoccurences();

        for (int i = 0; i < cols; i++){
            calendar.set(Calendar.DAY_OF_YEAR, (currentWeek-2)*7 + daysInWeek1 + i);
            String todaysDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

            // Find all events that occur on this day
            List<Map<String, Object>> todaysSubevents = DatabaseHandler.execQuery(
                    "SELECT subeventName, subStartTime FROM subEvents WHERE " + //TODO: subEndTime
                            "user_id = " + Login_Controller.uid +
                            " AND subeventDate = '" + todaysDate + "'");

            for (int j = 0; j < rows; j++){
                int currentHour = j;


                //ArrayList<HashMap<String, Object>> eventOfThisHour = new ArrayList<>(); //Make it an arraylist to allow multiple events per hour
                HashMap<String, Object> eventOfThisHour = new HashMap<>();
                String subeventName = null;

                // Find if a subevent occurs in this hour
                for (Map<String, Object> subevent: todaysSubevents){
                    if(subevent.get("subStartTime") != null){
                        String[] timeOfEvent = subevent.get("subStartTime").toString().split(":");
                        int hourOfEvent = Integer.parseInt(timeOfEvent[0]);
                        if(hourOfEvent == currentHour){
                            subeventName = subevent.get("subeventName").toString();
                            break;
                        }
                    }
                    else{
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
                        if (day == "Thursday"){
                            currentDayChar = 'R';
                        }

                        // if this class has a reoccurence in this time of the day, add it to class list: create a map with keys eventName and startTime
                        if (r.get("reoccur").toString().contains(String.valueOf(currentDayChar))){
                            if(r.get("startTime") != null){
                                String[] timeOfEvent = r.get("startTime").toString().split(":");
                                int hourOfEvent = Integer.parseInt(timeOfEvent[0]);
                                if(hourOfEvent == currentHour){
                                    subeventName = r.get("eventName").toString();
                                    break;
                                }
                                else {
                                    System.out.println("there is reoccurence today, but time not match. Event: " + r.get("eventName") +
                                            ". Current hour: " + currentHour);
                                }
                            }
                            else{
                                System.out.println("there is reoccurence today, but time is null. Event: " + r.get("eventName") +
                                        ". Current hour: " + currentHour);
                            }
                        }
                    }

                }


                //Find the ones that occur this hour
              /*  for (Map<String, Object> subevent: todaysSubevents){
                    String[] timeOfEvent = subevent.get("subStartTime").toString().split(":");
                    int hourOfEvent = Integer.parseInt(timeOfEvent[0]);
                    if(hourOfEvent == currentHour){
                        HashMap<String, Object> m = new HashMap<>();
                        m.put("subeventName", subevent.get("subeventName"));
                        eventOfThisHour.add(m);
                        break; // Only because we assume one event per time block
                    }
                }*/

                // If a subevent that starts in this hour exists, add it to the display
                if(subeventName != null){
                    AnchorPane a = setUpEventBox(Color.BLUEVIOLET,subeventName);

                    // Add it to the grid
                    mainPanel.add(a,i,j);
                    mainPanel.setAlignment(Pos.TOP_RIGHT);
                }

                Calendar current = Calendar.getInstance();
                if ((current.get(Calendar.DAY_OF_WEEK)) == (j + 1) && current.get(Calendar.HOUR_OF_DAY) == i && currentWeek==current.get(Calendar.WEEK_OF_YEAR)) {
                    //eventName.setTextFill(Color.RED);
                }

            }
        }
    }

    private AnchorPane setUpEventBox(Color color, String eventOfThisHour) {
        AnchorPane a = new AnchorPane();
        a.prefWidth(97);
        a.prefHeight(31.25);
        Rectangle r = new Rectangle();
        r.setWidth(85); r.setHeight(25);
        r.setArcWidth(20); r.setArcHeight(20);
        r.setLayoutY(5);
        r.setFill(color);
        a.getChildren().add(r);

        Label label = new Label(eventOfThisHour);
        label.setLayoutY(8);
        label.setLayoutX(2);
        label.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 11));
        a.getChildren().add(label);
        return a;
    }

    private void loadReoccurences() {
        List<Map<String, Object>> classList = DatabaseHandler.execQuery(
                "SELECT eventName, reoccur, date, endDate, startTime FROM userData WHERE " +
                        "user_id = " + Login_Controller.uid);

        if (classList != null) {
            // Cast map to hash map using deep copy
            for (Map<String, Object> c : classList) {
                HashMap<String, Object> m = new HashMap<>(c);
                reoccurences.add(m);
            }
        }
    }

    private void addReoccurToTodaysEvents(List<Map<String, Object>> todaysSubevents, Calendar calendar) {
       /* for (Map<String, Object> r: reoccurences){

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

        }*/
    }

}

