import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class THIRTEEN{
    @SuppressWarnings({"unchecked"})
    public static void main(String[] args){
        Map<String,Object> map = new HashMap<>();

        Function<String,List<String>> readFile = path -> {
            String[] datas;
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
            
            datas = sb.toString().toLowerCase().split("[^a-zA-Z0-9]+");
            List<String> dataList = new ArrayList<>();
            map.put("data", dataList.addAll(Arrays.asList(datas)));
            return dataList;
            
        };

        map.put("stop", new HashSet<String>());
        Supplier<Void> readStop = () -> {
            Set<String> stopWords = (Set<String>) map.get("stop");
            
            String str = "";
            try {
                byte[] encoded = Files.readAllBytes(Paths.get("../stop_words.txt"));
                str = new String(encoded);
            } catch (IOException e) {
                System.out.println("Error reading stop_words");
            }
            String[] words = str.split(",");
            
            for(String s:words) {
                stopWords.add(s);
            }

            return null;
        };

        Predicate<String> isStopWord = word -> {
            
            
            Object tempSet = map.get("stop");





            return ((Set<String>)tempSet).contains(word);
        };
        
        
        //map.put("fre", null);
        //Map<String, Integer> fre = new HashMap<>();
        map.put("fre", new HashMap<String, Integer>());

        Consumer<String> incrementFre = word -> {

            if(isStopWord.negate().test(word) && word.length() > 1) {
                Map<String, Integer> fre = (Map<String, Integer>) map.get("fre");
                fre.put(word,fre.getOrDefault(word,0)+1);
                map.put("fre",map.get("fre"));
            }
        };      /////////////////////////////

        




        Consumer<Map<String, Integer>> top25 = freMap -> {
            
            freMap = freMap
            .entrySet()
            .stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

            int i = 1;
            for(String key:freMap.keySet()){
                System.out.println(key+" - "+freMap.get(key));
                i++;
                if(i > 25) break;
            }

        };

        // Initial
        readStop.get();
        // Iterate all non-stop words to get frequency
        for(String word:readFile.apply(args[0])){
            incrementFre.accept(word);
        }
        // Sort and Print
        top25.accept((Map<String, Integer>) map.get("fre"));


    }
}
