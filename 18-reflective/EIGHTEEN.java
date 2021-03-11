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
import java.util.Scanner;
import java.util.stream.Collectors;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EIGHTEEN {
    public static void main(String[] args) throws Exception {
        args[0] = "../pride-and-prejudice.txt";
        WordFrequencyController wfcontroller = new WordFrequencyController();
        wfcontroller.dispatch(new String[] { "init", args[0] });
        wfcontroller.dispatch(new String[] { "run" });

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n\nPlease provide the number of the class name:\n" +
                    "1 DataStorageManager\n" +
                    "2 StopWordManager\n" +
                    "3 WordFrequencyManager\n" +
                    "4 WordFrequencyController\n" +
                    "5 Exit\n");
            String input = scanner.nextLine().strip();
            if (input.equals("5")) break;
            else if(input.equals("1"))
                printout("DataStorageManager");
            else if(input.equals("2"))
                printout("StopWordManager");
            else if(input.equals("3"))
                printout("WordFrequencyManager");
            else if(input.equals("4"))
                printout("WordFrequencyController");
            else
                System.out.println("Please provide valid input.");
        }
        scanner.close();



    }

    public static void printout(String className) {
        try {
            Class cls = Class.forName(className);
            Field[] fields = cls.getDeclaredFields();
            System.out.println("----------Fields----------");
            Arrays.stream(fields).forEach(f ->System.out.println("Field name: " + f.getName() + "\nField type: " + f.getType()));

            Method[] methods = cls.getDeclaredMethods();
            System.out.println("----------Methods----------");
            Arrays.stream(methods).forEach(System.out::println);

            Class superclass = cls.getSuperclass();
            System.out.println("--------Superclasses--------");
            while (superclass != null) {
                System.out.println(superclass.getName());
                superclass = superclass.getSuperclass();
            }

            Class[] interfaces = cls.getInterfaces();
            System.out.println("---------Interfaces---------");
            Arrays.stream(interfaces).forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Invalid class name.");
        }
    }
}

interface IWordFrequencyManager {
    void dispatch(String[] message);

    void incrementCount(String word);

    void sorted();
}

interface IDataStorageManager {
    String[] dispatch(String[] message);

    void init(String path);
}

interface IStopWordManager {
    String dispatch(String[] message);

    void init();

    String isStopWord(String word);
}

class WordFrequencyController {

    private String[] words;
    private DataStorageManager storageManager;
    private StopWordManager stopWordManager;
    private WordFrequencyManager wordFrequencyManager;
    private Class clsDsm;
    private Class clsSwm;
    private Class clsWfm;

    // private static String[] storageManager;
    WordFrequencyController() {
        try {
            clsDsm = Class.forName("DataStorageManager");
            clsSwm = Class.forName("StopWordManager");
            clsWfm = Class.forName("WordFrequencyManager");
            storageManager = (DataStorageManager) clsDsm.getDeclaredConstructor().newInstance();
            stopWordManager = (StopWordManager) clsSwm.getDeclaredConstructor().newInstance();
            wordFrequencyManager = (WordFrequencyManager) clsWfm.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dispatch(String[] message) throws Exception {
        if (message[0].equals("init"))
            init(message[1]);
        else if (message[0].equals("run"))
            run();
        else
            System.out.println("Message not understood " + message[0]);
    }

    public void init(String path) throws Exception {

        words = storageManager.dispatch(new String[] { "init", path });
        stopWordManager.dispatch(new String[] { "init" });

    }

    private void run() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        for (String word : words) {
            
            String[] str1 = new String[] { "is_stop_word", word };
            String[] str2 = new String[] { "increment_count", word  };
            if (clsSwm.getDeclaredMethod("dispatch", String[].class).invoke(stopWordManager, new Object[]{str1}).equals("false") && word.length() > 1) {
                clsWfm.getDeclaredMethod("dispatch", String[].class).invoke(wordFrequencyManager, new Object[]{str2});
            }
        }
        String[] str3 = new String[] { "sorted" };
        clsWfm.getDeclaredMethod("dispatch", String[].class).invoke(wordFrequencyManager, new Object[]{str3});

    }
}

class WordFrequencyManager implements IWordFrequencyManager {

    private Map<String, Integer> freq;

    WordFrequencyManager() {
        List<String> datasNo = new ArrayList<>();
        this.freq = new HashMap<>();
    }

    @Override
    public void dispatch(String[] message) {
        if (message[0].equals("increment_count"))
            incrementCount(message[1]);
        else if (message[0].equals("sorted"))
            sorted();
        else
            System.out.println("Message not understood " + message[0]);

    }

    @Override
    public void incrementCount(String word) {

        freq.put(word, freq.getOrDefault(word, 0) + 1);

    }

    @Override
    public void sorted() {
        freq = freq.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        int i = 1;
        for (String key : freq.keySet()) {
            System.out.println(key + " - " + freq.get(key));
            i++;
            if (i > 25)
                break;
        }
    }
}

class DataStorageManager implements IDataStorageManager {

    private String[] datas;

    @Override
    public String[] dispatch(String[] message) {
        if (message[0].equals("init"))
            init(message[1]);
        else if (message[0].equals("words"))
            return datas;
        else
            System.out.println("Message not understood " + message[0]);
        return datas;
    }

    @Override
    public void init(String path) {
        File file = new File(path);

        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
        
            while ((line = reader.readLine()) != null) {
                // System.out.println(line);
                sb.append(line);
                sb.append(" ");
            }
            reader.close();
            datas = sb.toString().toLowerCase().split("[^a-zA-Z0-9]+");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}

class StopWordManager {

    private List<String> stopWords;

    StopWordManager(){}

    public String dispatch(String[] message){
        if(message[0].equals("init")){
            init();
            return "init";
        }else if(message[0].equals("is_stop_word"))
            return isStopWord(message[1]);
        else{
            System.out.println("Message not understood " + message[0]);
        }
        return "error";
    }


    //read all stop words
    public void init(){
        
    
        String str = "";
        try {
          byte[] encoded = Files.readAllBytes(Paths.get("../stop_words.txt"));
          str = new String(encoded);
        } catch (IOException e) {
          System.out.println("Error reading stop_words");
        }
        String[] words = str.split(",");
        stopWords = new ArrayList<String>();
        stopWords.addAll(Arrays.asList(words));
        
        //System.out.println(datasNo.size());
    }

    public String isStopWord(String word){
        if(stopWords.contains(word)){
            //System.out.println(word+" is stopword");
            return "true";
        } 
        else return "false";
    }
}
