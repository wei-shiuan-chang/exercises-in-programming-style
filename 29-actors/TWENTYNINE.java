// package actors;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class TWENTYNINE {
    private abstract static class ActiveWFObject extends Thread {
        // private String name;
        private final BlockingQueue<Object[]> queue = new LinkedBlockingQueue<>();
        private boolean stopMe = false;

        ActiveWFObject() {
            super();
            this.start();
        }

        abstract void dispatch(Object[] message) throws Exception;

        @Override
        public void run() {
            while (!stopMe) {
                try {
                    Object[] message = this.queue.poll();
                    String n = this.getClass().getSimpleName();
                    // if(message == null){
                    //     System.out.println(n + " " + Arrays.toString(message));
                    //     System.exit(1);
                    // }
                    
                    if (message != null) {
                        this.dispatch(message);
                        if (message[0].equals("die")){
                            // System.out.println(n + ":  message[0].equals(\"die\")");
                            this.stopMe = true;
                            System.exit(1);
                        }
                            
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
            
        }

    }

    private static void send(ActiveWFObject receiver, Object[] message) throws Exception {
        receiver.queue.add(message);
    }

   

    private static class DataStorageManager extends ActiveWFObject{

        private List<String> datas = new ArrayList<>();
        private StopWordManager stopWordManager;
        @Override
        void dispatch(Object[] message) throws Exception {
            if(message[0].equals("init")){  // ['init', sys.argv[1], stop_word_manager]
                // System.out.println("DataStorageManager: init");
                this.init(new Object[]{message[1], message[2]});
            }else if(message[0].equals("send_word_freqs")){ //WordFrequencyController: {"send_word_freqs", dataStorageManager}
                // System.out.println("DataStorageManager: send_word_freqs");    
                this.processWords(new Object[]{message[1]});
            }else{
                send(stopWordManager, message);
            }
        }

        void init(Object[] message){
            String pathToFile = (String)message[0];
            stopWordManager = (StopWordManager) message[1];
            // System.out.println("stopWordManager is StopWordManager?" + message[1].getClass().getName());
            try {
                BufferedReader reader = new BufferedReader(new FileReader(new File(pathToFile)));
                String line;
                while ((line = reader.readLine()) != null) {
                    datas.addAll(Arrays.asList(line.toLowerCase().split("[^a-zA-Z0-9]+")));
                }
                reader.close();
                // System.out.println("Get all word, size: "+datas.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void processWords(Object[] message) throws Exception {
            // System.out.println("wordFrequencyController is WordFrequencyController?" + message[0].getClass().getName());
            
            WordFrequencyController wordFrequencyController = (WordFrequencyController) message[0];
            for(String w:datas){
                send(stopWordManager,new Object[]{"filter", w});
            }
            send(stopWordManager,new Object[]{"top25", wordFrequencyController});
            
        }


    }
    
    private static class StopWordManager extends ActiveWFObject{

        private WordFrequencyManager wordFrequencyManager;
        private final Set<String> stopwords = new HashSet<>();

        void dispatch(Object[] message) throws Exception {
            if(message[0].equals("init")){  //['init', word_freq_manager]
                this.init(new Object[]{message[1]});
            }else if(message[0].equals("filter")){  //DataStorageManager: {"filter", w}
                this.filter(new Object[]{message[1]});
            }else{
                send(wordFrequencyManager, message);
            }
        }
        void init(Object[] message) throws Exception{
            wordFrequencyManager = (WordFrequencyManager)message[0];
            
            stopwords.addAll(Arrays.asList(new String(Files.readAllBytes(Path.of("../stop_words.txt"))).split(",")));
            
        }
        void filter(Object[] message) throws Exception {
            if(!stopwords.contains(message[0]) && message[0].toString().length() > 1){
                send(wordFrequencyManager,new Object[]{"word", message[0]});
            }
        }

    }

    private static class WordFrequencyManager extends ActiveWFObject{

        private HashMap<String, Integer> freq = new HashMap<>();
        private WordFrequencyController wordFrequencyController;

        @Override
        void dispatch(Object[] message) throws Exception {
            if(message[0].equals("word")){
                this.incrementCount(new Object[]{message[1]});
            }else if(message[0].equals("top25")){
                this.top25(new Object[]{message[1]});
            }
        }

        void incrementCount(Object[] message){
            freq.put((String) message[0], freq.getOrDefault(message[0], 0) + 1);
        }

        void top25(Object[] message) throws Exception{
            wordFrequencyController = (WordFrequencyController)message[0];
            freq = freq
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(25)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
            send(wordFrequencyController, new Object[]{"top25",freq});
        }
    }

    private static class WordFrequencyController extends ActiveWFObject{

        DataStorageManager dataStorageManager = new DataStorageManager();
        void dispatch(Object[] message) throws Exception {
            if(message[0].equals("run")){   //['run', storage_manager]
                this.exc(new Object[]{message[1]});
            }else if(message[0].equals("top25")){
                this.display(new Object[]{message[1]});
            }
        }

        void exc(Object[] message) throws Exception {
            dataStorageManager = (DataStorageManager) message[0];
            send(dataStorageManager, new Object[]{"send_word_freqs", this});
        }

        void display(Object[] message) throws Exception {
            ((HashMap<String, Integer>)message[0]).entrySet().stream().forEach(o -> System.out.println(o.getKey() + " - " + o.getValue()));
            super.stopMe = true;
            send(dataStorageManager, new Object[]{"die"});
            
        }
    }

    public static void main(String[] args) throws Exception {
        
        // args[0] = "../pride-and-prejudice.txt";

        WordFrequencyManager wordFrequencyManager = new WordFrequencyManager();
        
        StopWordManager stopWordManager = new StopWordManager();
        send(stopWordManager, new Object[]{"init", wordFrequencyManager});

        DataStorageManager dataStorageManager = new DataStorageManager();
        send(dataStorageManager, new Object[]{"init", args[0], stopWordManager});

        WordFrequencyController wordFrequencyController = new WordFrequencyController();
        send(wordFrequencyController, new Object[]{"run", dataStorageManager});
        
        wordFrequencyController.join();
        dataStorageManager.join();
        stopWordManager.join();
        wordFrequencyManager.join();
    }

    
}
