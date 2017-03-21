package service;


import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;

public class ValidationService {

    /**
     * false if listKeyFromLabel contains fields which does not exist in listOfSelectedFields*/
    public static boolean validateKey(String keyFromLabel, ObservableList<String> listOfSelectedFields){
        ArrayList<String>  listKeyFromLabel = new ArrayList<String>(Arrays.asList(keyFromLabel.split(",")));
        listKeyFromLabel.removeAll(listOfSelectedFields);

        System.out.println(keyFromLabel.getClass().getName() + " = " +listKeyFromLabel);

      return listKeyFromLabel.size() <= 0;
    }
}
