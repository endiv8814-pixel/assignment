package assignment1.GUI.ai;

import java.util.ArrayList;

import assignment1.EmissionSource;
import assignment1.FootprintTracker;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;

public class DashboardAI extends Tab {

    private FootprintTracker tracker;
    private FlowPane flowPane;
    private Label summaryLabel;
    private TextArea detailArea;

    public DashboardAI(FootprintTracker tracker) {
        this.tracker = tracker;
        setText("Dashboard");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        summaryLabel = new Label();
        summaryLabel.setFont(Font.font("Arial", 14));
        updateSummary();

        flowPane = new FlowPane(8, 8);
        flowPane.setPadding(new Insets(5));

        detailArea = new TextArea();
        detailArea.setEditable(false);
        detailArea.setPrefHeight(120);
        detailArea.setFont(Font.font("Monospaced", 12));
        detailArea.setPromptText("Click an entry to see details...");

        ScrollPane scroll = new ScrollPane(flowPane);
        scroll.setPrefHeight(300);
        scroll.setFitToWidth(true);

        Label detailLabel = new Label("Entry Details:");
        detailLabel.setFont(Font.font("Arial", 13));

        root.getChildren().addAll(summaryLabel, scroll, detailLabel, detailArea);

        refreshEntries();
        setContent(root);
    }

    public void refreshEntries() {
        flowPane.getChildren().clear();
        updateSummary();

        ObservableList<EmissionSource> entries = tracker.getArrayList();

        for (EmissionSource e : entries) {
            double em = e.calculateEmission();
            Color bg;
            Color fg;

            if (em < 1.0) {
                bg = Color.GREEN;
                fg = Color.WHITE;
            } else if (em <= 3.0) {
                bg = Color.GOLD;
                fg = Color.BLACK;
            } else {
                bg = Color.CRIMSON;
                fg = Color.WHITE;
            }

            Label box = new Label(e.getSourceID());
            box.setFont(Font.font("Arial", 14));
            box.setTextFill(fg);
            box.setBackground(new Background(new BackgroundFill(bg, new CornerRadii(4), Insets.EMPTY)));
            box.setPadding(new Insets(8, 12, 8, 12));

            box.setOnMouseClicked(event -> detailArea.setText(e.toString()));

            flowPane.getChildren().add(box);
        }
    }

    private void updateSummary() {
        ObservableList<EmissionSource> entries = tracker.getArrayList();
        int count = entries.size();
        double total = tracker.getTotalEmissions();

        String topUser = "N/A";
        double topVal = -1;
        ArrayList<String> seen = new ArrayList<>();

        for (EmissionSource e : entries) {
            String u = e.getUserName();
            if (!seen.contains(u)) {
                seen.add(u);
                double uTotal = tracker.getTotalEmissionsForUser(u);
                if (uTotal > topVal) {
                    topVal = uTotal;
                    topUser = u;
                }
            }
        }

        summaryLabel.setText("Entries: " + count +
                "   |   Total CO2: " + String.format("%.2f", total) + " kg" +
                "   |   Top user: " + topUser);
    }
}
