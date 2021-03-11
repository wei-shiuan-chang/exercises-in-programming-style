import java.io.*;
import java.sql.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;

//java -classpath ".:sqlite-jdbc-3.34.0.jar" TWENTYSIX ../pride-and-prejudice.txt
public class TWENTYSIX {
    public static void create_db_schema(Connection connection) {
        try {
            Statement statement = connection.createStatement();

            statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("drop table if exists documents");
            statement.executeUpdate("drop table if exists words");
            statement.executeUpdate("drop table if exists characters");

            statement.executeUpdate("create table documents (id INTEGER PRIMARY KEY AUTOINCREMENT, name)");
            statement.executeUpdate("create table words (id, doc_id, value)");
            statement.executeUpdate("create table characters (id, word_id, value)");

            // statement.executeUpdate("insert into person values(3, 'leo')");
            // statement.executeUpdate("insert into person values(4, 'yui')");
            // ResultSet rs = statement.executeQuery("select * from person");
            connection.commit();
            System.out.println("Connected");
            // while(rs.next())
            // {
            // // read the result set
            // System.out.println("name = " + rs.getString("name"));
            // System.out.println("id = " + rs.getInt("id"));
            // }
        } catch (SQLException e) {
            System.out.println("Connection prob");
        }

    }

    public static void load_file_into_database(String path, Connection connection) throws Exception {
        Statement statement = connection.createStatement();
        
        ResultSet results = null;
        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
        StringBuilder sb = new StringBuilder();
        List<String> datas = new ArrayList<>();
        List<String> stop_words = new ArrayList<>();
        List<String> datas_no = new ArrayList<>();

        try {
            int i;
            while ((i = reader.read()) != -1) {
                if (!Character.isAlphabetic(i)) {
                    if (sb.length() > 1)
                        datas.add(sb.toString());
                    sb = new StringBuilder();
                } else {
                    sb.append(Character.toLowerCase((char) i));
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading data");
        } finally {
            reader.close();
        }

        // Remove stop words
        String str = "";
        try {
            byte[] encoded = Files.readAllBytes(Paths.get("../stop_words.txt"));
            str = new String(encoded);
        } catch (IOException e) {
            System.out.println("Error reading stop_words");
        }
        String[] words = str.split(",");
        stop_words.addAll(Arrays.asList(words));

        for (int i = 0; i < datas.size(); i++) {
            if (!stop_words.contains(datas.get(i)))
                datas_no.add(datas.get(i));
        }

        
        for(String s:datas){
            // System.out.println("Try to insert : "+s);
            statement.executeUpdate("INSERT INTO documents (name) VALUES ('"+s+"')");
        }
        connection.commit();
        // for(String s:datas){
        //     statement.executeUpdate("SELECT id from documents WHERE name = '"+s+"'");
        // }
        //statement.setFetchDirection(0);//

        // results = statement.executeQuery("SELECT MAX(id) FROM words");
        
        // while(results.next())
        // {
        // // read the result set
        // System.out.println("id = " + results.getString(1)); //
        // }
        int  word_id = 0;
        for(String s:datas_no){
            int doc_id = statement.executeQuery("SELECT id from documents WHERE name = '"+s+"'").getInt(1);
            statement.executeUpdate("INSERT INTO words VALUES ('"+word_id+"', '"+doc_id+"', '"+s+"')");
            
            word_id++;
            int  char_id = 0;
            for(char c:s.toCharArray()){
                statement.executeUpdate("INSERT INTO characters VALUES ('"+char_id+"', '"+word_id+"', '"+c+"')");
                char_id++;
            }
            
        }
        connection.commit();


        
    }

    public static void main(String[] args) throws Exception{
        //Create
        Connection connection = null;
        Statement stmt = null;
        ResultSet results = null;
        ResultSet results_z = null;

        connection =(Connection) DriverManager.getConnection("jdbc:sqlite:sample.db");
        connection.setAutoCommit(false);/////
        create_db_schema(connection);
        load_file_into_database(args[0],connection);

        results = connection.createStatement().executeQuery("SELECT value, COUNT(*) as C FROM words GROUP BY value ORDER BY C DESC");
        connection.commit();
        int count = 0;
        while(results.next() && count < 25)
        {
            // read the result set
            System.out.println(results.getString(1) + " - " + results.getString(2)); //
            count++;
        }

        results_z = connection.createStatement().executeQuery("SELECT COUNT(DISTINCT value) as C FROM words WHERE value LIKE '%z%'");
        
        while(results_z.next())
        {
        // read the result set
        System.out.println("The number of unique words with 'z': "+results_z.getString(1));// + " " + results_z.getString(2) + " " + results_z.getString(3)); //
        }
        connection.commit();

        //Query
        connection.close();
        
    }

}
