package assignment1.GUI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import assignment1.EmissionSource;
import assignment1.EnergyEmission;
import assignment1.FoodEmission;
import assignment1.FootprintTracker;
import assignment1.TransportationEmission;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class ButtonHandler implements EventHandler<ActionEvent>{
    //handles the button
    //takes all the inputs from input tab, aka TextFields, ComboBoxes, Label and DaePicker and a FootprintTracker
    //Checks all of them for validity and of everything is okay submits the record to tracker
    //Returns nothing
    private FootprintTracker tracker;
    private TextField TF;
    private TextField TFT;
    private TextField TF2;
    private ComboBox<String> comboBox;
    private ComboBox<String> comboBox2;
    private Label ReadyOrNot;
    private DatePicker datePicker;

    public ButtonHandler(FootprintTracker tracker, TextField TF, TextField TFT, TextField TF2, ComboBox<String> comboBox, ComboBox<String> comboBox2, Label ReadyOrNot, DatePicker datePicker){
        this.tracker = tracker;
        this.TF = TF;
        this.TFT = TFT;
        this.TF2 = TF2;
        this.comboBox = comboBox;
        this.comboBox2 = comboBox2;
        this.ReadyOrNot = ReadyOrNot;
        this.datePicker = datePicker;
    }

    public void handle(ActionEvent arg0){
        action();
    }

    public void action(){
        {
            boolean SourceID = Validator(TF.getText(), "^[A-Z]-[0-9]{3}$");
            boolean SourceIDRepeater = true;
            for(EmissionSource item : tracker.getArrayList()){
                if (item.getSourceID().equals(TF.getText())){
                    SourceIDRepeater = false;
                }
            }
            boolean TFT_Check = Validator(TFT.getText(), "^[0-9]+$");
            boolean Combo_Check = comboBox2.getValue() != null;
            boolean user_name = Validator(TF2.getText(), "^[A-Za-z]+$");
            boolean date = datePicker.getValue() != null;
            boolean before_date = datePicker.getValue().isBefore(java.time.LocalDate.now());
            if (SourceID && SourceIDRepeater && TFT_Check && Combo_Check && user_name && date && before_date){
                ReadyOrNot.setText("Submitted");
                if(comboBox.getValue().equals("Transportation")){
                    TransportationEmission TE = new TransportationEmission(TF.getText(), comboBox.getValue(), datePicker.getValue().toString(), TF2.getText(), Double.parseDouble(TFT.getText()), comboBox2.getValue());
                    tracker.addEntry(TE);
                    LoggerUtil.logEntryAdded(TF.getText(), TF2.getText(), TE.calculateEmission());
                }
                if(comboBox.getValue().equals("Energy")){
                    EnergyEmission EE = new EnergyEmission(TF.getText(), comboBox.getValue(), datePicker.getValue().toString(), TF2.getText(), Double.parseDouble(TFT.getText()), comboBox2.getValue());
                    tracker.addEntry(EE);
                    LoggerUtil.logEntryAdded(TF.getText(), TF2.getText(), EE.calculateEmission());
                }
                if(comboBox.getValue().equals("Food")){
                    FoodEmission FE = new FoodEmission(TF.getText(), comboBox.getValue(), datePicker.getValue().toString(), TF2.getText(), comboBox2.getValue(), Double.parseDouble(TFT.getText()));
                    tracker.addEntry(FE);
                    LoggerUtil.logEntryAdded(TF.getText(), TF2.getText(), FE.calculateEmission());
                }
            }
            else if(!SourceID){
                ReadyOrNot.setText("The ID is not valid!");
            }
            else if(!SourceIDRepeater){
                ReadyOrNot.setText("The ID is already in use!");
            }
            else if(!TFT_Check){
                ReadyOrNot.setText("The quantity is not valid!");
            }
            else if(!Combo_Check){
                ReadyOrNot.setText("Please select a category!");
            }
            else if(!user_name){
                ReadyOrNot.setText("The user name is not valid!");
            }
            else if(!date){
                ReadyOrNot.setText("Please select a date!");
            }
            else if(!before_date){
                ReadyOrNot.setText("Please select a date in the past!");
            }
        }
    }
    //Utility class for validating if a string has certain regex pattern
    //Takes String for text and String for regex as arguments
    //Returns True or False depending if text has regex pattern
    public boolean Validator(String text, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

}
