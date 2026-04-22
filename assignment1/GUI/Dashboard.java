package assignment1.GUI;

import assignment1.EmissionSource;
import assignment1.FootprintTracker;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Dashboard extends Tab{ // tab that displays a summary of all emission entries and their details

    // sets up the dashboard tab and registers a listener to refresh on data changes
    // tracker: shared footprint tracker instance

    public Dashboard(FootprintTracker tracker){
        setText("Dashboard");
        
        showdata(tracker);

        tracker.getArrayList().addListener(new DashboardListListener(this, tracker)); // auto-refresh when list changes
    }

    // rebuilds and renders the dashboard content from current tracker data
    // tracker: shared footprint tracker instance

    public void showdata(FootprintTracker tracker){

        ObservableList<EmissionSource> entries = tracker.getArrayList();
        VBox vbox = new VBox(10);
        GridPane details = new GridPane();
        setText("Dashboard");

        ScrollPane scrollPane = new ScrollPane();
        Label label = new Label("Summary: ");
        Label totalEntries = new Label("Total entries: " + entries.size());
        Label totalCO = new Label("Total CO2: " + tracker.getTotalEmissions());
        Label highestUser = new Label("Highest footprint: " + tracker.getHighestUser());
        label.setAlignment(Pos.TOP_LEFT);
        vbox.getChildren().add(label);
        vbox.getChildren().addAll(totalEntries, totalCO, highestUser);
        for (EmissionSource source : entries){
            if (source.calculateEmission()<1.0){ // low emission: green
                Button newLab = makButton(Color.GREEN, Color.WHITE);
                newLab.setText(source.getSourceID());
                newLab.setOnAction(new ShowDetailsAction(details, source));
                vbox.getChildren().add(newLab);
            }
            else if (source.calculateEmission()<3 && source.calculateEmission()>1){ // medium emission: yellow
                Button newLab = makButton(Color.YELLOW, Color.BLACK);
                newLab.setText(source.getSourceID());
                newLab.setOnAction(new ShowDetailsAction(details, source));
                vbox.getChildren().add(newLab);
            }
            else{ // high emission: red
                Button newLab = makButton(Color.RED, Color.WHITE);
                newLab.setText(source.getSourceID());
                newLab.setOnAction(new ShowDetailsAction(details, source));
                vbox.getChildren().add(newLab);
            }
        }

        
        vbox.getChildren().add(details);

        scrollPane.setContent(vbox);
        scrollPane.setFitToWidth(true);
        setContent(scrollPane);


        
    }

    // creates a styled button with the given background and text colors
    // backcolor: fill color for the button background
    // textColor: color for the button label text

    public Button makButton(Color backcolor, Color textColor){
        Button lab = new Button();
        BackgroundFill bf = new BackgroundFill(backcolor, CornerRadii.EMPTY, Insets.EMPTY);
        Background bg = new Background(bf);
        lab.setBackground(bg);
        lab.setTextFill(textColor);
        lab.setFont(Font.font("Arial", 20));
        return lab;
    }
    
    // creates a styled label with the given background and text colors
    // backcolor: fill color for the label background
    // textColor: color for the label text
    
    public Label makLabel(Color backcolor, Color textColor){
        Label lab = new Label();
        BackgroundFill bf = new BackgroundFill(backcolor, CornerRadii.EMPTY, Insets.EMPTY);
        Background bg = new Background(bf);
        lab.setBackground(bg);
        lab.setTextFill(textColor);
        lab.setFont(Font.font("Arial", 20));
        return lab;
    }
    
}
