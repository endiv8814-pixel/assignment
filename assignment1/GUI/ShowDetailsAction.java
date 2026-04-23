package assignment1.GUI;

import assignment1.EmissionSource;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ShowDetailsAction implements EventHandler<ActionEvent> {
    private GridPane grid;
    private EmissionSource source;
    public ShowDetailsAction(GridPane pane, EmissionSource source){
        this.grid=pane;
        this.source=source;
    }
    @Override
    public void handle(ActionEvent arg0) {

        Label title = makeLabel(Color.rgb(2, 8, 2), Color.rgb(57, 255, 20));
        title.setText("Details for Entry: " + source.getSourceID());
        Label details = makeLabel(Color.rgb(2, 8, 2), Color.rgb(57, 255, 20));
        details.setText(source.toString());

        grid.getChildren().clear();
        grid.add(title, 0, 0);
        grid.add(details, 0, 1);
    }

    public Label makeLabel(Color backcolor, Color textColor){

        Label lab = new Label();
        BackgroundFill bf = new BackgroundFill(backcolor, CornerRadii.EMPTY, Insets.EMPTY);
        Background bg = new Background(bf);

        lab.setBackground(bg);
        lab.setTextFill(textColor);
        lab.setFont(Font.font("Courier New",FontWeight.BOLD, 20));
        return lab;
    }
}
