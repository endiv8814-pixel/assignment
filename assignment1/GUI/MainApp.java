package assignment1.GUI;

import assignment1.FootprintTracker;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainApp extends Application {

    static final Color BG            = Color.rgb(2,  8,  2);
    static final Color HEADER_BG     = Color.rgb(2, 18,  2);
    static final Color BRIGHT_GREEN  = Color.rgb(57, 255, 20);
    static final Color DIM_GREEN     = Color.rgb(30, 100, 30);
    static final Color BORDER_GREEN  = Color.rgb(20,  70, 20);
    static final Color LINE_COLOR    = Color.rgb(0, 0, 0, 0.18);

    private final FootprintTracker tracker = new FootprintTracker("Lalala");

    @Override
    public void start(Stage stage) throws Exception {
        PersistenceManager.loadState(tracker);

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
            new Dashboard(tracker),
            new InputTab(tracker),
            new Offset(tracker)
        );
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setBackground(new Background(new BackgroundFill(BG, CornerRadii.EMPTY, Insets.EMPTY)));
        tabPane.setBorder(Border.EMPTY);

        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(BG, CornerRadii.EMPTY, Insets.EMPTY)));
        
        root.setCenter(tabPane);
        root.setBottom(buildFooter());

        StackPane stackRoot = new StackPane(root, makeScanlines());
        stackRoot.setBackground(new Background(new BackgroundFill(BG, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(stackRoot, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("VICTORIAPRINT_V3.0");
        stage.setResizable(true);
        stage.setOnCloseRequest(new setOnCloseRequestHandler(tracker));
        stage.show();
    }

    private HBox buildFooter() {
        HBox footer = new HBox(28);
        footer.setPadding(new Insets(9, 24, 9, 24));
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setBackground(new Background(new BackgroundFill(HEADER_BG, CornerRadii.EMPTY, Insets.EMPTY)));
        footer.setBorder(new Border(new BorderStroke(
            BORDER_GREEN, BorderStrokeStyle.SOLID,
            CornerRadii.EMPTY, new BorderWidths(1, 0, 0, 0)
        )));

        Label online   = footerLabel("SYSTEM: ONLINE", BRIGHT_GREEN);
        Label location = footerLabel("LOCATION: VICTORIA_SERVER_228", DIM_GREEN);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label cpu = footerLabel("CPU: 14%",   DIM_GREEN);
        Label mem = footerLabel("MEM: 8.8GB", DIM_GREEN);

        footer.getChildren().addAll(online, location, spacer, cpu, mem);
        return footer;
    }

    private Label footerLabel(String text, Color color) {
        Label l = new Label(text);
        l.setFont(Font.font("Courier New", FontWeight.NORMAL, 12));
        l.setTextFill(color);
        return l;
    }

    private Rectangle makeScanlines() {
        Rectangle rect = new Rectangle(2000, 2000);
        rect.setFill(new LinearGradient(
            0, 0, 0, 4, false, CycleMethod.REPEAT,
            new Stop(0.00, LINE_COLOR),
            new Stop(0.49, LINE_COLOR),
            new Stop(0.50, Color.TRANSPARENT),
            new Stop(1.00, Color.TRANSPARENT)
        ));
        rect.setMouseTransparent(true);
        return rect;
    }

    public static void main(String[] args) {
        launch(args);
    }
}