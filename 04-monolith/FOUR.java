import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;



public class FOUR{

    private static final List<String> stop_words = new ArrayList<>();
    private static List<String> datas = new ArrayList<>();
    private static List<String> orderedString = new ArrayList<>();
    private static HashMap<String, Integer> freq = new HashMap<>();


    public static void readFile() throws IOException{
        //File file = new File(args[0]);
        File file = new File("../pride-and-prejudice.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();

        try{
            int i;
            while ((i = reader.read()) != -1) {
                char ch = (char)i;
                //System.out.println("Do something with " + ch);
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

    public static void order(int index){
        for(int i = index; i > 0; i--){
            if(freq.get(orderedString.get(i)) > freq.get(orderedString.get(i-1))){
                String temp = orderedString.get(i);
                orderedString.set(i, orderedString.get(i-1));
                orderedString.set(i-1, temp);
            }else{
                break;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        //load stop words
        String str = "";
        
        byte[] encoded = Files.readAllBytes(Paths.get("../stop_words.txt"));
        str = new String(encoded);
        
        String[] words = str.split(",");
        stop_words.addAll(Arrays.asList(words));

        //Read char by char
        readFile();

        //Iterate through datas
        for(String s:datas){
            if(!stop_words.contains(s)){
                if(freq.containsKey(s)){
                    freq.put(s, freq.get(s) + 1);
                    int index = orderedString.indexOf(s);
                    order(index);
                }else{
                    freq.put(s, 1);
                    orderedString.add(s);
                }
            }
        }

        //Get the first 25 of the orderedString
        for(int i = 0; i < 25; i++){
            System.out.println(orderedString.get(i)+" - "+freq.get(orderedString.get(i)));
        }



    }
}