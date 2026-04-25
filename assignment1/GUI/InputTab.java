package assignment1.GUI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import assignment1.EmissionSource;
import assignment1.FootprintTracker;


public class InputTab extends Tab{//Tab that takes user input and creates an emission out of it
    //It sets up a series of inputs through javafx tools. It uses Labels, Buttons and comboBoxes according to the assignment
    //Also, it has valid checks for if some spaces are left empty or numbers are negative
    //At the end has a button that creates the emission if everything is valid.
    //Last section contains search by name function. It is realised through listView
    //Takes FootprintTracker as an argument.
    //Does not return anything.
    private static final Color BG = Color.rgb(2, 6, 4);
    private static final Color CARD_BG = Color.rgb(6, 15, 10); 
    private static final Color BRIGHT_GREEN = Color.rgb(65, 240, 105); 
    private static final Color DIM_GREEN = Color.rgb(45, 120, 80);
    private static final Color BORDER_GREEN = Color.rgb(30, 80, 50);

    public InputTab(FootprintTracker tracker){

        setText("Input");

        HBox root = new HBox(40);

        root.setPadding(new Insets(40));

        root.setBackground(new Background(new BackgroundFill(BG, CornerRadii.EMPTY, Insets.EMPTY)));

        root.setAlignment(Pos.TOP_CENTER);

        VBox left = new VBox(20);

        Label entryTitle = makeLabel("+ NEW_ENTRY_NODE", BRIGHT_GREEN, 18);

        GridPane grid = new GridPane();

        grid.setHgap(15);
        grid.setVgap(15);

        grid.setPadding(new Insets(20));
        grid.setBorder(new Border(new BorderStroke(BORDER_GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        grid.setMaxWidth(Double.MAX_VALUE);


        TextField TF = makeTextField(">> ENTER_SOURCE_ID");

        TextField  TFU = makeTextField(">> ENTER_USER");

        DatePicker datePicker = new DatePicker();

        datePicker.getEditor().setFont(Font.font("Courier New", 14));
        datePicker.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        datePicker.setBorder(new Border(new BorderStroke(BORDER_GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        datePicker.setMaxWidth(Double.MAX_VALUE);

        ComboBox<String> cb = makeComboBox(">> SELECT_CATEGORY");
        cb.setItems(FXCollections.observableArrayList("Transportation", "Energy", "Food"));
        
        ComboBox<String> cb2 = new ComboBox<>();
        cb2.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        cb2.setBorder(new Border(new BorderStroke(BORDER_GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        cb2.setMaxWidth(Double.MAX_VALUE);
        
        TextField a = new TextField();
        VBox inputs = new VBox(10);
        cb.setOnAction(new ComboBoxHandler(cb2, cb, a, inputs));


        grid.add(makeLabel("SOURCE_ID", DIM_GREEN, 12), 0, 0);
        grid.add(TF, 0, 1, 2, 1);
        
        grid.add(makeLabel("USER_ENTITY", DIM_GREEN, 12), 0, 2);
        grid.add(makeLabel("TIMESTAMP", DIM_GREEN, 12), 1, 2);
        grid.add(TFU, 0, 3);
        grid.add(datePicker, 1, 3);
        
        grid.add(makeLabel("CATEGORY & DATA", DIM_GREEN, 12), 0, 4);
        grid.add(cb, 0, 5, 2, 1);
        grid.add(inputs, 0, 6, 2, 1);



        Label Validation = makeLabel("", Color.RED, 12);

        TF.textProperty().addListener(new ValidationListenerHandle(Validation));

        Button add = makeButton(">> ADD ENTRY");
        add.setMaxWidth(Double.MAX_VALUE);

        add.setOnAction(new ButtonHandler(tracker, TF, a, TFU, cb, cb2, Validation, datePicker));

        left.getChildren().addAll(entryTitle, grid, Validation, add);        
        




        VBox right = new VBox(20);

        Label search = makeLabel(">>SEARCH BY USER", BRIGHT_GREEN, 18);

        VBox searchBox = new VBox(15);

        searchBox.setPadding(new Insets(20));

        searchBox.setBorder(new Border(new BorderStroke(BORDER_GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        searchBox.setMaxWidth(Double.MAX_VALUE);

        TextField tfs = makeTextField(">> SEARCH BY USER");

        Button bSearch = makeButton(">> RUN SEARCH");

        ObservableList<EmissionSource> res = FXCollections.observableArrayList();

        ListView<EmissionSource> view = new ListView<>(res);

        view.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        view.setBorder(new Border(new BorderStroke(BORDER_GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        view.setMaxWidth(Double.MAX_VALUE);

        bSearch.setOnAction(new SearchHandler(tracker.getArrayList(), res, tfs));

        searchBox.getChildren().addAll(tfs, bSearch, view);
        right.getChildren().addAll(search, searchBox);

        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);
        root.getChildren().addAll(left,right);

        ScrollPane scroll = new ScrollPane(root);

        scroll.setFitToWidth(true);

        scroll.setFitToHeight(true);

        setContent(scroll);
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
    public Label makeLabel(String text, Color backcolor, double size){
        Label lab = new Label();
        lab.setTextFill(backcolor);

        lab.setFont(Font.font("Courier New", FontWeight.BOLD, size));
        return lab; 
    }

    //Creates a button
    //Uses Color for backcolor and Color for textcolor
    //Returns the button with given colors
    public Button makeButton(String s){
        Button btn = new Button(s);
        btn.setFont(Font.font("Courier New", FontWeight.BOLD, 14));
        btn.setTextFill(BRIGHT_GREEN);
        btn.setBackground(new Background(new BackgroundFill(CARD_BG, CornerRadii.EMPTY, Insets.EMPTY)));
        btn.setBorder(new Border(new BorderStroke(BRIGHT_GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        btn.setPadding(new Insets(12, 20, 12, 20));
        
        
        return btn;
    }

    public TextField makeTextField(String s){

        TextField tf = new TextField();
        tf.setPromptText(s);
        tf.setFont(Font.font("Courier New", 14));
        tf.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        tf.setBorder(new Border(new BorderStroke(BORDER_GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        tf.setPadding(new Insets(10));
        tf.setMaxWidth(Double.MAX_VALUE);
        return tf;

    }
    private ComboBox<String> makeComboBox(String s) {
        ComboBox<String> cb = new ComboBox<>();
        cb.setPromptText(s);
        cb.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        cb.setBorder(new Border(new BorderStroke(BORDER_GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        cb.setMaxWidth(Double.MAX_VALUE);
        return cb;
    }
}
