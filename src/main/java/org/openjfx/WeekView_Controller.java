package org.openjfx;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
/**
 * @author Ayman Abu Awad
 */

public class WeekView_Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private GridPane hoursPanel;

    @FXML
    private GridPane mainPanel;

    @FXML
    void Today(MouseEvent event) {
        Calendar calendar = Calendar.getInstance();
        currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        CreateGrid ();
    }

    @FXML
    void backwardButton(MouseEvent event) {
        currentWeek--;
        CreateGrid ();
    }

    @FXML
    void forwardButton(MouseEvent event) {
        currentWeek++;
        CreateGrid ();

    }

    @FXML
    void initialize() {
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
    void CreateGrid (){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, currentWeek);

        Month_Year.setText(new SimpleDateFormat("MMMM, YYYY").format(calendar.getTime()));

        int rows = 24;
        int cols = 7;

        mainPanel.getChildren().clear();
        // Add it to the grid
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                Label Data = new Label(String.valueOf(i));
                // Add it to the grid
                mainPanel.add(Data,j,i );
                mainPanel.setAlignment(Pos.TOP_RIGHT);
                Calendar current = Calendar.getInstance();

                if ((current.get(Calendar.DAY_OF_WEEK)) == (j + 1) && current.get(Calendar.HOUR_OF_DAY) == i && currentWeek==current.get(Calendar.WEEK_OF_YEAR)) {
                    Data.setTextFill(Color.RED);
                }

            }
        }
    }
}
