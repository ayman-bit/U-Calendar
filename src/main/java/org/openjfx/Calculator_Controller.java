package org.openjfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.openjfx.tableEntry;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

 /**
* @author: Kamal Ali
 */

public class Calculator_Controller implements Initializable {

    @FXML
    private TableView<tableEntry> table;

    @FXML
    private ChoiceBox<String> choices;

    @FXML
    private TableColumn<tableEntry, String> subeventColumn;

    @FXML
    private TableColumn<tableEntry, Double> gradeColumn, outOfColumn, weightColumn, achievedColumn;

    @FXML
    private TextField subeventTextField, gradeTextField, outOfTextField, weightTextField, desiredTextField;

    @FXML
    private Label totalLabel;

    private String currentEvent;



    @Override
    public void initialize (URL url, ResourceBundle resourceBundle){

        loadClasses(); // loads the classes to the choice box

        // Listen for changes in selection and update currentEvent
        choices.getSelectionModel().selectedItemProperty().addListener(
                (v, oldValue, newValue) -> {
                                             currentEvent = newValue;
                                             table.setItems(getEntries());
                                             calculateTotal();}
                );

        subeventColumn.setCellValueFactory(
                new PropertyValueFactory<tableEntry,String>("subevent")
        );
        gradeColumn.setCellValueFactory(
                new PropertyValueFactory<tableEntry,Double>("grade")
        );
        outOfColumn.setCellValueFactory(
                new PropertyValueFactory<tableEntry,Double>("outOf")
        );
        weightColumn.setCellValueFactory(
                new PropertyValueFactory<tableEntry,Double>("weight")
        );

        //TODO: check this works
        achievedColumn.setCellValueFactory(
                new PropertyValueFactory<tableEntry,Double>("achieved")
        );

        // Not sure if necessary
        table.setEditable(true);
    }

    void loadClasses(){

        List<Map<String, Object>> classList = DatabaseHandler.execQuery("SELECT eventName FROM userData WHERE user_id = " + Login_Controller.uid);
        for (Map<String, Object> i : classList) {
            for (Map.Entry<String, Object> me : i.entrySet()) {
                choices.getItems().add(me.getValue().toString());
            }
        }

    }

    public ObservableList<tableEntry> getEntries() {
        ObservableList<tableEntry> entries = FXCollections.observableArrayList();

        // Load the table in a variable
        List<Map<String, Object>> grades = DatabaseHandler.execQuery("SELECT * FROM grades WHERE (user_id = " + Login_Controller.uid
                                                                    + " AND eventName = '" + currentEvent + "')");

        // For each row
        for(Map<String, Object> row : grades){
            // reading the row from table and converting to string
            String subevent = row.get("subevent").toString();
            String grade = row.get("grade").toString();
            String outOf = row.get("outOf").toString();
            String weight = row.get("weight").toString();

            entries.add(new tableEntry(subevent,
                        Double.parseDouble(grade),
                        Double.parseDouble(outOf),
                        Double.parseDouble(weight)));
        }

        return entries;
    }

    @FXML
    void addToTableClicked(ActionEvent event) {

        // Extract the inputs from the TextFields and perform casting if necessary
        String subevent = subeventTextField.getText();
        double grade = Double.parseDouble(gradeTextField.getText());
        double outOf = Double.parseDouble(outOfTextField.getText());
        double weight = Double.parseDouble(weightTextField.getText());

        String qu = "INSERT INTO grades(eventName,subevent,grade,outOf,weight,user_id) VALUES ("
                + "'" + currentEvent + "',"
                + "'" + subevent + "',"
                + "'" + grade + "',"
                + "'" + outOf + "',"
                + "'" + weight + "',"
                + "'" + Login_Controller.uid + "'"
                + ")";
        if (currentEvent != null){
            boolean q = DatabaseHandler.execAction(qu);
            if(!q){ //Success
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Error");
                alert.showAndWait();
            }
        }

        // Update the displayed entries
        table.setItems(getEntries());

        // Update the total
        calculateTotal();

        clearTextBoxes();
    }

    public void calculateTotal(){
        double total, totalPossible, lost, percentage;
        total=totalPossible=percentage=0;

        for (tableEntry row : table.getItems()) {
            total += achievedColumn.getCellObservableValue(row).getValue(); // reads the values in achievedColumn
            totalPossible += weightColumn.getCellObservableValue(row).getValue();
        }
        lost = totalPossible - total;
        if(totalPossible!=0){percentage = total/totalPossible * 100;}

        String totalValueLabel = "Achieved " + total + " out of a possible " + totalPossible
                                  + " points\nand lost " + lost + " points. Current percentage: "
                                  + percentage + "%";
        totalLabel.setText(totalValueLabel); // Set the value of the label to the total
    }

    @FXML
    void calculateNeeded(ActionEvent event) {
        double remaining, desired, totalWeight,totalAchieved, neededPoints, neededPercent, max;
        desired = Double.parseDouble(desiredTextField.getText());
        totalWeight=totalAchieved=0;

        for (tableEntry row : table.getItems()) {
            totalAchieved += achievedColumn.getCellObservableValue(row).getValue(); // reads the values in achievedColumn
            totalWeight += weightColumn.getCellObservableValue(row).getValue();
        }

        remaining = 100 - totalWeight; //TODO ensure totalWeight is not >100 and desired is > totalAchieved
        neededPoints = desired - totalAchieved;
        neededPercent = neededPoints/remaining * 100;
        max = totalAchieved + remaining;


        //alert.setHeaderText(null);

        if(neededPoints>remaining){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
            alert.setContentText("You cannot achieve the desired total. Maximum you can achieve is: " + max + "%");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
            alert.setContentText("You need to achieve " + neededPoints + " out of " + remaining + " points, "
                    + "or " + neededPercent +"% in the remaining points, to achieve " + desired + "% overall."
            +" Maximum you can achieve is: " + max + "%");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
       desiredTextField.setText("");
    }

    @FXML
    void exitCalc(ActionEvent event) throws IOException {
        Parent MainApp = FXMLLoader.load(getClass().getResource("Application.fxml"));
        Scene mainScene = new Scene(MainApp);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }

    void clearTextBoxes(){
        subeventTextField.setText("");
        gradeTextField.setText("");
        outOfTextField.setText("");
        weightTextField.setText("");
    }

    // TODO:
    public void changeSubeventName(CellEditEvent edittedCell) {
          /*tableEntry cell =  table.getSelectionModel().getSelectedItem();
        cell.setSubevent(edittedCell.getNewValue().toString());*/
    }

}
