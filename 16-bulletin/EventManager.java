// package bulletin;
import java.util.Observable;
import java.util.Observer;

// package 16-bulletin-board;

public class EventManager {

    
    public static class Event extends Observable {

        /**
         * Set the changed flag of this event.
         */
        public void setChanged() {
            super.setChanged();
        }
    }
    // Read files
    static final int DATA_STORAGE = 0;
    static final int WORD_FREQUENCY_COUNTER = 1;
    static final int TOP_25 = 2;
    static final int WORD_WITH_Z = 3;

    public static final int MAX_NUM_EVENTS  = 5;

    
    protected static Event[] aEvent = new Event[MAX_NUM_EVENTS];

    /**
     * Initialize the event bus. Existing subscriptions (event/receiver mappings) are cleared.
     */
    public static void initialize() {
        for (int i=0; i<MAX_NUM_EVENTS; i++) {
            aEvent[i] = new Event();
        }
    }

    public static void subscribeTo(int iEventCode, Observer objSubscriber) {
        aEvent[iEventCode].addObserver(objSubscriber);
    }

    public static void announce(int iEventCode, Object sEventParam) {
        aEvent[iEventCode].setChanged();
        aEvent[iEventCode].notifyObservers(sEventParam);
    }

}
