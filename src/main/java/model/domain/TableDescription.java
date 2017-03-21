package model.domain;

public class TableDescription {

    private String tableKey;

    private String fieldName;
    private String fieldType;
    private int recordsCount;

    public TableDescription() {
    }

    public TableDescription(String tableKey, String fieldName, String fieldType, int recordsCount) {
        this.tableKey = tableKey;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.recordsCount = recordsCount;
    }

    public String getTableKey() {
        return tableKey;
    }
    public void setTableKey(String tableKey) {
        this.tableKey = tableKey;
    }

    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public String getFieldType() {
        return fieldType;
    }
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
    public int getRecordsCount() {
        return recordsCount;
    }
    public void setRecordsCount(int recordsCount) {
        this.recordsCount = recordsCount;
    }

    @Override
    public String toString() {
        return "TableDescription{" +
                "tableKey='" + tableKey + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", fieldType='" + fieldType + '\'' +
                ", recordsCount=" + recordsCount +
                '}';
    }

}
