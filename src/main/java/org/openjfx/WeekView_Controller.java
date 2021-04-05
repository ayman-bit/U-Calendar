package org.openjfx;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
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
    Rectangle rectangle;

    @FXML
    Label eventName = new Label("s");

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

    int currentWeek;
    @FXML
    private Label Month_Year;
    String today;

    @FXML
    void CreateGrid () throws IOException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, currentWeek);

        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(Calendar.WEEK_OF_YEAR, 2);
        String secondDayOnWeek2 = new SimpleDateFormat("dd").format(tempCalendar.getTime()); // Why does this return the day on Monday?
        int daysInWeek1 = Integer.parseInt(secondDayOnWeek2) - 2;
        System.out.println(secondDayOnWeek2);

        tempCalendar.set(Calendar.DAY_OF_YEAR, (currentWeek-2)*7 + daysInWeek1 + 1);
        String beginDate = new SimpleDateFormat("MMMM-dd").format(tempCalendar.getTime());
        tempCalendar.set(Calendar.DAY_OF_YEAR, (currentWeek-2)*7 + daysInWeek1 + 7);
        String endDate = new SimpleDateFormat("MMMM-dd").format(tempCalendar.getTime());



        //Month_Year.setText("Week: " + new SimpleDateFormat("WW, MMMM, YYYY").format(calendar.getTime()));
        Month_Year.setText(beginDate + " to " + endDate);

        int rows = 24;
        int cols = 7;

        mainPanel.getChildren().clear();
        // Add it to the grid
        // We might need to switch the two loops
        for (int i = 0; i < cols; i++){
            calendar.set(Calendar.DAY_OF_YEAR, (currentWeek-2)*7 + daysInWeek1 + i + 1);
            String todaysDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

            // Find all events that occur on this day
            List<Map<String, Object>> todaysSubevents = DatabaseHandler.execQuery(
                    "SELECT subeventName, subStartTime FROM subEvents WHERE " + //TODO: subEndTime
                            "user_id = " + Login_Controller.uid +
                            " AND subeventDate = '" + todaysDate + "'");

            System.out.println("Size: " + todaysSubevents.size());

            for (int j = 0; j < rows; j++){
                int currentHour = j;


                //ArrayList<HashMap<String, Object>> eventOfThisHour = new ArrayList<>(); //Make it an arraylist to allow multiple events per hour
                HashMap<String, Object> eventOfThisHour = new HashMap<>();
                String subeventName = null;

                // Find the subevent that occurs in this hour
                for (Map<String, Object> subevent: todaysSubevents){
                    if(subevent.get("subStartTime") != null){
                        System.out.println("Not null");
                        String[] timeOfEvent = subevent.get("subStartTime").toString().split(":");
                        int hourOfEvent = Integer.parseInt(timeOfEvent[0]);
                        if(hourOfEvent == currentHour){
                            subeventName = subevent.get("subeventName").toString();
                            break;
                        }
                    }
                    else{
                        System.out.println("Null");
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
}
