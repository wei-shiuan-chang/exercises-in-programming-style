import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

// package 30-dataspaces;

public class THIRTY {

    private static final BlockingQueue<String> wordSpace = new LinkedBlockingDeque<>();
    private static final BlockingQueue<HashMap<String, Integer>> freqSpace = new LinkedBlockingDeque<>();
    static Set<String> stopwords = new HashSet<>();
    static {
        try {
            stopwords.addAll(Arrays.asList(new String(Files.readAllBytes(Path.of("../stop_words.txt"))).split(",")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Runnable processWords = () -> {
        HashMap<String, Integer> freq = new HashMap<>();
        while (!wordSpace.isEmpty()) {
            String word;
            try {
                word = wordSpace.poll(1, TimeUnit.SECONDS);
                
            } catch (InterruptedException e) {
                break;
            }
            if (!stopwords.contains(word)) {    // && word.length() > 1
                freq.put(word, freq.getOrDefault(word, 0) + 1);
            }
        }
        // System.out.println("freqSpace.addsize: "+freq.size());
        try {
            freqSpace.put(freq);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    };

    public static void main(String[] args) throws Exception {

        wordSpace.addAll(Arrays.asList(new String(Files.readAllBytes(Path.of(args[0]))).toLowerCase().split("[^a-zA-Z0-9]+")).stream().filter(w -> w.length() > 1).collect(Collectors.toList()));
        int threadNum = 5;
        List<Thread> workers = new ArrayList<>(threadNum);
        for (int i = 0; i < threadNum; i++) {
            workers.add(new Thread(processWords));
        }
        workers.forEach(Thread::start);
        workers.forEach(w -> {
            try {
                w.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        final HashMap<String, Integer> wordFreq = new HashMap<>();
        while(!freqSpace.isEmpty()){
            HashMap<String, Integer> freqs = freqSpace.poll();
       

            freqs.forEach((k,v) -> {
                wordFreq.put(k, wordFreq.getOrDefault(k, 0)+v);
            });
        }
        // System.out.println("wordFreq.size: "+wordFreq.size());
        // System.out.println(Collections.max(wordFreq.values()));
        wordFreq
            .entrySet()
            .stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .limit(25)
            .forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
    }
}
