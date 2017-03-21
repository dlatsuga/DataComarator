package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.domain.KeyPattern;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

/*TODO*/
public class PatternService {

    private List<KeyPattern> keyPatterns;
    private Set<String> patternsName;
    private File file;
    private Gson gson;

    private String path = System.getProperty("user.dir") + "\\patterns.json";

    public List<KeyPattern> getKeyPatterns() {
        return keyPatterns;
    }
    public void setKeyPatterns(List<KeyPattern> keyPatterns) {
        this.keyPatterns = keyPatterns;
    }
    public Set<String> getPatternsName() {
        return patternsName;
    }
    public void setPatternsName(Set<String> patternsName) {
        this.patternsName = patternsName;
    }


    public void loadKeyPatterns() {

        gson = new Gson();
        patternsName = new TreeSet<>();


//        try {
//            file = new File(Thread.currentThread()
//                .getContextClassLoader()
//                .getResource("patterns/patterns.json").toURI());
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }


        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            keyPatterns = new ArrayList<KeyPattern>(Arrays.asList(gson.fromJson(reader, KeyPattern[].class)));
            for (KeyPattern keyPattern : keyPatterns) {
                patternsName.add(keyPattern.getName());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        try {
//            keyPatterns = new ArrayList<KeyPattern>(Arrays.asList(gson.fromJson(new FileReader(file), KeyPattern[].class)));
//            for (KeyPattern keyPattern : keyPatterns) {
//                patternsName.add(keyPattern.getName());
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    public KeyPattern getPatternInstanceByName(String patternName) {

        for (KeyPattern keyPattern : keyPatterns) {
            if (keyPattern.getName().equals(patternName)) {
                return keyPattern;
            }
        }
        return null;
    }

    public void updateKeyPatternList(String name, String key_for_join, String row_number_list, String row_number_sort, String compare_fields, String initial_fields, String group_key, String export_split_key) {
        KeyPattern keyPattern = getPatternInstanceByName(name);
        if (keyPattern != null) {
            keyPatterns.remove(keyPattern);
            keyPattern.setKey_for_join(key_for_join);
            keyPattern.setRow_number_list(row_number_list);
            keyPattern.setRow_number_sort(row_number_sort);
            keyPattern.setCompare_fields(compare_fields);
            keyPattern.setInitial_fields(initial_fields);
            keyPattern.setGroup_fields(group_key);
            keyPattern.setExport_split_key(export_split_key);
            keyPatterns.add(keyPattern);
        } else {
            keyPattern = new KeyPattern(name, key_for_join, row_number_list, row_number_sort, compare_fields, initial_fields, group_key, export_split_key);
            keyPatterns.add(keyPattern);
            patternsName.add(name);
        }
    }

    public void saveKeyPatternList() {


        try(FileWriter writer = new FileWriter(path)){
            gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            gson.toJson(keyPatterns, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }


//        try {
//            file = new File(Thread.currentThread()
//                    .getContextClassLoader()
//                    .getResource("patterns/patterns.json").toURI());
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//
//        try (Writer writer = new FileWriter(file)) {
//            gson = new GsonBuilder()
//                    .setPrettyPrinting()
//                    .create();
//            gson.toJson(keyPatterns, writer);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


}
