package assignment1.GUI.ai;

import assignment1.EnergyEmission;
import assignment1.FoodEmission;
import assignment1.FootprintTracker;
import assignment1.TransportationEmission;
import assignment1.GUI.LoggerUtil;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class InputTabAI extends Tab {
    public InputTabAI(FootprintTracker tracker) {
        setText("Input");

        VBox root = new VBox(10);
        root.setPadding(new Insets(12));

        // source ID + validation
        Label idLabel = new Label("Source ID:");
        TextField idField = new TextField();
        idField.setMaxWidth(120);
        Label validLabel = new Label("✗ Invalid");
        validLabel.setTextFill(Color.RED);

        idField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.matches("[A-Z]-\\d{3}")) {
                validLabel.setText("✓ Valid");
                validLabel.setTextFill(Color.GREEN);
            } else {
                validLabel.setText("✗ Invalid");
                validLabel.setTextFill(Color.RED);
            }
        });

        HBox idRow = new HBox(8, idLabel, idField, validLabel);

        // type selection
        Label typeLabel = new Label("Type:");
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Transportation", "Energy", "Food");
        typeBox.setValue("Transportation");

        // dynamic fields container
        VBox dynamicFields = new VBox(8);

        // transport fields
        TextField distField = new TextField();
        distField.setPromptText("Distance (km)");
        distField.setMaxWidth(150);
        ComboBox<String> modeBox = new ComboBox<>();
        modeBox.getItems().addAll("Car", "Bus", "Train", "Bicycle");
        modeBox.setValue("Car");
        VBox transportBox = new VBox(6, new Label("Distance (km):"), distField, new Label("Mode:"), modeBox);

        // energy fields
        TextField kwhField = new TextField();
        kwhField.setPromptText("kWh");
        kwhField.setMaxWidth(150);
        ComboBox<String> srcBox = new ComboBox<>();
        srcBox.getItems().addAll("Grid", "Solar", "Wind");
        srcBox.setValue("Grid");
        VBox energyBox = new VBox(6, new Label("kWh:"), kwhField, new Label("Source:"), srcBox);

        // food fields
        ComboBox<String> mealBox = new ComboBox<>();
        mealBox.getItems().addAll("Vegan", "Vegetarian", "Poultry", "Beef");
        mealBox.setValue("Vegan");
        TextField mealsField = new TextField();
        mealsField.setPromptText("Number of meals");
        mealsField.setMaxWidth(150);
        VBox foodBox = new VBox(6, new Label("Meal type:"), mealBox, new Label("Number of meals:"), mealsField);

        dynamicFields.getChildren().add(transportBox);

        typeBox.setOnAction(e -> {
            dynamicFields.getChildren().clear();
            String selected = typeBox.getValue();
            if (selected.equals("Transportation")) dynamicFields.getChildren().add(transportBox);
            else if (selected.equals("Energy")) dynamicFields.getChildren().add(energyBox);
            else dynamicFields.getChildren().add(foodBox);
        });

        // user + date
        TextField userField = new TextField();
        userField.setPromptText("User name");
        userField.setMaxWidth(150);
        TextField dateField = new TextField();
        dateField.setPromptText("Date (yyyy-mm-dd)");
        dateField.setMaxWidth(150);

        // status
        Label statusLabel = new Label();
        statusLabel.setFont(Font.font("Arial", 13));

        // add button
        Button addBtn = new Button("Add Entry");
        addBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            String user = userField.getText().trim();
            String date = dateField.getText().trim();
            String type = typeBox.getValue();

            if (!id.matches("[A-Z]-\\d{3}") || user.isEmpty() || date.isEmpty()) {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("Fill all fields correctly.");
                return;
            }

            try {
                if (type.equals("Transportation")) {
                    double dist = Double.parseDouble(distField.getText().trim());
                    String mode = modeBox.getValue();
                    TransportationEmission te = new TransportationEmission(id, "Transportation", date, user, dist, mode);
                    tracker.addEntry(te);
                    LoggerUtil.logEntryAdded(id, user, te.calculateEmission());

                } else if (type.equals("Energy")) {
                    double kwh = Double.parseDouble(kwhField.getText().trim());
                    String src = srcBox.getValue();
                    EnergyEmission ee = new EnergyEmission(id, "Energy", date, user, kwh, src);
                    tracker.addEntry(ee);
                    LoggerUtil.logEntryAdded(id, user, ee.calculateEmission());

                } else {
                    double meals = Double.parseDouble(mealsField.getText().trim());
                    String meal = mealBox.getValue();
                    FoodEmission fe = new FoodEmission(id, "Food", date, user, meal, meals);
                    tracker.addEntry(fe);
                    LoggerUtil.logEntryAdded(id, user, fe.calculateEmission());
                }

                statusLabel.setTextFill(Color.GREEN);
                statusLabel.setText("Entry added!");
                idField.clear();

            } catch (NumberFormatException ex) {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("Invalid number entered.");
            }
        });

        // search section
        Label searchLabel = new Label("Search by user:");
        searchLabel.setFont(Font.font("Arial", 13));
        TextField searchField = new TextField();
        searchField.setMaxWidth(150);
        Button searchBtn = new Button("Search");
        ListView<String> resultList = new ListView<>();
        resultList.setPrefHeight(120);

        searchBtn.setOnAction(e -> {
            resultList.getItems().clear();
            String name = searchField.getText().trim();
            for (assignment1.EmissionSource src : tracker.getArrayList()) {
                if (src.getUserName().equals(name)) {
                    resultList.getItems().add(src.toString());
                }
            }
            if (resultList.getItems().isEmpty()) {
                resultList.getItems().add("No entries found for: " + name);
            }
        });

        HBox searchRow = new HBox(8, searchField, searchBtn);

        root.getChildren().addAll(
            idRow,
            new HBox(8, typeLabel, typeBox),
            dynamicFields,
            new Label("User:"), userField,
            new Label("Date:"), dateField,
            addBtn, statusLabel,
            searchLabel, searchRow, resultList
        );

        setContent(root);
    }
}
