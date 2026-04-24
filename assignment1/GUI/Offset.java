package assignment1.GUI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import TCP.ConnectionConfig;
import TCP.ResponseParser;
import assignment1.FootprintTracker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class Offset extends Tab{ // tab that allows users to purchase carbon offsets and view transaction history

    private static final Color BG = Color.rgb(2, 6, 4); 
    private static final Color CARD_BG = Color.rgb(6, 18, 12); 
    private static final Color BRIGHT_GREEN = Color.rgb(65, 240, 105); 
    private static final Color DIM_GREEN = Color.rgb(45, 120, 80);
    private static final Color BORDER_GREEN = Color.rgb(30, 75, 50); 
    private static final Color BLUE_BORDER = Color.rgb(40, 140, 220); 

    private ObservableList<String> ol = FXCollections.observableArrayList(); // list of past transactions shown in history view
    private Label totalEm; // label displaying current total emissions, updated reactively
    private Label discountLabel;
    private TextArea ta;
    


    // sets up the offset tab and registers a listener to keep total emissions label
    // tracker: shared footprint tracker instance
    public Offset(FootprintTracker tracker){

        setText("Offset");
        showdata(tracker);
        tracker.getArrayList().addListener(new OffsetListListener(this, tracker)); // auto-refresh label when entries change
    }

    // builds and renders the offset purchase UI
    // tracker: shared footprint tracker instance
    public void showdata(FootprintTracker tracker) {
        ta = new TextArea();
        ta.setEditable(false);
        ta.setPrefRowCount(6);
        ta.setText("SYSTEM_READY");
        ta.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        ta.setBackground(new Background(new BackgroundFill(BG, CornerRadii.EMPTY, Insets.EMPTY)));

        discountLabel = new Label();
        discountLabel.setTextFill(DIM_GREEN);
        discountLabel.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));

        VBox root = new VBox(20);
        root.setPadding(new Insets(40, 60, 40, 60));
        root.setAlignment(Pos.TOP_CENTER);
        root.setBackground(new Background(new BackgroundFill(BG, CornerRadii.EMPTY, Insets.EMPTY)));

        root.getChildren().add(buildEmissionSec(tracker));

        TextField TF = makeTextField("AMOUNT_TO_OFFSET (KG)");

        ComboBox<String> cb = new ComboBox<>();
        cb.getItems().addAll("Credit Card", "Digital Wallet", "Campus Card");
        cb.setValue("Credit Card");
        cb.setMaxWidth(Double.MAX_VALUE);
        cb.setBackground(new Background(new BackgroundFill(CARD_BG, new CornerRadii(2), Insets.EMPTY)));
        cb.setBorder(new Border(new BorderStroke(DIM_GREEN, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(1))));

        Button purchase = makePButton();
        Button requestDiscount = makeRButton(tracker);

        requestDiscount.setOnAction(e -> {
            double total = tracker.getTotalEmissions();
            requestDiscount.setDisable(true);
            discountLabel.setText("CONNECTING_TO_SERVER...");
            discountLabel.setTextFill(BLUE_BORDER);

            Task<String> discount = new Task<>() {
                @Override
                protected String call() throws Exception {
                    ConnectionConfig config = new ConnectionConfig("localhost", 228);
                    try (Socket socket = new Socket(config.getHost(), config.getPort())) {
                        socket.setSoTimeout(5000);
                        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        output.println(total);
                        return input.readLine();
                    }
                }
            };

            discount.setOnSucceeded(i -> {
                displayDiscount(discount.getValue());
                requestDiscount.setDisable(false);
            });

            discount.setOnFailed(i -> {
                ta.setText(">> SYSTEM_ERROR: SERVER_UNREACHABLE");
                discountLabel.setText("CONNECTION_FAILED");
                discountLabel.setTextFill(Color.RED);
                requestDiscount.setDisable(false);
            });

            new Thread(discount).start();
        });

        purchase.setOnAction(new PurchaseHandler(TF, cb, ta, purchase, ol, this, tracker));

        HBox buttons = new HBox(12, purchase, requestDiscount);
        buttons.setAlignment(Pos.CENTER);
        HBox.setHgrow(purchase, Priority.ALWAYS);
        HBox.setHgrow(requestDiscount, Priority.ALWAYS);
        purchase.setMaxWidth(Double.MAX_VALUE);
        requestDiscount.setMaxWidth(Double.MAX_VALUE);

        if (ol.isEmpty()) {
            ol.add("NO_TRANSACTIONS_FOUND");
        }
        ListView<String> history = new ListView<>(ol);
        history.setPrefHeight(250);
        history.setBackground(new Background(new BackgroundFill(BG, CornerRadii.EMPTY, Insets.EMPTY)));

        VBox historyBox = makeBox("OFFSET_TRANSACTION_HISTORY", history);
        VBox discountBox = makeBox("LOGS", ta);

        root.getChildren().addAll(TF, cb, buttons, discountLabel, historyBox, discountBox);

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        scroll.setBackground(new Background(new BackgroundFill(BG, CornerRadii.EMPTY, Insets.EMPTY)));
        scroll.setBorder(Border.EMPTY);

        setContent(scroll);
    }

    private void displayDiscount(String res) {
        try {
            HashMap<Integer, String> map = ResponseParser.parse(res);

            discountLabel.setTextFill(Color.GREEN);
            discountLabel.setText(">> DISCOUNT_APPLIED: " + map.get(1) + "%\n>> NEW FOOTPRINT: " + map.get(2) + "kg CO2");

            LoggerUtil.logDiscountAppliet(res);

        } catch (Exception e) {
            discountLabel.setText(">> ERROR: INVALID_SERVER_RESPONSE");
        }
    }

    public void updateTotalEmissions(FootprintTracker tracker){
        totalEm.setText(String.valueOf(tracker.getTotalEmissions()));

    }

    
    public Button makePButton(){
        Button lab = new Button("PURCHASE_CARBON_CREDITS");
        lab.setFont(Font.font("Courier New", FontWeight.BOLD, 15));
        lab.setTextFill(BRIGHT_GREEN);
        lab.setPadding(new Insets(16, 20, 16, 20));

        BackgroundFill bf = new BackgroundFill(CARD_BG, new CornerRadii(2), Insets.EMPTY);
        Background bg = new Background(bf);
        lab.setBackground(bg);
        
        lab.setBorder(new Border(new BorderStroke(BRIGHT_GREEN, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));

        
        return lab;
    }

    public Button makeRButton(FootprintTracker tracker){
        Button lab = new Button(">> EQUEST_DISCOUNT");
        lab.setFont(Font.font("Courier New", FontWeight.BOLD, 15));
        lab.setTextFill(BLUE_BORDER);
        lab.setPadding(new Insets(16, 20, 16, 20));

        BackgroundFill bf = new BackgroundFill(CARD_BG, new CornerRadii(2), Insets.EMPTY);
        Background bg = new Background(bf);
        lab.setBackground(bg);
        
        lab.setBorder(new Border(new BorderStroke(BLUE_BORDER, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));

        lab.setDisable(tracker.getTotalEmissions()<=0);
        
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



    private VBox buildEmissionSec(FootprintTracker tracker){


        VBox sec = new VBox(6);

        sec.setAlignment(Pos.CENTER);

        HBox total = new HBox(12);

        total.setAlignment(Pos.CENTER);

        totalEm = new Label(String.valueOf(tracker.getTotalEmissions()) +" KG_CO2");

        totalEm.setFont(Font.font("Courier New", FontWeight.BOLD, 70));

        totalEm.setTextFill(BRIGHT_GREEN);

        total.getChildren().add(totalEm);

        Label sub = new Label("AWAITING_OFFSET_COMPENSATION");
        sub.setFont(Font.font("Courier New", FontWeight.NORMAL, 14));
        sub.setTextFill(DIM_GREEN);

        sec.getChildren().addAll(total, sub);

        return sec;



    }

    public TextField makeTextField(String s){
        TextField tf = new TextField();

        tf.setPromptText(s);

        tf.setFont(Font.font("Courier New", FontWeight.BOLD, 18));
        tf.setAlignment(Pos.CENTER);

        tf.setPadding(new Insets(18, 20, 18, 20));

        tf.setBackground(new Background(new BackgroundFill(CARD_BG, new CornerRadii(2), Insets.EMPTY)));

        tf.setBorder(new Border(new BorderStroke(BRIGHT_GREEN, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(2))));

        tf.setMaxWidth(Double.MAX_VALUE);

        return tf;

    }

    public VBox makeBox(String s, javafx.scene.Node content) {
        VBox b = new VBox(10);
        b.setPadding(new Insets(15));
        b.setBackground(new Background(new BackgroundFill(BG, CornerRadii.EMPTY, Insets.EMPTY)));
        b.setBorder(new Border(new BorderStroke(BORDER_GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

        Label title = new Label(s);
        title.setFont(Font.font("Courier New", FontWeight.BOLD, 12));
        title.setTextFill(DIM_GREEN);

        b.getChildren().addAll(title, content);
        return b;
    }

    
    
    
}
