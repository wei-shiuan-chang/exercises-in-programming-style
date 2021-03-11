
// package bulletin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.stream.Collectors;

public abstract class ZWords extends EventHandler {
    public ZWords(Map obj, int iCommandEvCode, int iOutputEvCode) {
        super(obj, iCommandEvCode, iOutputEvCode);
    }

    protected Map<String, Object> execute(Map inMap) {
        System.out.println("In zWords !!!!!!!!!!!!!!!!");

        Map<String, Integer> zMap = (Map<String, Integer>) ((Map<String, Object>) inMap).get("fre");

        for (String key : zMap.keySet()) {
            System.out.println(key + " - " + zMap.get(key));
        }

        return (Map<String, Object>) inMap;
    }

    // @Override
    // public void update(Observable o, Object arg) {
    //     // TODO Auto-generated method stub

    // }
}