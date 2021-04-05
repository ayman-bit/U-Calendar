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

        Month_Year.setText(new SimpleDateFormat("MMMM, YYYY").format(calendar.getTime()));

        int rows = 24;
        int cols = 7;

        mainPanel.getChildren().clear();
        // Add it to the grid
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                /*Label Data = new Label(String.valueOf(i));*/
                AnchorPane a = new AnchorPane();
                a.prefWidth(97);
                a.prefHeight(31.25);
                Rectangle r = new Rectangle();
                r.setWidth(85);
                r.setHeight(25);
                r.setArcWidth(20);
                r.setArcHeight(20);
                r.setLayoutY(5);
                r.setFill(Color.BLUEVIOLET);
                a.getChildren().add(r);

                Label label = new Label("cell: " + i + ", " + j);
                label.setLayoutY(8);
                label.setLayoutX(2);
                label.setFont(Font.font("system", FontWeight.BOLD, FontPosture.REGULAR, 11));
                a.getChildren().add(label);

                // Add it to the grid
                mainPanel.add(a,j,i);
                mainPanel.setAlignment(Pos.TOP_RIGHT);
                Calendar current = Calendar.getInstance();

                if ((current.get(Calendar.DAY_OF_WEEK)) == (j + 1) && current.get(Calendar.HOUR_OF_DAY) == i && currentWeek==current.get(Calendar.WEEK_OF_YEAR)) {
                    //eventName.setTextFill(Color.RED);
                }

            }
        }
    }
}
