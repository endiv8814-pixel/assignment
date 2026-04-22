package assignment1.GUI;





import assignment1.FootprintTracker;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainApp extends Application{ // entry point of the JavaFX application, sets up the main window and tabs
    
    private FootprintTracker tracker = new FootprintTracker("Lalala"); // tracker instance passed to all tabs

    // initializes and displays the primary stage (arg0)
    @Override
    public void start(Stage arg0) throws Exception {
        PersistenceManager.loadState(tracker);
        TabPane tabPane = new TabPane();
        
        Tab dashboardTab = new Dashboard(tracker);
        Tab inputTab = new InputTab(tracker);
        Tab offsetTab = new Offset(tracker);

        tabPane.getTabs().add(dashboardTab);
        tabPane.getTabs().add(inputTab);
        tabPane.getTabs().add(offsetTab);

        Scene scene = new Scene(tabPane, 1000,777);

        arg0.setScene(scene);
        arg0.setTitle("GreenPrint");
        arg0.setResizable(false);
        arg0.setOnCloseRequest(new setOnCloseRequestHandler(tracker)); // saves state when window is closed
        arg0.show();
    }

    // launches the JavaFX application
    public static void main(String[] args){
        launch(args);
    }

    
}
