package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by DTLS on 03.03.2017.
 */
public class ValidationService {

    private List<String> listKeyForJoin = new ArrayList<>();
    private List<String> listRowNumberList = new ArrayList<>();
    private List<String> listRowNumberSort = new ArrayList<>();
    private List<String> listCompareFields = new ArrayList<>();
    private List<String> listInitialFields = new ArrayList<>();
    private List<String> listExportSplitKey = new ArrayList<>();

    /*Входящие параметры - String... Значение на Label всегда или String или Empty*/
    public void parseLabelValue(String key_for_join, String row_number_list, String row_number_sort, String compare_fields, String initial_fields, String export_split_key){

        listKeyForJoin = Arrays.asList(key_for_join.split(","));
        listRowNumberList = Arrays.asList(row_number_list.split(","));
        listRowNumberSort = Arrays.asList(row_number_sort.split(","));
        listCompareFields = Arrays.asList(compare_fields.split(","));
        listInitialFields = Arrays.asList(initial_fields.split(","));
        listExportSplitKey = Arrays.asList(export_split_key.split(","));
    }

    public String transformCompareFieldsInSqlCase(List<String> listCompareFields){
        StringBuilder sb = new StringBuilder();
        String tmp;
        String result;
        for (String listCompareField : listCompareFields) {
            tmp = ",case when m." + listCompareField + " = t." + listCompareField + " then '0'\n" +
                    " when m." + listCompareField + " is null and t." + listCompareField + " is null then '0'\n" +
                    " else m." + listCompareField + " || ' | ' || t." + listCompareField + " end " + listCompareField + "\n";
            sb.append(tmp);
        }
        result = sb.toString().substring(1, sb.toString().length()); // Delete first coma
        return result;
    }

    /*Для расчета Diff  DECODE("' || atc.COLUMN_NAME || '",''0'',0,1)*/
    public String decodeCompareFieldsForDiff(List<String> listCompareFields){
        StringBuilder sb = new StringBuilder();
        String tmp;
        String result;
        for (String listCompareField : listCompareFields) {
            tmp = "+DECODE(" + listCompareField + ",'0',0,1)";
            sb.append(tmp);
        }
        result = sb.toString().substring(1, sb.toString().length()); // Delete plus
        return result;
    }

    /*Выводит исходные поля из мастера и теста вначале запроса*/
    public String createStringForInitialFields(List<String> listInitialFields){
        StringBuilder sb = new StringBuilder();
        String tmp;
        String result;
        for (String listInitialField : listInitialFields) {
            tmp = ", m." + listInitialField + " m_" + listInitialField + ", t." + listInitialField + " t_" + listInitialField;
            sb.append(tmp);
        }
        result = sb.toString().substring(1, sb.toString().length()); // Delete first coma
        return result;
    }

    public String createJoinCondition(List<String> listKeyForJoin, boolean isMasterTable){
        StringBuilder sb = new StringBuilder();
        String tmp;
        String result;
        String alias;
        if(isMasterTable){
            alias = "m.";
        }else{
            alias = "t.";
        }
        for (String fieldName : listKeyForJoin) {
            tmp = " || " + alias + fieldName;
            sb.append(tmp);
        }
        result = sb.toString().substring(3, sb.toString().length());
        return result;
    }

}
