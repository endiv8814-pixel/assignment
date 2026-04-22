package assignment1.GUI;

import assignment1.FootprintTracker;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;


public class setOnCloseRequestHandler implements EventHandler<WindowEvent> {
    private FootprintTracker tracker;

    public setOnCloseRequestHandler(FootprintTracker tracker){
        this.tracker = tracker;
    }
    @Override
    public void handle(WindowEvent arg0) {
        PersistenceManager.saveState(tracker);
    }
}