package backend.db;

import java.util.*;

public class test {



    public static void main ( String [] args ) {
        List<String> keys  =  Arrays.asList("first_name", "last_name", "date_of_birth", "id");
        Map <String, String>  values = new HashMap<>();
        values.put("first_name", "Tom");
        int num  = 0;
        for ( String i :values.keySet()) {
            if (!keys.contains(i)) {
                System.out.print("false");
                break;
            }

        }
        System.out.print("True");
    }
}
