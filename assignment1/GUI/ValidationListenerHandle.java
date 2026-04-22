package assignment1.GUI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class ValidationListenerHandle implements ChangeListener<String> {
    //Handler for validation Label
    //Takes Label as a parameter and does not return anything
    Label label = new Label();
    public ValidationListenerHandle(Label label){
        this.label = label;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable,
                        String oldValue,
                        String newValue) {
        action(label, newValue);
    }

    public void action(Label Validation, String newValue){
        {
            boolean bool = Validator(newValue, "^[A-Z]-[0-9]{3}$");
            if(bool){
                Validation.setText("✓ Valid");
                Validation.setTextFill(Color.GREEN);
            }
            else{
                Validation.setText("✗ Invalid");
                Validation.setTextFill(Color.RED);
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