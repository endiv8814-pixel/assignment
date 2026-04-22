package assignment1.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ComboBoxHandler implements EventHandler<ActionEvent>{
    //ComboBox handler
    //Takes two comboBoxes, a TextField and VBox as parameters
    //Does not return anything
    private ComboBox<String> comboBox2;
    private ComboBox<String> comboBox;
    private TextField TFT;
    private VBox vbox;
    public ComboBoxHandler(ComboBox<String> comboBox2, ComboBox<String> comboBox, TextField TFT, VBox vbox){
        this.comboBox2 = comboBox2;
        this.comboBox = comboBox;
        this.TFT = TFT;
        this.vbox = vbox;
    }

    @Override
    public void handle(ActionEvent arg0) {
        action();
    }

    public void action(){
        //Depending on the choice, sets up the VBox
        if(comboBox.getValue() == "Transportation"){
                System.out.println("WORKED");
                vbox.getChildren().removeAll(TFT, comboBox2);
                ObservableList<String> items2 = FXCollections.observableArrayList("Car", "Bus", "Train", "Bicycle");
                comboBox2.setItems(items2);
                TFT.setPromptText("Enter distance in km...");
                vbox.getChildren().add(TFT);
                vbox.getChildren().add(comboBox2);
            }
            if(comboBox.getValue() == "Energy"){
                vbox.getChildren().removeAll(TFT, comboBox2);
                ObservableList<String> items2 = FXCollections.observableArrayList("Grid", "Solar", "Wind");
                comboBox2.setItems(items2);
                TFT.setPromptText("Enter kWh...");
                vbox.getChildren().add(TFT);
                vbox.getChildren().add(comboBox2);
            }
            if(comboBox.getValue() == "Food"){
                vbox.getChildren().removeAll(TFT, comboBox2);
                ObservableList<String> items2 = FXCollections.observableArrayList("Vegan", "Vegetarian", "Poultry", "Beef");
                comboBox2.setItems(items2);
                TFT.setPromptText("Enter number of meals...");
                vbox.getChildren().add(comboBox2);
                vbox.getChildren().add(TFT);
            }
    }
}
