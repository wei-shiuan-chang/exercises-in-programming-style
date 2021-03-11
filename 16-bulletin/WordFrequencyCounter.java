package bulletin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

public class WordFrequencyCounter extends EventHandler {

    public WordFrequencyCounter(Map obj, int iCommandEvCode, int iOutputEvCode) {
        super(obj, iCommandEvCode, iOutputEvCode);
    }

    @SuppressWarnings({ "unchecked" })
    protected Map<String, Object> execute(Map inMap) {
        System.out.println("In the Fre Counter!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        for (String s : ((Map<String, Object>) inMap).keySet()) {
            System.out.println("key: " + s);
        }

        // System.out.println("data.size = "+ ((Map<String, Object>) ((Map<String,
        // Object>) inMap).get("data")).size()
        // + ", stop.size = "
        // + ((Map<String, Object>) ((Map<String, Object>) inMap).get("stop")).size());

        Map<String, Integer> freMap = new HashMap<>();
        Map<String, Integer> zMap = new HashMap<>();

        List<String> dataList = (List<String>) ((Map<String, Object>) inMap).get("data");
        Set<String> stopSet = (Set<String>) ((Map<String, Object>) inMap).get("stop");
        System.out.println("datalist size = " + dataList.size() + ", stopSet size = " + stopSet.size());
        for (String word : dataList) {
            if (!stopSet.contains(word) && word.length() > 1) {
                freMap.put(word, freMap.getOrDefault(word, 0) + 1);
                if (word.indexOf('z') != -1)
                    zMap.put(word, zMap.getOrDefault(word, 0) + 1);
            }
        }

        ((Map<String, Object>) inMap).put("fre", freMap);
        ((Map<String, Object>) inMap).put("z", zMap);

        System.out.println("Finish word count, fre.size = " + freMap.size() + ", z.size = " + zMap.size());

        return (Map<String, Object>) inMap;

    }
    // public static List<String> convertObjectToList(Object obj) {
    // List<?> list = new ArrayList<>();
    // if (obj.getClass().isArray()) {
    // list = Arrays.asList((Object[])obj);
    // } else if (obj instanceof Collection) {
    // list = new ArrayList<>((Collection<String>)obj);
    // }
    // return (List<String>) list;
    // }

    // @Override
    // public void update(Observable o, Object arg) {
    //     // TODO Auto-generated method stub

    // }


}


