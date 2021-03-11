// package bulletin;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.stream.Collectors;

public class Top25 extends EventHandler {
    public Top25(Map obj, int iCommandEvCode, int iOutputEvCode) {
        super(obj, iCommandEvCode, iOutputEvCode);
    }

    protected Map<String, Object> execute(Map inMap) {
        System.out.println("In top25 !!!!!!!!!!!!!!!!");

        Map<String, Integer> freMap = (Map<String, Integer>) ((Map<String, Object>) inMap).get("fre");

        // Print top 25
        freMap = freMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        System.out.println("Before print !!!!!!!!!!!!!!!!" + freMap.entrySet().size());
        int i = 1;

        for (Entry<String, Integer> key : freMap.entrySet()) {
            System.out.println(key.getKey() + " - " + key.getValue());
            i++;
            if (i > 25)
                break;
        }

        return (Map<String, Object>) inMap;
    }

    // @Override
    // public void update(Observable o, Object arg) {
    //     // TODO Auto-generated method stub

    // }
}
