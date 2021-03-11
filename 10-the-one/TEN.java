import java.util.function.Function;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class TEN {
    private static class TheOne{

        Object value;
        private TheOne(Object obj){
            this.value = obj;
        }

        private TheOne bind(Function func){
            this.value = func.apply(value);
                return this;
        }

        private void print() {
            System.out.println(value);
        }
    }

    private static Function<String,String> readFile = path -> {

        StringBuilder sb = new StringBuilder();
        String line;
        try{
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                sb.append(line);
                sb.append(" ");
            }
            reader.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return sb.toString();
        
    };

    private static Function<String,String> normalize = str -> {
        return str.toLowerCase();
    };

    private static Function<String,String[]> scan = str -> {
        return str.split("[^a-zA-Z0-9]+");
    };

    private static Function<String[],List<String>> removeStopWords = strArr -> {
        //Read Stop
        Set<String> stopWords = new HashSet<>();
            
        String str = "";
        try {
            byte[] encoded = Files.readAllBytes(Paths.get("../stop_words.txt"));
            str = new String(encoded);
        } catch (Exception e) {
            System.out.println("Error reading stop_words");
        }
        String[] words = str.split(",");
        
        for(String s:words) {
            stopWords.add(s);
        }

        List<String> noStop = new ArrayList<>();
        for(String s:strArr){
            if(!stopWords.contains(s) && s.length()>1) noStop.add(s);
        }
        return noStop;
    };


    private static Function<List<String>,Map<String,Integer>> frequency = strArr -> {
    
        Map<String,Integer> freMap = new HashMap<>();
        for(String s:strArr){
            freMap.put(s, freMap.getOrDefault(s, 0)+1);
        }
        
        return freMap;

    };

    private static Function<Map<String,Integer>,Map<String,Integer>> sort = freMap -> {

        freMap = freMap
            .entrySet()
            .stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
        return freMap;

    };

    private static Function<Map<String,Integer>,String> print = freMapSorted -> {

        StringBuilder out = new StringBuilder();
        int i = 1;
        for(String key:freMapSorted.keySet()){
            out.append(key+" - "+freMapSorted.get(key)+"\n");
            i++;
            if(i > 25) break;
        }
        return out.toString();
    };

    public static void main(String[] args){
        TheOne theOne = new TheOne(args[0]);
        theOne.bind(readFile).bind(normalize).bind(scan).bind(removeStopWords).bind(frequency).bind(sort).bind(print).print();
    }
    
}
