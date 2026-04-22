package assignment1.GUI.ai;

import assignment1.FootprintTracker;
import assignment1.GUI.PersistenceManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainAppAI extends Application {

    private FootprintTracker tracker = new FootprintTracker("GreenPrint-AI");

    @Override
    public void start(Stage primaryStage) throws Exception {

        PersistenceManager.loadState(tracker);

        TabPane tabPane = new TabPane();

        Tab dashboard = new DashboardAI(tracker);
        dashboard.setClosable(false);

        Tab inputTab = new InputTabAI(tracker);
        inputTab.setClosable(false);

        Tab offsetTab = new OffsetTabAI(tracker);
        offsetTab.setClosable(false);

        tabPane.getTabs().addAll(dashboard, inputTab, offsetTab);

        Scene scene = new Scene(tabPane, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("GreenPrint AI Version");

        primaryStage.setOnCloseRequest(e -> {
            PersistenceManager.saveState(tracker);
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
