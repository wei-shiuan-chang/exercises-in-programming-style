import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TWENTYEIGHT {
    static int count;
    public static void main(String[] args) {
        CountAndSort countAndSort = new CountAndSort(new NonStopWords(new AllWords(args[0])));
        while(countAndSort.hasNext()){
            System.out.println("-----------------------------");
            print(countAndSort.next());
        }
    }

    static class AllWords implements Iterator<String> {

        private String line;
        private BufferedReader reader;
        private List<String> wordsOfLine;
        private int index;
        // int c = 0;
        // int l = 0;

        AllWords(String filePath) {
            try {
                this.reader = new BufferedReader(new FileReader(new File(filePath)));
                this.wordsOfLine = nextLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public List<String> nextLine() throws Exception{
            if ((line = reader.readLine()) != null) {
                // l++;
                return Arrays.asList(line.toLowerCase().split("[^a-zA-Z0-9]+"));
            }
            return null;
        }


        @Override
        public boolean hasNext() {
            if(wordsOfLine != null && wordsOfLine.size() != index) return true;
            try{
                while((wordsOfLine = nextLine()) != null){
                    if(!wordsOfLine.isEmpty()){
                        index = 0;
                        return true;
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            
            return false;
        }

        @Override
        public String next() {

            String next;
            if(hasNext()){
                next = wordsOfLine.get(index);
                index++;
                // c++;
                // System.out.println(next);
                return next;
            }else{
                // System.out.println("words.size: "+c+", line: "+l);
                return null;
            }

        }
        
    }

    static class NonStopWords implements Iterator<String> {

        private Iterator<String> allWords;
        private Set<String> stopWords;
        private String next;
        private boolean ready;
        private List<String> nonStopWords;
        private int index = 0;

        
        NonStopWords(Iterator<String> allWords) {
            try {
                this.allWords = allWords;
                this.stopWords = stopWords();
                this.nonStopWords = nonStopWords();
                this.ready = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Set<String> stopWords(){
            Set<String> stopwords = new HashSet<>();
            String str = "";
            try {
                byte[] encoded = Files.readAllBytes(Paths.get("../stop_words.txt"));
                str = new String(encoded);
            } catch (IOException e) {
                System.out.println("Error reading stop_words");
            }
            String[] words = str.split(",");
            stopwords.addAll(Arrays.asList(words));
            // System.out.println("stopword num = "+stopwords.size());
            return stopwords;
        }

        public List<String> nonStopWords(){
            nonStopWords = new ArrayList<>();
            while(allWords.hasNext()){
                next = allWords.next();
                if(!stopWords.contains(next) && next.length() > 1){
                    nonStopWords.add(next);
                }
            }
            return nonStopWords;
        }


        @Override
        public boolean hasNext() {
            return !nonStopWords.isEmpty() && index != nonStopWords.size();
        }

        @Override
        public String next() {

            if(hasNext()){
                return nonStopWords.get(index++);
            }else{
                return null;
            }

        }
        
    }

    static class CountAndSort implements Iterator<Map<String,Integer>> {

        private Iterator<String> nonStopWords;
        private Map<String,Integer> freq;
        private int index;

        CountAndSort(Iterator<String> nonStopWords){
            this.nonStopWords = nonStopWords;
            this.freq = new HashMap<>();
            this.index = 1;
        }

        public Map<String, Integer> sort(Map<String, Integer> freMap) {
             
            freMap = freMap
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
            return freMap;
        }

        @Override
        public boolean hasNext() {
            return nonStopWords.hasNext();
        }

        @Override
        public Map<String,Integer> next() {
            while(index % 5000 != 0){
                if(hasNext()){
                    String word = nonStopWords.next();
                    freq.put(word, freq.getOrDefault(word, 0)+1);
                }else{
                    return sort(freq);
                }
                index++;
            }
            index = 1;
            return sort(freq);
        }
        
    }

    public static void print(Map<String,Integer> freq) {
        
        Iterator<Map.Entry<String, Integer>> iterator = freq.entrySet().iterator();
        int i = 0;
        while (iterator.hasNext() && (i < 25)) {
            Map.Entry<String, Integer> entry = iterator.next();
            System.out.println(entry.getKey() + " - " + entry.getValue());
            i++;
        }
    
    }

}
