package plugins;


import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

public class framework {

    private static final String PROP_PATH = "config.properties";
    private static final Properties PROPERTIES = new Properties();
    private static IApp app;
    public static void main(String[] args) {
        String filePath = "../pride-and-prejudice.txt";
        Scanner in = new Scanner(System.in);
        String name = in.nextLine();
        in.close();

        try {
            URL classUrl = new URL("file:///home/runner/ProgrammingStyle/week5/app/" + name + ".jar");
            URL[] classUrls = {classUrl};
            URLClassLoader cloader = new URLClassLoader(classUrls);
            Class cls = cloader.loadClass(name);
            IApp app = (IApp) cls.newInstance();
            
            int i = 0;
            for(String s:app.frequencies(app.words(args[0])).keySet()){
                System.out.println(s+" - "+app.frequencies(app.words(args[0])).get(s));
                i++;
                if(i>=25) return;
            }
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    


}
