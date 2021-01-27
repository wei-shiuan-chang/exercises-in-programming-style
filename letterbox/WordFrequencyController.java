
package letterbox;

import java.util.HashMap;
import java.util.Map;

public class WordFrequencyController {

    private String[] words;

    
    //private static String[] storageManager;
    WordFrequencyController(){
    }


    public void dispatch(String[] message){
        if(message[0].equals("init")) 
            init(message[1]);
        else if(message[0].equals("run"))
            run();
        else
            System.out.println("Message not understood " + message[0]);
    }

    public void init(String path){
        
        words = DataStorageManager.dispatch(new String[] {"init",path});

        StopWordManager.dispatch(new String[] {"init"});

    }

    private void run(){
        for(String word:words){
            if(StopWordManager.dispatch(new String[] {"is_stop_word",word}).equals("false")){
                WordFrequencyManager.dispatch(new String[] {"increment_count",word});
            }
        }
        
        Map<String, Integer> freq = new HashMap<>();
        freq = WordFrequencyManager.dispatch(new String[] {"sorted"});
        int i = 1;
        for(String key:freq.keySet()){
            System.out.println(key+" - "+freq.get(key));
            i++;
            if(i > 25) break;
        }

        

    }
}
