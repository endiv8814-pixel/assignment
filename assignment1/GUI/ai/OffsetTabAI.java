package assignment1.GUI.ai;

import assignment1.FootprintTracker;
import assignment1.GUI.LoggerUtil;
import assignment1.GUI.TransactionHandler;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class OffsetTabAI extends Tab {
    private Label totalLabel;

    public OffsetTabAI(FootprintTracker tracker) {
        setText("Carbon Offset");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        totalLabel = new Label("Total emissions: " + String.format("%.2f", tracker.getTotalEmissions()) + " kg CO2");
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

        Label amtLabel = new Label("kg CO2:");
        TextField amtField = new TextField();
        amtField.setPromptText("enter amount");

        Label payLabel = new Label("Payment:");
        ComboBox<String> payBox = new ComboBox<>();
        payBox.getItems().addAll("Credit Card", "Digital Wallet", "Campus Card");
        payBox.setValue("Credit Card");

        grid.add(amtLabel, 0, 0);
        grid.add(amtField, 1, 0);
        grid.add(payLabel, 0, 1);
        grid.add(payBox, 1, 1);

        Label statusLabel = new Label();

        Button buyBtn = new Button("Purchase Offset");

        TextArea receiptArea = new TextArea();
        receiptArea.setEditable(false);
        receiptArea.setPrefHeight(150);
        receiptArea.setFont(Font.font("Monospaced", 11));

        Label histLabel = new Label("History:");
        histLabel.setFont(Font.font("Arial", 13));

        ListView<String> historyList = new ListView<>();
        historyList.setPrefHeight(120);

        buyBtn.setOnAction(e -> {
            String raw = amtField.getText().trim();

            if (raw.isEmpty()) {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("enter amount first");
                return;
            }

            double kg;
            try {
                kg = Double.parseDouble(raw);
            } catch (NumberFormatException ex) {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("not a valid number");
                return;
            }

            if (kg <= 0) {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("must be > 0");
                return;
            }

            String pay = payBox.getValue();
            buyBtn.setDisable(true);
            statusLabel.setTextFill(Color.GRAY);
            statusLabel.setText("processing...");
            receiptArea.clear();

            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            double finalKg = kg;

            pause.setOnFinished(ev -> {
                receiptArea.setText(TransactionHandler.makeReceipt(finalKg, pay));
                historyList.getItems().add(0, TransactionHandler.makeHistoryLine(finalKg, pay));
                LoggerUtil.logOffsetPurchased(finalKg, pay);
                totalLabel.setText("Total emissions: " + String.format("%.2f", tracker.getTotalEmissions()) + " kg CO2");
                statusLabel.setTextFill(Color.GREEN);
                statusLabel.setText("done!");
                buyBtn.setDisable(false);
                amtField.clear();
            });

            pause.play();
        });

        root.getChildren().addAll(
            totalLabel,
            grid,
            buyBtn,
            statusLabel,
            new Label("Receipt:"),
            receiptArea,
            histLabel,
            historyList
        );

        setContent(root);
    }
}
