package assignment1.GUI;

import assignment1.EmissionSource;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;


public class SearchHandler implements EventHandler<ActionEvent>{
    private ObservableList<EmissionSource> emissions;
    private ObservableList<EmissionSource> searched_emissions;
    private TextField TF3;

    public SearchHandler(ObservableList<EmissionSource> emissions, ObservableList<EmissionSource> searched_emissions, TextField TF3){
        this.emissions = emissions;
        this.searched_emissions = searched_emissions;
        this.TF3 = TF3;
    }

    @Override
    public void handle(ActionEvent arg0) {
        searched_emissions.clear();
            for(EmissionSource item : emissions){
                if (item.getUserName().equals(TF3.getText())){
                    searched_emissions.add(item);
            }
        }
    }

}
