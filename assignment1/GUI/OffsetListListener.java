package assignment1.GUI;

import assignment1.EmissionSource;
import assignment1.FootprintTracker;
import javafx.collections.ListChangeListener;

public class OffsetListListener implements ListChangeListener<EmissionSource> {
    private final Offset offset;
    private final FootprintTracker tracker;

    public OffsetListListener(Offset offset, FootprintTracker tracker){
        this.offset = offset;
        this.tracker = tracker;
    }

    @Override
    public void onChanged(ListChangeListener.Change<? extends EmissionSource> change) {
        offset.updateTotalEmissions(tracker);
    }
}
