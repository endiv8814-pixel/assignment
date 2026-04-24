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
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Dashboard extends Tab {

    private static final Color BG = Color.rgb(2, 6, 4); 
    private static final Color HEADER_BG = Color.rgb(5, 15, 10);  
    private static final Color CARD_BG = Color.rgb(4, 12, 8);    
    private static final Color CARD_BG_RED = Color.rgb(24, 4, 4); 
    private static final Color BRIGHT_GREEN = Color.rgb(65, 240, 105); 
    private static final Color DIM_GREEN = Color.rgb(45, 140, 85);  
    private static final Color BORDER_GREEN = Color.rgb(30, 80, 50); 
    private static final Color RED = Color.rgb(235, 65, 80); 
    private static final Color YELLOW = Color.rgb(190, 190, 50); 

    public Dashboard(FootprintTracker tracker) {
        setText("Dashboard");
        showdata(tracker);
        tracker.getArrayList().addListener(new DashboardListListener(this, tracker));
    }

    private HBox buildHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(14, 24, 14, 24));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setBackground(new Background(new BackgroundFill(HEADER_BG, CornerRadii.EMPTY, Insets.EMPTY)));
        header.setBorder(new Border(new BorderStroke(
            BORDER_GREEN, BorderStrokeStyle.SOLID,
            CornerRadii.EMPTY, new BorderWidths(0, 0, 1, 0)
        )));

        Label logo = new Label("VICTORIAPRINT_V3.0");
        logo.setFont(Font.font("Courier New", FontWeight.BOLD, 22));
        logo.setTextFill(BRIGHT_GREEN);

        header.getChildren().add(logo);
        return header;
    }

    public void showdata(FootprintTracker tracker) {
        ObservableList<EmissionSource> entries = tracker.getArrayList();

        VBox root = new VBox(24);
        root.setPadding(new Insets(26));
        root.setBackground(new Background(new BackgroundFill(BG, CornerRadii.EMPTY, Insets.EMPTY)));

        GridPane details = new GridPane();
        details.setHgap(16);
        details.setVgap(8);

        details.setBackground(new Background(new BackgroundFill(BG, CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(buildHeader());

        root.getChildren().add(buildSummarySection(tracker, entries.size()));

        root.getChildren().add(buildSectionHeader(">> TRANSACTION_LOGS"));

        FlowPane cardsPane = new FlowPane(12, 12);
        cardsPane.setPadding(new Insets(2, 0, 0, 0));
        for (EmissionSource source : entries) {
            cardsPane.getChildren().add(buildCard(source, details));
        }
        root.getChildren().add(cardsPane);
        root.getChildren().add(details);

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setBackground(new Background(new BackgroundFill(BG, CornerRadii.EMPTY, Insets.EMPTY)));
        scroll.setBorder(Border.EMPTY);

        setContent(scroll);
    }

    private VBox buildSummarySection(FootprintTracker tracker, int count) {
        VBox section = new VBox(12);

        section.getChildren().add(buildSectionHeader(">> SYSTEM_SUMMARY"));

        HBox statsCard = new HBox(0);
        statsCard.setBackground(new Background(new BackgroundFill(CARD_BG, new CornerRadii(3), Insets.EMPTY)));
        statsCard.setBorder(new Border(new BorderStroke(
            BORDER_GREEN, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(1)
        )));
        statsCard.setPadding(new Insets(20, 0, 20, 0));
        statsCard.setMinHeight(110);

        VBox entriesBox = buildStatBox(">> TOTAL ENTRIES",   String.valueOf(count), BRIGHT_GREEN);
        VBox co2Box = buildStatBox(">> TOTAL CO2 (KG)", String.valueOf(tracker.getTotalEmissions()), BRIGHT_GREEN);
        VBox highBox = buildStatBox(">> HIGHEST FOOTPRINT", tracker.getHighestUser(), RED);

        HBox.setHgrow(entriesBox, Priority.ALWAYS);
        HBox.setHgrow(co2Box, Priority.ALWAYS);
        HBox.setHgrow(highBox, Priority.ALWAYS);

        statsCard.getChildren().addAll(entriesBox, makeVSep(), co2Box, makeVSep(), highBox);
        section.getChildren().add(statsCard);
        return section;
    }

    private VBox buildStatBox(String labelText, String valueText, Color valueColor) {
        VBox box = new VBox(6);
        box.setPadding(new Insets(0, 30, 0, 22));
        box.setAlignment(Pos.TOP_LEFT);
        box.setBorder(new Border(new BorderStroke(
            DIM_GREEN, BorderStrokeStyle.SOLID,
            CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 2)
        )));

        Label lbl = new Label(labelText);
        lbl.setFont(Font.font("Courier New", FontWeight.BOLD, 11));
        lbl.setTextFill(DIM_GREEN);

        Label val = new Label(valueText);
        val.setFont(Font.font("Courier New", FontWeight.BOLD, 34));
        val.setTextFill(valueColor);

        box.getChildren().addAll(lbl, val);
        return box;
    }

    private Button buildCard(EmissionSource source, GridPane details) {
        double emission = source.calculateEmission();

        Color borderColor;
        Color cardBg;
        if (emission < 1.0) {
            borderColor = Color.rgb(0, 180, 60);
            cardBg = CARD_BG;
        } else if (emission < 3.0) {
            borderColor = YELLOW;
            cardBg = Color.rgb(20, 20, 4);
        } else {
            borderColor = RED;
            cardBg = CARD_BG_RED;
        }

        VBox content = new VBox(6);
        content.setPadding(new Insets(14, 18, 14, 18));
        content.setMinWidth(155);

        String userName = "";
        try {
            userName = source.getUserName();
        } catch (Exception ignored) { }

        Label ownerLbl = new Label(userName);
        ownerLbl.setFont(Font.font("Courier New", FontWeight.BOLD, 14));
        ownerLbl.setTextFill(DIM_GREEN);

        Label idLbl = new Label(source.getSourceID());
        idLbl.setFont(Font.font("Courier New", FontWeight.BOLD, 19));
        idLbl.setTextFill(BRIGHT_GREEN);

        Label co2Lbl = new Label(emission + "kg CO2");
        co2Lbl.setFont(Font.font("Courier New", FontWeight.BOLD, 14));
        co2Lbl.setTextFill(DIM_GREEN);

        content.getChildren().addAll(ownerLbl, idLbl, co2Lbl);

        Button card = new Button();
        card.setGraphic(content);
        card.setPadding(Insets.EMPTY);
        card.setBackground(new Background(new BackgroundFill(cardBg, new CornerRadii(4), Insets.EMPTY)));
        card.setBorder(new Border(new BorderStroke(
            borderColor, BorderStrokeStyle.SOLID, new CornerRadii(4), new BorderWidths(2)
        )));
        card.setOnAction(new ShowDetailsAction(details, source));

        return card;
    }

    private Label buildSectionHeader(String title) {
        Label lbl = new Label(title);
        lbl.setFont(Font.font("Courier New", FontWeight.BOLD, 14));
        lbl.setTextFill(DIM_GREEN);
        return lbl;
    }

    private Region makeVSep() {
        Region sep = new Region();
        sep.setMinWidth(1);
        sep.setMaxWidth(1);
        sep.setMinHeight(70);
        sep.setBackground(new Background(new BackgroundFill(BORDER_GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        return sep;
    }

    public Button makButton(Color backcolor, Color textColor) {
        Button lab = new Button();
        lab.setTextFill(BRIGHT_GREEN);
        lab.setFont(Font.font("Courier New", FontWeight.BOLD, 14));
        lab.setPadding(new Insets(10, 20, 10, 20));
        lab.setMinWidth(120);
        lab.setBackground(new Background(new BackgroundFill(CARD_BG, new CornerRadii(0), Insets.EMPTY)));
        lab.setBorder(new Border(new BorderStroke(
            backcolor.darker(), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2)
        )));
        return lab;
    }

    public Label makLabel(Color backcolor, Color textColor, double size) {
        Label lab = new Label();
        lab.setBackground(new Background(new BackgroundFill(backcolor, CornerRadii.EMPTY, Insets.EMPTY)));
        lab.setTextFill(textColor);
        lab.setFont(Font.font("Courier New", FontWeight.BOLD, size));
        return lab;
    }
}