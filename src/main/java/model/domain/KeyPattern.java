package model.domain;

public class KeyPattern {
    private String name;
    private String key_for_join;
    private String row_number_list;
    private String row_number_sort;
    private String compare_fields;
    private String initial_fields;
    private String group_fields;
    private String export_split_key;

    public KeyPattern() {
    }

    public KeyPattern(String name, String key_for_join, String row_number_list, String row_number_sort, String compare_fields, String initial_fields, String group_key, String export_split_key) {
        this.name = name;
        this.key_for_join = key_for_join;
        this.row_number_list = row_number_list;
        this.row_number_sort = row_number_sort;
        this.compare_fields = compare_fields;
        this.initial_fields = initial_fields;
        this.group_fields = group_key;
        this.export_split_key = export_split_key;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getKey_for_join() {
        return key_for_join;
    }
    public void setKey_for_join(String key_for_join) {
        this.key_for_join = key_for_join;
    }
    public String getRow_number_list() {
        return row_number_list;
    }
    public void setRow_number_list(String row_number_list) {
        this.row_number_list = row_number_list;
    }
    public String getRow_number_sort() {
        return row_number_sort;
    }
    public void setRow_number_sort(String row_number_sort) {
        this.row_number_sort = row_number_sort;
    }
    public String getCompare_fields() {
        return compare_fields;
    }
    public void setCompare_fields(String compare_fields) {
        this.compare_fields = compare_fields;
    }
    public String getInitial_fields() {
        return initial_fields;
    }
    public void setInitial_fields(String initial_fields) {
        this.initial_fields = initial_fields;
    }

    public String getGroup_fields() {
        return group_fields;
    }
    public void setGroup_fields(String group_fields) {
        this.group_fields = group_fields;
    }

    public String getExport_split_key() {
        return export_split_key;
    }
    public void setExport_split_key(String export_split_key) {
        this.export_split_key = export_split_key;
    }


}
