import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//package week3;

public class DataStorageManager {

    private static String[] datas;
    
    
    

    public static String[] dispatch(String[] message)throws Exception{
        if(message[0].equals("init")) 
            init(message[1]);
        else if(message[0].equals("words"))
            return datas;
        else
            System.out.println("Message not understood " + message[0]);
        return datas;
    }

    private static void init(String path) throws Exception{
        File file = new File(path);
        
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            //System.out.println(line);
            sb.append(line);
            sb.append(" ");
        }
        reader.close();
        
        datas = sb.toString().toLowerCase().split("[^a-zA-Z0-9]+");
        //System.out.println(datas.length);

        
    }





    




}
