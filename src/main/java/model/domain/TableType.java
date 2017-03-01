package model.domain;

/**
 * BASE -- table for copying. Located in VT schema.
 * REPLICA -- copy of BASE table. Located in Default schema.
 * EXTRA -- result table with extra records Master or Test. Located in Default schema.
 * ANALYSIS -- result table with Deviations. Located in Default schema.
 * DEFAULT --
 */
public enum TableType {
     BASE
    ,REPLICA
    ,EXTRA
    ,ANALYSIS
    ,OTHER
}
