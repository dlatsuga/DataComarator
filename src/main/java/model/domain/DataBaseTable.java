package model.domain;

public class DataBaseTable {
    private String schema;
    private String name;
    private double size;
    private int fieldsCount;
    private int rowsCount;

    public DataBaseTable() {
    }

    public DataBaseTable(String schema, String name, int fieldsCount) {
        this.schema = schema;
        this.name = name;
        this.size = 3;
        this.fieldsCount = fieldsCount;
        this.rowsCount = 5;
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
    public double getSize() {
        return size;
    }
    public void setSize(double size) {
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
