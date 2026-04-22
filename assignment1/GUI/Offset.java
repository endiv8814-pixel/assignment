package assignment1.GUI;

import assignment1.FootprintTracker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class Offset extends Tab{ // tab that allows users to purchase carbon offsets and view transaction history

    private ObservableList<String> ol = FXCollections.observableArrayList(); // list of past transactions shown in history view
    private Label totalEm; // label displaying current total emissions, updated reactively


    // sets up the offset tab and registers a listener to keep total emissions label
    // tracker: shared footprint tracker instance
    public Offset(FootprintTracker tracker){

        setText("Offset");
        showdata(tracker);
        tracker.getArrayList().addListener(new OffsetListListener(this, tracker)); // auto-refresh label when entries change
    }

    // builds and renders the offset purchase UI
    // tracker: shared footprint tracker instance
    public void showdata(FootprintTracker tracker){
        VBox vbox = new VBox(10);
        FlowPane flowPane = new FlowPane(10,10);
        totalEm = makLabel(null, Color.BLACK);
        totalEm.setText("Total Emissions: " + tracker.getTotalEmissions());
        TextField TF = new TextField();
        TF.setPromptText("kg of CO2 to offset");
        ComboBox<String> cb = new ComboBox<>();
        cb.getItems().addAll("Credit Card", "Digital Wallet", "Campus Card");
        TextArea ta = new TextArea();
        ListView<String> history = new ListView<>(ol);
        history.setPrefHeight(150);
        history.setMaxHeight(150);
        Button purchase = makButton(null, Color.BLACK);
        purchase.setText("Purchase Offset");

        purchase.setOnAction(new PurchaseHandler(TF, cb, ta, purchase, ol));
        vbox.getChildren().addAll(totalEm, TF, cb, purchase, ta, history);
        flowPane.getChildren().add(vbox);
        setContent(flowPane);
    }

    public void updateTotalEmissions(FootprintTracker tracker){
        totalEm.setText("Total Emissions: " + tracker.getTotalEmissions());
    }


    public Button makButton(Color backcolor, Color textColor){
        Button lab = new Button();
        BackgroundFill bf = new BackgroundFill(backcolor, CornerRadii.EMPTY, Insets.EMPTY);
        Background bg = new Background(bf);
        lab.setBackground(bg);
        lab.setTextFill(textColor);
        lab.setFont(Font.font("Arial", 20));
        return lab;
    }
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
