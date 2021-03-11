// package 21-constructivist;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TWENTYONE {
    //The shared mutable data
    private static final List<String> stop_words = new ArrayList<>();
    


    public static List<String> extract_words(String path) throws Exception{
        
        if(path.isEmpty()) return List.of();

        List<String> datas = new ArrayList<>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
            String line;
            while ((line = reader.readLine()) != null) {
                datas.addAll(Arrays.asList(line.toLowerCase().split("[^a-zA-Z0-9]+")));
            }
            reader.close();
        }catch(IOException e){
            System.out.println("I/O error when opening {"+ path +"}: {"+e.getMessage()+"}");
            return List.of();
        }
        
        return datas;
        
    }

    public static List<String> remove_stop_words(List<String> datas){

        if(datas.isEmpty()) return List.of();

        Set<String> stopwords = new HashSet<>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(new File("../stop_words.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                stopwords.addAll(Arrays.asList(line.toLowerCase().split(",")));
            }
            reader.close();
        }catch(IOException e){
            System.out.println("I/O error when opening ../stops_words.txt: {"+e.getMessage()+"}");
            List.of();
        }

        return datas.stream().filter(w -> !stopwords.contains(w) && w.length() > 1).collect(Collectors.toList());

    }

    public static Map<String, Integer> frequencies(List<String> datasNo) {

        if(datasNo.isEmpty()) return Map.of();

        HashMap<String, Integer> freq = new HashMap<>();
        for(String s:datasNo){
            freq.put(s, freq.getOrDefault(s, 0)+1);
        }
        return freq;
    }

    public static Map<String, Integer> sort(Map<String, Integer> freq){
        
        if(freq.isEmpty()) return Map.of();

        freq = freq
            .entrySet()
            .stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        return freq;
    }
    

    public static void main(String[] args)throws Exception{
        
        String filename = args.length >= 1 ? args[0] : "../pride-and-prejudice.txt";
        Map<String, Integer> word_freqs = sort(frequencies(remove_stop_words(extract_words(filename))));
        
        word_freqs.entrySet().stream().limit(25).forEach(w -> System.out.println(w.getKey() + " - " + w.getValue()));

    }
}
