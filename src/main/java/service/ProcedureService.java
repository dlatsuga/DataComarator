package service;


import exceptions.ConnectionRefusedException;
import model.dao.DaoFactory;
import model.dao.DaoProcedure;

import java.util.Arrays;
import java.util.List;

public class ProcedureService {
    private DaoProcedure daoProcedure;
    public String executeProcedure(boolean[] checkBoxArray, String[] keysValueArray) throws ConnectionRefusedException {
        String result = null;
        if(checkBoxArray[0]){
            System.out.println("Start Create Base Tables");
            result = executeProcedureToCreateBaseTables();
            System.out.println("Done Create Base Tables");
        }
        if(checkBoxArray[1]){
            System.out.println("Start Update Row Number");
            result = executeProcedureUpdateRowNumber(keysValueArray[1], keysValueArray[2]);
            System.out.println("Done Update Row Number");
        }
        if(checkBoxArray[2]){
            System.out.println("Start Create Result Tables");
            result = executeProcedureToCreateResultTables(keysValueArray);
            System.out.println("Done Create Result Tables");
        }
        if(checkBoxArray[3]){
            System.out.println("Start Export");
            executeExportQuery(keysValueArray[6]);
            System.out.println("Done Export");
        }
        return result;
    }

    private String executeProcedureToCreateBaseTables() throws ConnectionRefusedException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        daoProcedure = daoFactory.getDaoProcedure();
        return daoProcedure.callProcedureToCreateBaseTables();
    }

    private String executeProcedureUpdateRowNumber(String rnList, String rnSort) throws ConnectionRefusedException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        daoProcedure = daoFactory.getDaoProcedure();
        return daoProcedure.callProcedureToUpdateRowNumber(rnList, rnSort);
    }

    private String executeProcedureToCreateResultTables(String[] keysValueArray) throws ConnectionRefusedException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        daoProcedure = daoFactory.getDaoProcedure();
        return daoProcedure.callProcedureToCreateResultTables(parametersConverter(keysValueArray));
    }

    private void executeExportQuery(String splitKey) throws ConnectionRefusedException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        daoProcedure = daoFactory.getDaoProcedure();
        daoProcedure.executeExportQuery(splitKey);
    }

//  0    lbl_Key.getText()
//  1   ,lbl_RN_List.getText()
//  2   ,lbl_RN_Sort.getText()
//  3   ,lbl_Compare_Fields.getText()
//  4   ,lbl_Initial_Fields.getText()
//  5   ,lbl_Group_Key.getText()
//  6   ,lbl_Split_Key.getText()};



    private String[] parametersConverter(String[] keysValueArray) {
        String[] arrayOfParametersToCreateResultTable = new String[6];

            arrayOfParametersToCreateResultTable[0] = transformCompareFieldsInSqlCase(keysValueArray[3]);   // case_fields
            arrayOfParametersToCreateResultTable[1] = decodeCompareFieldsForDiff(keysValueArray[3]);    // decode_fields

            arrayOfParametersToCreateResultTable[2] = createStringForInitialFields(keysValueArray[4]);  // initial_fields

            arrayOfParametersToCreateResultTable[3] = createJoinCondition(keysValueArray[0], true); // master join condition
            arrayOfParametersToCreateResultTable[4] = createJoinCondition(keysValueArray[0], false); // test join condition

            arrayOfParametersToCreateResultTable[5] = createGroupByKey(keysValueArray[5]);  // group_by_fields

        return arrayOfParametersToCreateResultTable;
    }

    private List<String> splitFields(String fieldsInLine){
        return Arrays.asList(fieldsInLine.split(","));
    }

    private String transformCompareFieldsInSqlCase(String compareFields) {
        List<String> listCompareFields = splitFields(compareFields);
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

    private String decodeCompareFieldsForDiff(String compareFields) {
        List<String> listDecodeFields = splitFields(compareFields);
        StringBuilder sb = new StringBuilder();
        String tmp;
        String result;
        for (String listDecodeField : listDecodeFields) {
            tmp = "+DECODE(" + listDecodeField + ",'0',0,1)";
            sb.append(tmp);
        }
        result = sb.toString().substring(1, sb.toString().length()); // Delete plus
        return result;
    }

    private String createStringForInitialFields(String initialFields) {
        List<String> listInitialFields = splitFields(initialFields);
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

    private String createJoinCondition(String keyForJoin, boolean isMaster) {
        List<String> listKeyForJoin = splitFields(keyForJoin);
        StringBuilder sb = new StringBuilder();
        String tmp;
        String result;
        String alias;
            if(isMaster){
                alias = "m.";
            }else{
                alias = "t.";
            }
        for (String fieldName : listKeyForJoin) {
            tmp = " || " + alias + fieldName;
            sb.append(tmp);
        }
        result = alias + "RN ||" + sb.toString().substring(3, sb.toString().length());
        return result;
    }

    private String createGroupByKey(String groupByFields){
        List<String> listGroupByFields = splitFields(groupByFields);
        StringBuilder sb = new StringBuilder();
        String tmp;
        String result;
        for (String fieldName : listGroupByFields) {
            tmp = " || nvl(m." + fieldName + ",t." + fieldName +")";
            sb.append(tmp);
        }
        result = sb.toString().substring(4, sb.toString().length()) + " GROUP_KEY";
        return result;
    }

//    private String createSplitKey(String splitKey){
//
//    }

}
