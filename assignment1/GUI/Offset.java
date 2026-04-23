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
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class Offset extends Tab{ // tab that allows users to purchase carbon offsets and view transaction history

    private static final Color BG = Color.rgb(2,   8,  2);
    private static final Color HEADER_BG = Color.rgb(2,  18,  2);
    private static final Color CARD_BG = Color.rgb(8,  22,  8);
    private static final Color CARD_BG_RED = Color.rgb(28,  4,  4);
    private static final Color BRIGHT_GREEN = Color.rgb(57, 255, 20);
    private static final Color DIM_GREEN = Color.rgb(35, 110, 35);
    private static final Color BORDER_GREEN = Color.rgb(20,  70, 20);
    private static final Color RED = Color.rgb(220,  20, 60);
    private static final Color YELLOW = Color.rgb(200, 180,  0);


    private ObservableList<String> ol = FXCollections.observableArrayList(); // list of past transactions shown in history view
    private Label totalEm; // label displaying current total emissions, updated reactively
    Label discountLabel = makLabel(null, Color.BLACK);


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

        FlowPane flowPane = new FlowPane(10,10);
        VBox root = new VBox(24);

        root.setPadding(new Insets(40, 60, 40, 60));

        root.setAlignment(Pos.CENTER);

        root.setBackground(new Background(new BackgroundFill(BG, CornerRadii.EMPTY, Insets.EMPTY)));
        HBox h = new HBox(12);
        
        h.setAlignment(Pos.CENTER);

        totalEm = new Label(String.valueOf(tracker.getTotalEmissions())+ " KG_CO2");

        totalEm.setFont(Font.font("Courier New", FontWeight.BOLD, 72));
        totalEm.setTextFill(BRIGHT_GREEN);

        h.getChildren().add(totalEm);
    
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


        // DISCOUNT LOGIC

        Button requestDiscount = makButton(null, Color.BLACK);

        requestDiscount.setText("Request Discount");

        requestDiscount.setDisable(tracker.getTotalEmissions()<=0);


        requestDiscount.setOnAction(e ->{

            double total = tracker.getTotalEmissions();

            requestDiscount.setDisable(true);

            discountLabel.setText("Connecting to Server...");


            discountLabel.setTextFill(Color.BLUE);

            
            Task<String> discount = new Task<>() {

                @Override
                protected String call() throws Exception {
                    ConnectionConfig config = new ConnectionConfig("localhost", 228);
                    try (Socket socket = new Socket(config.getHost(), config.getPort())){

                        socket.setSoTimeout(5000);
                        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                        
                        output.println(total);

                        return input.readLine();
                    }

                }
                
                
            };

            discount.setOnSucceeded(i ->{

                displayDiscount(discount.getValue());

                requestDiscount.setDisable(false);



            });

            discount.setOnFailed(i ->{


                discountLabel.setText("Could not reach server. Please try again later.");

                discountLabel.setTextFill(Color.RED);

                requestDiscount.setDisable(false);
            });

            new Thread(discount).start();

            



        });
        purchase.setOnAction(new PurchaseHandler(TF, cb, ta, purchase, ol));
        root.getChildren().addAll(h, TF, cb, purchase, requestDiscount, discountLabel, ta, history);
        flowPane.getChildren().add(root );
        setContent(flowPane);
    }

    private void displayDiscount(String res) {
        try {
            HashMap<Integer, String> map = ResponseParser.parse(res);

            discountLabel.setTextFill(Color.GREEN);
            discountLabel.setText("Discount applied: " + map.get(1) + "% - Your footprint is " + map.get(2) + "kg CO2");

            LoggerUtil.logDiscountAppliet(res);

        } catch (Exception e) {
            discountLabel.setText("Something went wrong. Try again");
        }
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
