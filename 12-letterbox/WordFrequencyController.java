
//package week3;

import java.util.HashMap;
import java.util.Map;

public class WordFrequencyController {

    private String[] words;

    
    //private static String[] storageManager;
    WordFrequencyController(){}


    public void dispatch(String[] message)throws Exception{
        if(message[0].equals("init")) 
            init(message[1]);
        else if(message[0].equals("run"))
            run();
        else
            System.out.println("Message not understood " + message[0]);
    }

    public void init(String path)throws Exception{
        
        words = DataStorageManager.dispatch(new String[] {"init",path});
        StopWordManager.dispatch(new String[] {"init"});

    }

    private void run(){
        //System.out.println(words.length);
        WordFrequencyManager wfm = new WordFrequencyManager();
        for(String word:words){
            if(StopWordManager.dispatch(new String[] {"is_stop_word",word}).equals("false") && word.length() > 1){
                wfm.dispatch(new String[] {"increment_count",word});
            }
        }
        wfm.dispatch(new String[] {"sorted"});
        // Map<String, Integer> freq = new HashMap<>();
        // freq = WordFrequencyManager.dispatch(new String[] {"sorted"});
        // System.out.println(freq.size());
        

        

    }
}
