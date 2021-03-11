package plugins;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//FIVE.java
public class app2 implements IApp {

    @Override
    public List<String> words(String path) throws Exception {

        final List<String> stop_words = new ArrayList<>();
        List<String> datas = new ArrayList<>();
        File file = new File(path);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();

        try{
            int i;
            while ((i = reader.read()) != -1) {
                char ch = (char)i;
                if(!Character.isAlphabetic(i)){
                    if(sb.length()>1)
                        datas.add(sb.toString());
                    sb = new StringBuilder();
                }else{
                    sb.append(Character.toLowerCase((char)i));
                }
            }

        }finally{
            reader.close();
        }

        List<String> datasNo = new ArrayList<>();
    
        String str = "";
        try {
          byte[] encoded = Files.readAllBytes(Paths.get("../stop_words.txt"));
          str = new String(encoded);
        } catch (IOException e) {
          System.out.println("Error reading stop_words");
        }
        String[] words = str.split(",");
        stop_words.addAll(Arrays.asList(words));
        
        for(int i = 0; i < datas.size(); i++){
            if(!stop_words.contains(datas.get(i)))
                datasNo.add(datas.get(i));
        }

        return datasNo;

    }
    
    @Override
    public HashMap<String, Integer> frequencies(List<String> datas_no){
        HashMap<String, Integer> freq = new HashMap<>();
        for(String s:datas_no){
            freq.put(s, freq.getOrDefault(s, 0)+1);
        }
        freq = freq
        .entrySet()
        .stream()
        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        return freq;
    }

}    
