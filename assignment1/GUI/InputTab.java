package assignment1.GUI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import assignment1.EmissionSource;
import assignment1.FootprintTracker;


public class InputTab extends Tab{//Tab that takes user input and creates an emission out of it
    //It sets up a series of inputs through javafx tools. It uses Labels, Buttons and comboBoxes according to the assignment
    //Also, it has valid checks for if some spaces are left empty or numbers are negative
    //At the end has a button that creates the emission if everything is valid.
    //Last section contains search by name function. It is realised through listView
    //Takes FootprintTracker as an argument.
    //Does not return anything.
    public InputTab(FootprintTracker tracker){
        VBox vbox = new VBox(10);
        setText("Input");
        FlowPane flowPane = new FlowPane(10,10);
        TextField TF = new TextField();
        TF.setPromptText("Enter Source ID");
        Label Validation = makeLabel(null, Color.RED);
        TF.textProperty().addListener(new ValidationListenerHandle(Validation));
        ComboBox<String> comboBox = new ComboBox<>();
        TextField TFT = new TextField();
        ComboBox<String> comboBox2 = new ComboBox<>();
        ObservableList<String> items = FXCollections.observableArrayList("Transportation", "Energy", "Food");
        comboBox.setItems(items);
        comboBox.setPromptText("Select...");
        comboBox.setOnAction(new ComboBoxHandler(comboBox2, comboBox, TFT, vbox));
        VBox vbox2 = new VBox(10);
        TextField TF2 = new TextField();
        TF2.setPromptText("Enter User name...");
        DatePicker datePicker = new DatePicker(); 
        datePicker.setPromptText("Enter date...");
        Label ReadyOrNot = makeLabel(Color.WHITE, Color.BLACK);
        Button button = makeButton(Color.WHITE, Color.BLACK);
        button.setText("Add Entry");
        button.setOnAction(new ButtonHandler(tracker, TF, TFT, TF2, comboBox, comboBox2, ReadyOrNot, datePicker));
        VBox vbox3 = new VBox(10); 
        TextField TF3 = new TextField();
        ObservableList<EmissionSource> searched_emissions = FXCollections.observableArrayList();
        ListView<EmissionSource> listView = new ListView<>(searched_emissions);
        TF3.setPromptText("Search By user...");
        Button search = makeButton(Color.WHITE, Color.BLACK);
        search.setText("Search");
        vbox3.getChildren().addAll(TF3, search, listView); // vbox for search by name
        search.setOnAction(new SearchHandler(tracker.getArrayList(), searched_emissions, TF3));
        TF3.setPromptText("Search By User...");
        vbox.getChildren().addAll(TF, comboBox); //vbox for comboBox and footprint name
        vbox2.getChildren().addAll(Validation, TF2, datePicker, button, ReadyOrNot); //vbox for username, date, button and valid checker
        flowPane.getChildren().addAll(vbox, vbox2, vbox3); 
        setContent(flowPane);
    }
    //Utility class for validating if a string has certain regex pattern
    //Takes String for text and String for regex as arguments
    //Returns True or False depending if text has regex pattern
    public boolean Validator(String text, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }
    //Creates a label 
    //Gets Two colors as arguments
    //Returns a label with given colors
    public Label makeLabel(Color backcolor, Color textColor){
        Label lab = new Label();
        BackgroundFill bf = new BackgroundFill(backcolor, CornerRadii.EMPTY, Insets.EMPTY);
        Background bg = new Background(bf);
        lab.setBackground(bg);
        lab.setTextFill(textColor);
        lab.setFont(Font.font("Arial", 20));
        return lab;
    }

    //Creates a button
    //Uses Color for backcolor and Color for textcolor
    //Returns the button with given colors
    public Button makeButton(Color backcolor, Color textColor){
        Button lab = new Button();
        BackgroundFill bf = new BackgroundFill(backcolor, CornerRadii.EMPTY, Insets.EMPTY);
        Background bg = new Background(bf);
        lab.setBackground(bg);
        lab.setTextFill(textColor);
        lab.setFont(Font.font("Arial", 20));
        return lab;
    }
}
