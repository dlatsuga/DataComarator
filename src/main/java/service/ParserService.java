package service;

import com.google.gson.Gson;
import model.domain.KeyPattern;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ParserService {
//    https://habrahabr.ru/company/naumen/blog/228279/

    public void loadKeyPatterns(){
        Gson gson = new Gson();
        try {
            KeyPattern[] keyPatterns = gson.fromJson(new FileReader("patterns/patterns"),KeyPattern[].class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }




}
