package model.domain;

public class DataBaseTable {

    private String objectKey;

    private String schema;
    private String name;
    private int size;
    private int fieldsCount;
    private int rowsCount;

    public DataBaseTable() {
    }

    public DataBaseTable(String key, String schema, String name, int fieldsCount) {
        this.objectKey = key;
        this.schema = schema;
        this.name = name;
        this.size = 3;
        this.fieldsCount = fieldsCount;
        this.rowsCount = 5;
    }

    public String getObjectKey() {
        return objectKey;
    }
    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public String getSchema() {
        return schema;
    }
    public void setSchema(String schema) {
        this.schema = schema;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public int getFieldsCount() {
        return fieldsCount;
    }
    public void setFieldsCount(int fieldsCount) {
        this.fieldsCount = fieldsCount;
    }
    public int getRowsCount() {
        return rowsCount;
    }
    public void setRowsCount(int rowsCount) {
        this.rowsCount = rowsCount;
    }


    @Override
    public String toString() {
        return "DataBaseTable{" +
                "schema='" + schema + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", fieldsCount=" + fieldsCount +
                ", rowsCount=" + rowsCount +
                '}';
    }
}
