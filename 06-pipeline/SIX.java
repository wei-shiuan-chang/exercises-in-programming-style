/*package 05-cookbook;*/

import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;



public class SIX{

    //The shared mutable data
    private static final List<String> stop_words = new ArrayList<>();
    


    public static List<String> readFile(String path) throws Exception{
        List<String> datas = new ArrayList<>();
        File file = new File(path);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();

        try{
            int i;
            while ((i = reader.read()) != -1) {
                char ch = (char)i;
                //System.out.println("Do something with " + ch);
                //datas.add(Character.toLowerCase(ch));
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
        return datas;
    }

    public static List<String> remove_stop_words(List<String> datas){

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

    public static Map<String, Integer> frequencies(List<String> datasNo) {
        HashMap<String, Integer> freq = new HashMap<>();
        for(String s:datasNo){
            freq.put(s, freq.getOrDefault(s, 0)+1);
        }
        return freq;
    }

    public static Map<String, Integer> sort(Map<String, Integer> freq){
        freq = freq
        .entrySet()
        .stream()
        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        return freq;
    }

    public static void print25(Map<String, Integer> freq){
        int i = 0;
        for(String s:freq.keySet()){
          System.out.println(s+" - "+freq.get(s));
          i++;
          if(i>=25) return;
        }
    }
    

    

    public static void main(String[] args)throws Exception{
        
        print25(sort(frequencies(remove_stop_words(readFile("../pride-and-prejudice.txt")))));

    }
}