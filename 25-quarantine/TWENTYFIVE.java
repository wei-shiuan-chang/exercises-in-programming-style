
// package 25-quarantine;
import java.util.function.Function;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;



public class TWENTYFIVE {
    interface Fun {
        Object _f(Object obj);
    }
    private static class TFTheOne {

        List<Fun> funList = new ArrayList<>();
        Object value = new Object();

        private TFTheOne(String[] args) {
            value = args;
        }

        private TFTheOne bind(Fun fun) {
            funList.add(fun);
            return this;
        }

        private void execute() {

            for(Fun f:funList){
                value = f._f(value);
            }
            System.out.println(value);
        }
    }

    private static Fun get_input(){
        Fun fun = new Fun() {
            @Override
            public Object _f(Object args) {
                return ((String[]) args)[0];
            }
        };
        return fun;
    }

    private static Fun extract_words(){
        Fun fun = new Fun() {
            @Override
            public Object _f(Object path_to_file) {
                List<String> datas = new ArrayList<>();
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(new File((String)path_to_file)));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        datas.addAll(Arrays.asList(line.toLowerCase().split("[^a-zA-Z0-9]+")));
                    }
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return datas;
            }
        };
        return fun;

    }

    private static Fun remove_stop_words(){
        Fun fun = new Fun() {
            @Override
            public List<String> _f(Object word_list) {
                Set<String> stopwords = new HashSet<>();
                BufferedReader reader;
                try {
                    reader = new BufferedReader(new FileReader(new File("../stop_words.txt")));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stopwords.addAll(Arrays.asList(line.toLowerCase().split(",")));
                    }
                    
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                return ((List<String>)word_list).stream().filter(w -> !stopwords.contains(w) && w.length() > 1).collect(Collectors.toList());
            }
        };
        return fun;
    }


    private static Fun frequencies(){
        Fun fun = new Fun(){
            public Map<String,Integer> _f(Object word_list){
                HashMap<String, Integer> freq = new HashMap<>();
                ((List<String>)word_list).stream().forEach(w -> freq.put(w, freq.getOrDefault(w, 0)+1));
                return freq;
            }
        };
        return fun;
    }

    private static Fun sort(){
        Fun fun = new Fun(){
            public Map<String,Integer> _f(Object word_freqs){
                return ((Map<String,Integer>)word_freqs)
                        .entrySet()
                        .stream()
                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
            }
        };
        return fun;
    }

    private static Fun top25_freqs(){
        Fun fun = new Fun(){
            public String _f(Object word_freqs){
                StringBuilder builder = new StringBuilder();
                ((Map<String,Integer>)word_freqs).entrySet().stream().limit(25).forEach(w -> builder.append(w.getKey() + " - " + w.getValue() + "\n"));
                return builder.toString();
            }
        };
        return fun;
    }

    public static void main(String[] args){
        TFTheOne theOne = new TFTheOne(args);
        theOne
            .bind(get_input())
            .bind(extract_words())
            .bind(remove_stop_words())
            .bind(frequencies())
            .bind(sort())
            .bind(top25_freqs())
            .execute();
    }
    
}
