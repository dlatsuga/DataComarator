package service;


import exceptions.ConnectionRefusedException;
import model.dao.DaoFactory;
import model.dao.DaoProcedure;

public class ProcedureService {
    private DaoProcedure daoProcedure;
    public void executeProcedure(boolean[] checkBoxArray) throws ConnectionRefusedException {
        if(checkBoxArray[0]){
            System.out.println("Start executeProcedureToCreateBaseTables");
            executeProcedureToCreateBaseTables();
            System.out.println("Done executeProcedureToCreateBaseTables");
        }

    }

    private void executeProcedureToCreateBaseTables() throws ConnectionRefusedException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        daoProcedure = daoFactory.getDaoProcedure();
        daoProcedure.callProcedureToCreateBaseTables();
    }


}
