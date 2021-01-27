import java.util.List;

package letterbox;

public class TWELVE {
    
    static void main(String[] args){
        WordFrequencyController wfcontroller = new WordFrequencyController();
        wfcontroller.dispatch(new String[] {"init",args[1]});
        wfcontroller.dispatch(new String[] {"run"});
    }


}
