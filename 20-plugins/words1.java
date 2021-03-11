
public class words1 {

    File file = new File(path);
    BufferedReader reader = new BufferedReader(new FileReader(file));
    StringBuilder sb = new StringBuilder();

    try{
        int i;
        while ((i = reader.read()) != -1) {
            char ch = (char)i;
            //System.out.println("Do something with " + ch);
            //datas.add(Character.toLowerCase(ch));
            if(!Character.isAlphabetic(i)){
                if(sb.length()>1)
                    datas.add(sb.toString());
                sb = new StringBuilder();
            }else{
                sb.append(Character.toLowerCase((char)i));
            }
        }

    }finally{
        reader.close();
    }
}    
   