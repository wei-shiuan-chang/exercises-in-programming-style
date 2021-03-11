import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collector;
import java.util.stream.Collectors;

// package 32-double-map-reduce;

public class THIRTYTWO {

    public static class Partition implements Iterator {

        private static final BlockingQueue<String> data = new LinkedBlockingDeque<>();
        int nlines;

        public Partition(String dataStr, int nlines) {
            this.nlines = nlines;
            String[] lines = dataStr.split("\n");

            for (String l : lines) {
                data.add(l);
            }
        }

        @Override
        public boolean hasNext() {
            return !data.isEmpty();
        }

        @Override
        public Object next() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < nlines; i++) {
                String chunk = data.poll();
                if (chunk == null)
                    break;
                builder.append(chunk + " "); // line1 line2 line3...
            }
            return builder.toString();
        }

    }

    public static class SplitWords {

        String[] scan(String strData) {
            return strData.toLowerCase().split("[^a-zA-Z0-9]+");
        }

        List<String> removeStopWords(String[] wordList) throws IOException {

            Set<String> stopwords = new HashSet<>();
            stopwords.addAll(Arrays.asList(new String(Files.readAllBytes(Path.of("../stop_words.txt"))).split(",")));
            return Arrays.asList(wordList).stream().filter(w -> !stopwords.contains(w) && w.length() > 1).collect(Collectors.toList());
        }

        List<Map.Entry<String, Integer>> main(String dataStr) throws IOException {
            List<Map.Entry<String, Integer>> result = new ArrayList<>();
            removeStopWords(scan(dataStr)).stream().forEach(w -> result.add(new AbstractMap.SimpleEntry<>(w, 1)));
            return result;
        }
    }

    public static Map<String, List<Map.Entry<String, Integer>>> regrouop(List<Map.Entry<String, Integer>> pairsList){
        Map<String, List<Map.Entry<String, Integer>>> mapping = new HashMap<>();

        pairsList.stream().forEach(entry -> {

            
            if(mapping.containsKey(entry.getKey())){
                mapping.get(entry.getKey()).add(entry);
            }else{
                mapping.put(entry.getKey(),new ArrayList<>());
                mapping.get(entry.getKey()).add(entry);
            }
            
        });
        return mapping;

    }

    public static Entry<String, Integer> countWords(Map<String, List<Map.Entry<String, Integer>>> mapping){
        
        int sum = 0;  
		String word = "";
		for(Entry<String, List<Entry<String, Integer>>> e : mapping.entrySet()) {
			word = e.getKey();
			for(Entry<String, Integer> k: e.getValue()) {
				sum += k.getValue();
			}
		}
		return new AbstractMap.SimpleEntry<>(word, sum);

    }

    public static Map<String, Integer> sort(Map<String, Integer> wordFreq){
        return wordFreq
            .entrySet()
            .stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));


    }


    public static void main(String[] args) throws Exception {
        StringBuilder sb = new StringBuilder();
        String str;
        BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])));
        try{
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append(" ");
            }
            reader.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        str = sb.toString();

        Iterator iterator = new Partition(str,200);
        ArrayList<Map.Entry<String, Integer>> data = new ArrayList<>();
        while(iterator.hasNext()) {
            SplitWords spl = new SplitWords();
            
            
            ArrayList<Map.Entry<String, Integer>> temp = (ArrayList<Entry<String, Integer>>) spl
                    .main((String) iterator.next());
            for(Map.Entry<String, Integer> e: temp) {
                data.add(e);
            }
        }

        Map<String, List<Entry<String, Integer>>> group = regrouop(data);

        HashMap<String, Integer> freq = new HashMap<>();

        for(Entry<String, List<Entry<String, Integer>>> g : group.entrySet()) {
            Map<String, List<Entry<String, Integer>>> temp = new HashMap<>();
            temp.put(g.getKey(), g.getValue());
            Map.Entry<String, Integer> output = countWords(temp);
            freq.put(output.getKey(), output.getValue());
        }

        Set<Entry<String, Integer>> freqE = freq.entrySet();

        freqE
        .stream()
        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
        .limit(25)
        .forEach(e -> System.out.println(e.getKey()+" - "+e.getValue()));

    }
}
