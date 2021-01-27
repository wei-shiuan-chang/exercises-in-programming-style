/*package 05-cookbook;*/

import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;



public class FIVE {

    //The shared mutable data
    private static final List<String> stop_words = new ArrayList<>();
    private static List<String> datas = new ArrayList<>();
    private static List<String> datas_no = new ArrayList<>();
    private static HashMap<String, Integer> freq = new HashMap<>();


    public static void readFile(String path) throws Exception{
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
    }

    /*

    public void filter_chars_and_normalize() throws Exception{
    
        for(char ch:datas){
            if(!Character.isAlphabetic(ch)){
                ch = ' ';
            }
        }
    }

    public void scan(String path) throws Exception{

    }
    */

    public static void remove_stop_words(){

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
                datas_no.add(datas.get(i));
        }
    }

    public static void frequencies() {
        for(String s:datas_no){
            freq.put(s, freq.getOrDefault(s, 0)+1);
        }
    
    }

    public static void sort(){
        freq = freq
        .entrySet()
        .stream()
        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

    }

    public static void print25(){
        int i = 0;
        for(String s:freq.keySet()){
          System.out.println(s+" - "+freq.get(s));
          i++;
          if(i>=25) return;
        }
    }
    

    

    public static void main(String[] args)throws Exception{
        readFile("../pride-and-prejudice.txt");
        remove_stop_words();
        frequencies();
        sort();
        print25();

    }
}