package model.domain;

public class DataBaseTable {

    private String objectKey;
    private SchemaType schemaType;
    private TableType tableType;
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
        this.schemaType = defineSchemaType(schema);
        this.tableType = defineTableType(schema, name);
    }

    private SchemaType defineSchemaType(String schema) {
        if(schema.substring(0,3).equals("VT_")){
            return SchemaType.VT;
        }
        return SchemaType.DEFAULT;
    }

    private TableType defineTableType(String schema, String name) {
        if(schema.substring(0,3).equals("VT_")){
            return TableType.BASE;
        }
        else{
            if(name.substring(0,4).equals("VT_R")){
                return TableType.REPLICA;
            }
            else if(name.substring(0,4).equals("VT_E")){
                return TableType.EXTRA;
            }
            else if(name.substring(0,4).equals("VT_A")){
                return TableType.ANALYSIS;
            }
            else{
                return tableType.OTHER;
            }
        }
    }

    public String getObjectKey() {
        return objectKey;
    }
    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public SchemaType getSchemaType() {
        return schemaType;
    }
    public void setSchemaType(SchemaType schemaType) {
        this.schemaType = schemaType;
    }
    public TableType getTableType() {
        return tableType;
    }
    public void setTableType(TableType tableType) {
        this.tableType = tableType;
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
