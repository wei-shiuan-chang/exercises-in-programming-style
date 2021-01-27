import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jdk.tools.jlink.resources.plugins;

package letterbox;

public class StopWordManager {

    private static List<String> datasNo;

    StopWordManager(){
        datasNo = new ArrayList<String>();

    }

    public static String dispatch(String[] message){
        if(message[0].equals("init")){
            init();
            return "init";
        }else if(message[0].equals("is_stop_word"))
            return isStopWord(message[1]);
        else{
            System.out.println("Message not understood " + message[0]);
        }
        return "error";
    }


    //read all stop words
    private static void init(){
        
    
        String str = "";
        try {
          byte[] encoded = Files.readAllBytes(Paths.get("../stop_words.txt"));
          str = new String(encoded);
        } catch (IOException e) {
          System.out.println("Error reading stop_words");
        }
        String[] words = str.split(",");
        datasNo.addAll(Arrays.asList(words));
        
        
    }

    private static String isStopWord(String word){
        if(datasNo.contains(word)) return "true";
        else return "false";
    }



}
