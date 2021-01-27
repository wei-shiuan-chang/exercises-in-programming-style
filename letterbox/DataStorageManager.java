import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

package letterbox;

public class DataStorageManager {

    private static String[] datas;
    
    
    

    public static String[] dispatch(String[] message){
        if(message[0].equals("init")) 
            init(message[1]);
        else if(message[0].equals("words"))
            return datas;
        else
            System.out.println("Message not understood " + message[0]);
        return datas;
    }

    private static void init(String path){
        File file = new File(path);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append(" ");
        }
        reader.close();
        datas = sb.toString().toLowerCase().split("[^a-zA-Z0-9]+");
    }





    




}
