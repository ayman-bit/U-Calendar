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
import javafx.stage.Stage;
import org.openjfx.tableEntry;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/*
@Author: Kamal Ali
 */

public class Calculator_Controller implements Initializable {

    @FXML
    private TableView<tableEntry> table;

    @FXML
    private ChoiceBox<String> choices;

    @FXML
    private TableColumn<tableEntry, String> subeventColumn;

    @FXML
    private TableColumn<tableEntry, Double> gradeColumn;

    @FXML
    private TableColumn<tableEntry, Double> outOfColumn;

    @FXML
    private TableColumn<tableEntry, Double> weightColumn;

    @FXML
    private TableColumn<tableEntry, Double> achievedColumn;

    @FXML
    private TextField subeventTextField;

    @FXML
    private TextField gradeTextField;

    @FXML
    private TextField outOfTextField;

    @FXML
    private TextField weightTextField;

    @FXML
    private Label totalLabel;

    @FXML
    private TextField desiredTextField;

    @FXML
    private Button calculateNeeded;

    @FXML
    private Button addToTable;

    private int index;



    @Override
    public void initialize (URL url, ResourceBundle resourceBundle){

        loadClasses(); // loads the classes to the choice box

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

        // Load the dummy data to table
        table.setItems(getEntries());
        //table.getColumns().addAll(subeventColumn, gradeColumn, outOfColumn, weightColumn, achievedColumn);
        calculateTotal();

        // TODO
        /*
        public void changeSubeventName(CellEditEvent edittedCell)
        {
            tableEntry cell =  table.getSelectionModel().getSelectedItem();
            cell.setSubevent(edittedCell.getNewValue().toString());
        }
        */
    }

    // Reads the data. TODO: This should be reading from the database in the future depending on selection
    public ObservableList<tableEntry>  getEntries()
    {
        ObservableList<tableEntry> entries = FXCollections.observableArrayList();
        entries.add(new tableEntry("Assign1", 85, 100, 5));
        entries.add(new tableEntry("Assign2", 30, 30, 10));
        entries.add(new tableEntry("Midterm", 90, 100, 25));
        entries.add(new tableEntry("Project", 50, 50, 60));

        return entries;
    }

    //TODO: addToTableClicked should add the new item to the database.
    @FXML
    void addToTableClicked(ActionEvent event) {

        // Extract the inputs from the TextFields and perform casting if necessary
        String subevent = subeventTextField.getText();
        double grade = Double.parseDouble(gradeTextField.getText());
        double outOf = Double.parseDouble(outOfTextField.getText());
        double weight = Double.parseDouble(weightTextField.getText());

        tableEntry entry = new tableEntry(subevent, grade, outOf, weight);
        table.getItems().add(entry);
        calculateTotal();
    }


    public void calculateTotal(){
        double total = 0;

        for (tableEntry row : table.getItems()) {
            total += achievedColumn.getCellObservableValue(row).getValue(); // reads the values in achievedColumn
        }

        String totalValueLabel = String.valueOf(total); // Cast total to String
        totalLabel.setText(totalValueLabel); // Set the value of the label to the total
    }

    //TODO
    @FXML
    void calculateNeeded(ActionEvent event) {

    }

    //TODO: loadClasses should read classes from database
    void loadClasses(){
        choices.getItems().add("ECE 5010");
        choices.getItems().add("ECE 5100");
        choices.getItems().add("ECE 5500");
    }

    @FXML
    void exitCalc(ActionEvent event) throws IOException {
        Parent MainApp = FXMLLoader.load(getClass().getResource("Application.fxml"));
        Scene mainScene = new Scene(MainApp);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }
}
