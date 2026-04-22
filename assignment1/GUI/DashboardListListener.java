package assignment1.GUI;

import assignment1.EmissionSource;
import assignment1.FootprintTracker;
import javafx.collections.ListChangeListener;

public class DashboardListListener implements ListChangeListener<EmissionSource> {
    private Dashboard dashboard;
    private FootprintTracker tracker;

    public DashboardListListener(Dashboard dashboard, FootprintTracker tracker){
        this.dashboard = dashboard;
        this.tracker = tracker;
    }

    @Override
    public void onChanged(ListChangeListener.Change<? extends EmissionSource> change) {
        dashboard.showdata(tracker);
    }
}
