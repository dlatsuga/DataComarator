package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.domain.KeyPattern;

import java.io.*;
import java.util.*;

/*TODO*/
public class PatternService {

    private List<KeyPattern> keyPatterns;
    private Set<String> patternsName;

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

    ClassLoader classLoader = PatternService.class.getClassLoader();
//    File file = new File(classLoader.getResource("patterns/patterns.json").getFile().toString());
    File file = new File("C:\\IDEA_Projects\\DataComparator\\src\\main\\resources\\patterns\\patterns.json");

    Gson gson = new Gson();


    public void loadKeyPatterns(){
        patternsName = new HashSet<>();
        try {
            keyPatterns = new ArrayList<KeyPattern>(Arrays.asList(gson.fromJson(new FileReader(file), KeyPattern[].class)));
            for (KeyPattern keyPattern : keyPatterns) {
                patternsName.add(keyPattern.getName());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public KeyPattern getPatternInstanceByName(String patternName){

        for (KeyPattern keyPattern : keyPatterns) {
            if(keyPattern.getName().equals(patternName)){
                return keyPattern;
            }
        }
        return null;
    }

    private String name;
    private String key_for_join;
    private String row_number_list;
    private String row_number_sort;
    private String compare_fields;
    private String initial_fields;
    private String export_split_key;


    public void updateKeyPatternList(String name, String key_for_join, String row_number_list, String row_number_sort, String compare_fields, String initial_fields, String export_split_key){
        KeyPattern keyPattern = getPatternInstanceByName(name);
        if(keyPattern != null){
            keyPatterns.remove(keyPattern);
            keyPattern.setKey_for_join(key_for_join);
            keyPattern.setRow_number_list(row_number_list);
            keyPattern.setRow_number_sort(row_number_sort);
            keyPattern.setCompare_fields(compare_fields);
            keyPattern.setInitial_fields(initial_fields);
            keyPattern.setExport_split_key(export_split_key);
            keyPatterns.add(keyPattern);
        }
        else{
            keyPattern = new KeyPattern(name, key_for_join, row_number_list, row_number_sort, compare_fields, initial_fields, export_split_key);
            keyPatterns.add(keyPattern);
            patternsName.add(name);
        }
    }

    public void saveKeyPatternList(){
        try (Writer writer = new FileWriter("C:\\IDEA_Projects\\DataComparator\\src\\main\\resources\\patterns\\patterns.json")) {
                gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .create();
                gson.toJson(keyPatterns, writer);
                System.out.println("Save Done");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }



}
