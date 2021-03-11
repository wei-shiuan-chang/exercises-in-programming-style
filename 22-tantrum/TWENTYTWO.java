// package 21-constructivist;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TWENTYTWO {
    //The shared mutable data
    private static final List<String> stop_words = new ArrayList<>();
    


    public static List<String> extract_words(String path) throws IOException{
        
        assert path instanceof String : "I need a string!";
        assert !path.isEmpty() : "I need a non-empty string!";

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
            throw e;
        }
        
        return datas;
        
    }

    public static List<String> remove_stop_words(List<String> datas) throws IOException{

        assert datas instanceof List : "I need a list!";
        assert !datas.isEmpty() : "I need a non-empty list!";

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
            throw e;
        }

        return datas.stream().filter(w -> !stopwords.contains(w) && w.length() > 1).collect(Collectors.toList());

    }

    public static Map<String, Integer> frequencies(List<String> datasNo) {

        assert datasNo instanceof List : "I need a list!";
        assert !datasNo.isEmpty() : "I need a non-empty list!";

        HashMap<String, Integer> freq = new HashMap<>();
        for(String s:datasNo){
            freq.put(s, freq.getOrDefault(s, 0)+1);
        }
        return freq;
    }

    public static Map<String, Integer> sort(Map<String, Integer> freq){
        
        assert freq instanceof List : "I need a map!";
        assert !freq.isEmpty() : "I need a non-empty map!";

        try{
            return freq
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(25)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
        }catch(Exception e){
            System.out.println("Sorted threw: " + e.getMessage());
            throw e;
        }
    }

    

    

    public static void main(String[] args)throws Exception{
        try{
            assert args.length >= 1 : "You idiot! I need an input file!";
            Map<String, Integer> word_freqs = sort(frequencies(remove_stop_words(extract_words("../pride-and-prejudice.txt"))));
            assert word_freqs instanceof Map : "OMG! This is not a map!";
            assert word_freqs.size() == 25 : "SRSLY? Less than 25 words!";
            word_freqs.entrySet().stream().forEach(w -> System.out.println(w.getKey() + " - " + w.getValue()));
        }catch(Exception e){
            System.out.println("Something wrong: " + e.getMessage());
            e.printStackTrace();
        }



    }
}
