package org.openjfx;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class MonthView_Controller {
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
        previous.add(Calendar.MONTH, -1);
        int prevEnd = previous.getActualMaximum(Calendar.DAY_OF_MONTH);

        int prev = prevEnd - calendar.get(Calendar.DAY_OF_WEEK)+2;


        int rows = 6;
        int cols = 7;
        boolean found = false;

        mainPanel.getChildren().clear();
        // Add it to the grid
          for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                if (calendar.get(Calendar.DAY_OF_WEEK)==(rows*i + j+1) && !found) {
//                    System.out.println(j);
                    // Add VBox and style it
                    Text Data = new Text(String.valueOf(calendar.get(Calendar.DATE)));
                    // Add it to the grid
                    mainPanel.add(Data,j,i );
                    found = true;
                } else if (found) {
                    date++;
                    calendar.set(Calendar.DAY_OF_MONTH, date);
                    Text Data = new Text(String.valueOf(calendar.get(Calendar.DATE)));
                    // Add it to the grid
                    mainPanel.add(Data, j, i);
                } else {
                    previous.set(Calendar.DAY_OF_MONTH, prev);
                    Text prevData = new Text(String.valueOf(previous.get(Calendar.DATE)));
                    // Add it to the grid
                    mainPanel.add(prevData, j, i);
                    prev++;
                }

                if (date == end) {
                    date = 0;
                } if (today.equals(new SimpleDateFormat("ddMMYYYY").format(calendar.getTime()))) {

                    Text Data = new Text(String.valueOf(calendar.get(Calendar.DATE)));
                    Data.setFill(Color.RED);

                    // Add it to the grid
                    mainPanel.add(Data, j, i);


                }
            }
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
