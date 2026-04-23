package TCP;

import java.util.HashMap;

public class ResponseParser {

    public static HashMap<Integer, String> parse(String res) throws Exception{
        

        HashMap<Integer, String> map = new HashMap<>();

        if (res==null || !res.startsWith("DISCOUNT:")){


            throw new Exception("Empty response"); 
        }

        String[] blocks = res.split(":");


        if (blocks.length!=3){

            throw new Exception("Invalid format");
        }

        String p = blocks[1];

        String v = blocks[2];
        
        map.put(1, p);

        map.put(2, v);



        return map;



    }
    
}
