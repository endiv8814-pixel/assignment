package assignment1.GUI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import assignment1.FootprintTracker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
                    
                    try (Socket socket = new Socket("localhost", 228)){

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
        vbox.getChildren().addAll(totalEm, TF, cb, purchase, requestDiscount, discountLabel, ta, history);
        flowPane.getChildren().add(vbox);
        setContent(flowPane);
    }

    private void displayDiscount(String res) {

        try{
        if (res==null || !res.startsWith("DISCOUNT:")){

            throw new Exception("Empty response");
        }

        String[] blocks = res.split(":");

        if (blocks.length != 3) throw new Exception("Invalid format");

        String p = blocks[1];

        String v = blocks[2];


        discountLabel.setTextFill(Color.GREEN);

        discountLabel.setText("Discount applied: " + p + "% - Your footprint is " + v + "kg CO2");
        

        LoggerUtil.logDiscountAppliet(res);

        
    
        }

        catch (Exception e){

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
