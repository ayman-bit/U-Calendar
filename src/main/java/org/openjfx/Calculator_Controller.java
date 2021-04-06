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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.openjfx.tableEntry;

import java.io.IOException;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author: Kamal Ali
 */

public class Calculator_Controller implements Initializable {

    @FXML
    private AnchorPane parent;

    @FXML
    private TableView<tableEntry> table;

    @FXML
    private ChoiceBox<String> choices;

    @FXML
    private TableColumn<tableEntry, String> subeventColumn, achievedColumn;

    @FXML
    private TableColumn<tableEntry, Double> gradeColumn, outOfColumn, weightColumn; // Double should be DecimalFormat

    @FXML
    private TextField subeventTextField, gradeTextField, outOfTextField, weightTextField, desiredTextField;

    private String currentEvent;

    private tableEntry selectedRow;


    @Override
    public void initialize (URL url, ResourceBundle resourceBundle){
        makeStageDragable();
        loadClasses(); // loads the classes to the choice box

        // Listen for changes in selection and update currentEvent
        choices.getSelectionModel().selectedItemProperty().addListener(
                (v, oldValue, newValue) -> {
                    currentEvent = newValue;
                    table.setItems(getEntries());
                    }
        );

        subeventColumn.setCellValueFactory(
                new PropertyValueFactory<>("subevent")
        );
        gradeColumn.setCellValueFactory(
                new PropertyValueFactory<>("grade")
        );
        outOfColumn.setCellValueFactory(
                new PropertyValueFactory<>("outOf")
        );
        weightColumn.setCellValueFactory(
                new PropertyValueFactory<>("weight")
        );

        //TODO: check this works
        achievedColumn.setCellValueFactory(
                new PropertyValueFactory<>("achieved")
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
            String gradeS = row.get("grade").toString();
            String outOfS = row.get("outOf").toString();
            String weightS = row.get("weight").toString();



            entries.add( new tableEntry(subevent,
                    Double.parseDouble(gradeS),
                    Double.parseDouble(outOfS),
                    Double.parseDouble(weightS)) );
        }

        return entries;
    }

    @FXML
    void addToTableClicked(ActionEvent event) {

        if(currentEvent==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please choose a class from the list above first to start adding");
            alert.showAndWait();
            return;
        }

        // Make sure user provided the right input
        if (gradeTextField.getText() == "" || outOfTextField.getText() == "" || weightTextField.getText() == ""){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please make sure to fill in all the proper boxes");
            alert.showAndWait();
            return;
        }

        double grade, outOf, weight;
        grade = outOf = weight = 0;

        // Extract the inputs from the TextFields and perform casting if necessary
        try{
            grade = Double.parseDouble(gradeTextField.getText());
            outOf = Double.parseDouble(outOfTextField.getText());
            weight = Double.parseDouble(weightTextField.getText());
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please make sure your fields are formatted correctly");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
        String subevent = subeventTextField.getText();

        //Make sure nothing is null

        String qu = "INSERT INTO grades(eventName,subevent,grade,outOf,weight,user_id) VALUES ("
                + "'" + currentEvent + "',"
                + "'" + subevent + "',"
                + "'" + grade + "',"
                + "'" + outOf + "',"
                + "'" + weight + "',"
                + "'" + Login_Controller.uid + "'"
                + ")";

        if(grade>outOf){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Warning: 'Grade' is greater than 'Out of'");
            alert.showAndWait();
        }
        boolean q = DatabaseHandler.execAction(qu);
        if(!q){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Error in inserting to database");
            alert.showAndWait();
        }

        table.setItems(getEntries()); // Update the displayed entries
        clearTextBoxes();


    }

    @FXML
    void calculateTotal(ActionEvent event){
        String totalString, percentageS ="";
        double total, totalPossible, lost, percentage;
        total=totalPossible=percentage=0;

        for (tableEntry row : table.getItems()) {
            totalString = achievedColumn.getCellObservableValue(row).getValue(); // reads the values in achievedColumn
            total+= Double.parseDouble(totalString);
            totalPossible += weightColumn.getCellObservableValue(row).getValue();
        }

        lost = totalPossible - total;

        if(totalPossible!=0){
            percentage = total/totalPossible * 100;
            DecimalFormat df = new DecimalFormat("#.##");
            percentageS = df.format(percentage);
            lost = Double.parseDouble(df.format(lost));
        }


        String totalValueLabel = "Achieved " + total + " out of a possible " + totalPossible
                + " points\nand lost " + lost + " points. Current percentage: "
                + percentageS + "%";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(totalValueLabel);
        alert.showAndWait();

    }

    @FXML
    void calculateNeeded(ActionEvent event) {
        String totalString, neededPercent;
        double remaining, desired, totalWeight,totalAchieved, neededPoints, max;
        totalWeight=totalAchieved=0;
        DecimalFormat df = new DecimalFormat("#.##");

        if(currentEvent == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please choose a class from the list above first to start adding");
            alert.showAndWait();
            return;
        }

        if(desiredTextField.getText() == ""){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter your desired grade first");
            alert.showAndWait();
            return;
        }
        try{
            desired = Double.parseDouble(desiredTextField.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please make sure your desired grade is a number");
            alert.setHeaderText(null);
            alert.showAndWait();
            desiredTextField.setText("");
            return;
        }


        for (tableEntry row : table.getItems()) {
            totalString = achievedColumn.getCellObservableValue(row).getValue(); // reads the values in achievedColumn
            totalAchieved+= Double.parseDouble(totalString);
            totalWeight += weightColumn.getCellObservableValue(row).getValue();
        }

        remaining = 100 - totalWeight;
        neededPoints = desired - totalAchieved;
        neededPercent = df.format(neededPoints/remaining * 100);
        max = totalAchieved + remaining;

        if(totalWeight>100){
            Alert alert = new Alert(Alert.AlertType.WARNING, "", ButtonType.OK);
            alert.setContentText("The total weight (" + totalWeight + ") is greater than 100. Consider deleting and adjusting some entries.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
        if (desired<totalAchieved){
            Alert alert = new Alert(Alert.AlertType.WARNING, "", ButtonType.OK);
            alert.setContentText("Your desired total is less than your current total (" + totalAchieved + ")");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
        else if(neededPoints>remaining){
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
    void getSelectedRow(MouseEvent event){
        selectedRow = table.getSelectionModel().getSelectedItem();
    }

    @FXML
    void deleteRow(ActionEvent event){
        if (selectedRow != null){
            String subevent = selectedRow.getSubevent();
            String qu = "DELETE FROM grades WHERE (user_id = '" + Login_Controller.uid + "' AND eventName = '" + currentEvent + "' AND subevent = '" + subevent +"')";
            boolean q = DatabaseHandler.execAction(qu);

            if(q){
                table.setItems(getEntries());
            }
            else{
                //what to do when q is false
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Please select a row first from the table above");
            alert.showAndWait();
        }
    }

    @FXML
    void exitCalc(MouseEvent event) throws IOException {
        Controller.start("Application.fxml", event);
    }

    void clearTextBoxes(){
        subeventTextField.setText("");
        gradeTextField.setText("");
        outOfTextField.setText("");
        weightTextField.setText("");
    }

    @FXML
    private void close_app(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void minimize_stage(MouseEvent event) {
        UCalendar.stage.setIconified(true);
    }

    private double xOffSet = 0;
    private double yOffSet = 0;
    private void makeStageDragable() {
        parent.setOnMousePressed((event) -> {
            xOffSet = event.getSceneX();
            yOffSet = event.getSceneY();
        });
        parent.setOnMouseDragged((event) -> {
            UCalendar.stage.setX(event.getScreenX() - xOffSet);
            UCalendar.stage.setY(event.getScreenY() - yOffSet);
            UCalendar.stage.setOpacity(0.8f);
        });
        parent.setOnDragDone((event) -> {
            UCalendar.stage.setOpacity(1.0f);
        });
        parent.setOnMouseReleased((event) -> {
            UCalendar.stage.setOpacity(1.0f);
        });
    }

}