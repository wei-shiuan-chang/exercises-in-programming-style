// package bulletin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

// package 16-bulletin-board;

@SuppressWarnings({"unchecked"})
public class DataStorage extends EventHandler {
    
	
    public DataStorage(Map obj, int iCommandEvCode, int iOutputEvCode) {
        super(obj, iCommandEvCode, iOutputEvCode);
    }

    protected Map<String,Object> execute(Map inMap) {

        
        // Read Pride and prejudice
        List<String> dataList = new ArrayList<>();
        String filePath = (String) ((Map<String, Object>) inMap).get("path");
        String[] datas;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
            while ((line = reader.readLine()) != null) {
                // System.out.println(line);
                sb.append(line);
                sb.append(" ");
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        datas = sb.toString().toLowerCase().split("[^a-zA-Z0-9]+");
        
        ((Map<String, Object>) inMap).put("data", dataList.addAll(Arrays.asList(datas)));

        // Read stopwords
        
        Set<String> stopWords = new HashSet<>();

        String str = "";
        try {
            byte[] encoded = Files.readAllBytes(Paths.get("../stop_words.txt"));
            str = new String(encoded);
        } catch (IOException e) {
            System.out.println("Error reading stop_words");
        }
        String[] words = str.split(",");

        for (String s : words) {
            stopWords.add(s);
        }
        ((Map<String, Object>) inMap).put("stop", stopWords);

        System.out.println("Finish read files, data.size = "+datas.length+", stop.size = "+stopWords.size());
        return (Map<String, Object>) inMap;
        
    }

	

}
