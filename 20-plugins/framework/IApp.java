package plugins;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

public interface IApp {
    List<String> words(String path) throws Exception;
    HashMap<String, Integer> frequencies(List<String> datas_no);


}
