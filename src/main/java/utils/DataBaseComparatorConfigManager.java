package utils;


import com.google.gson.Gson;
import model.domain.DataBaseComparatorConfig;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DataBaseComparatorConfigManager {

    public static DataBaseComparatorConfig loadConfigurationParameters(){
        Gson gson = new Gson();
        String path = System.getProperty("user.dir") + "\\config.json";
        DataBaseComparatorConfig dataBaseComparatorConfig = null;

        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            dataBaseComparatorConfig = gson.fromJson(reader, DataBaseComparatorConfig.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataBaseComparatorConfig;
    }
}
