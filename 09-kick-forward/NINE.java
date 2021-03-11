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


@SuppressWarnings({"unchecked"})
public class NINE{

    //Input: freMap(sorted)
    private static Consumer<Map<String,Integer>> print = freMap -> {
    
        int i = 1;
        for(String key:freMap.keySet()){
            System.out.println(key+" - "+freMap.get(key));
            i++;
            if(i > 25) break;
        }
    
    };

    //Input: freMap, print
    private static BiConsumer<Map<String,Integer>, Consumer> sort = (freMap,func) -> {
    
        freMap = freMap
            .entrySet()
            .stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        func.accept(freMap);
    
    };

    //Input: String[](No Stopwords), sort
    private static BiConsumer<List<String>, BiConsumer> frequencies = (noStop,func) -> {
    
        Map<String,Integer> freMap = new HashMap<>();
        for(String s:noStop){
            freMap.put(s, freMap.getOrDefault(s, 0)+1);
        }
        func.accept(freMap,print);
    
    };

    //Input: String[], frequencies
    private static BiConsumer<String[], BiConsumer> removeStopWords = (strArr,func) -> {
        
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

        func.accept(noStop, sort);


    };

    //Input: data(lowerCase)(String), removeStopWords
    private static BiConsumer<String, BiConsumer> split = (strData,func) -> {

        String[] strArr = strData.split("[^a-zA-Z0-9]+");
        func.accept(strArr,frequencies);

    };

    //Input: data(String), split
    private static BiConsumer<String, BiConsumer> normalize = (strData,func) -> {
        func.accept(strData.toLowerCase(), removeStopWords);
    };


    //Input: pathName, normalize
    private static BiConsumer<String, BiConsumer> readFile = (pathToFile,func) -> {
        
        StringBuilder sb = new StringBuilder();
        String line;
        try{
            BufferedReader reader = new BufferedReader(new FileReader(new File(pathToFile)));
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                sb.append(line);
                sb.append(" ");
            }
            reader.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        func.accept(sb.toString(),split);
        
    };

    public static void main(String[] args){
        
        readFile.accept(args[0], normalize);

    }
}