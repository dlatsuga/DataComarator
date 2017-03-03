package service;


import exceptions.ConnectionRefusedException;
import model.dao.DaoFactory;
import model.dao.DaoProcedure;

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
            result = executeProcedureUpdateRowNumber(keysValueArray);
            System.out.println("Done Update Row Number");
        }


        return result;
    }

    private String executeProcedureToCreateBaseTables() throws ConnectionRefusedException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        daoProcedure = daoFactory.getDaoProcedure();
        return daoProcedure.callProcedureToCreateBaseTables();
    }

    private String executeProcedureUpdateRowNumber(String[] keysValueArray) throws ConnectionRefusedException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        daoProcedure = daoFactory.getDaoProcedure();
        return daoProcedure.callProcedureToUpdateRowNumber(keysValueArray);
    }

}
