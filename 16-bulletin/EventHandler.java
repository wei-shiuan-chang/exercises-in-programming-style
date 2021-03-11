// package bulletin;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;


/**
 * This class is a superclass for command event handler classes. Subclasses only need to
 * define the <code>execute</code> method for command processing if they handle
 * one event and generate one output event.  
 */
abstract public class EventHandler implements Observer {

    protected Map obj;
    protected int iOutputEvCode;

    public EventHandler(Map obj, int iCommandEvCode, int iOutputEvCode) {
        // Subscribe to command event.
        EventManager.subscribeTo(iCommandEvCode, this);
       
        // Remember the database reference and output event name.
        this.obj = obj;
        this.iOutputEvCode = iOutputEvCode;
    }

    public void update(Observable event, Object param) {
        // Announce a new output event with the execution result.
        EventManager.announce(this.iOutputEvCode, this.execute((Map)param));
    }

    abstract protected Map<String,Object> execute(Map param);
}