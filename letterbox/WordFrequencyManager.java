
package letterbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WordFrequencyManager {
    
    private static HashMap<String, Integer> freq;

    WordFrequencyManager(){
        List<String> datasNo = new ArrayList<>();
    }

    public static Map<String, Integer> dispatch(String[] message){
        if(message[0].equals("increment_count")) 
            incrementCount(message[1]);
        else if(message[0].equals("sorted"))
            sorted();
        else
            System.out.println("Message not understood " + message[0]);
        return freq;
    }


    private static void incrementCount(String word) {
        freq = new HashMap<>();
        freq.put(word, freq.getOrDefault(word, 0)+1);
    }

    private static Map<String, Integer> sorted(){
        freq = freq
        .entrySet()
        .stream()
        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        return freq;
    }
}