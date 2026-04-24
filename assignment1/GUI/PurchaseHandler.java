package assignment1.GUI;
import assignment1.FootprintTracker;
import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PurchaseHandler implements EventHandler<ActionEvent> {
    private TextField TF;
    private ComboBox<String> cb;
    private TextArea ta;
    private Button purchase;
    private ObservableList<String> ol;
    private Offset offset;
    private FootprintTracker tracker;
    private static double offsetAmount = 0.0;
    public PurchaseHandler(TextField TF, ComboBox<String> cb, TextArea ta, Button purchase, ObservableList<String> ol, Offset offset, FootprintTracker tracker){
        this.TF = TF;
        this.cb = cb;
        this.ta = ta;
        this.purchase = purchase;
        this.ol = ol;
        this.offset = offset;
        this.tracker = tracker;
    }
    public void handle(ActionEvent arg0){
        action();
    }

    public void action(){
            if (TF.getText().isEmpty() || cb.getValue() == null) { // ignore if input is incomplete
                return;
            }
            ta.clear();
            purchase.setDisable(true);

            PauseTransition pause = new PauseTransition(Duration.seconds(2)); // simulate processing delay

            pause.setOnFinished(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event){
                    try{
                    offsetAmount += Double.parseDouble(TF.getText());
                    double cost = (Double.parseDouble(TF.getText())/1000)*15;
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Payment confirmation");
                    alert.setHeaderText(null);
                    alert.setContentText("Payment for: " + cost + "$ Confirmed!");
                    alert.show();
                    String transaction =TransactionHandler.makeReceipt(Double.parseDouble(TF.getText()), String.valueOf(cost));
                    String historyLine = TransactionHandler.makeHistoryLine(Double.parseDouble(TF.getText()), String.valueOf(cost));
                    ta.appendText(transaction);
                    ol.remove("NO_TRANSACTIONS_FOUND");
                    ol.add(historyLine);
                    LoggerUtil.logOffsetPurchased(Double.parseDouble(TF.getText()), cb.getValue());
                    System.out.println(ol);
                    TF.clear();
                    cb.setValue(null);
                    purchase.setDisable(false);
                    offset.updateTotalEmissions(tracker);
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                        purchase.setDisable(false); // re-enable button on failure
                    }
            }
            });
            pause.play();
        }

    public static double getOffsetAmount() {
        return offsetAmount;
    }

}
