package model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class DaoProcedureImpl implements DaoProcedure {
    Connection conn;

    public DaoProcedureImpl(Connection conn) {
        this.conn = conn;
    }

    public void callProcedureToCreateBaseTables(){
        String sql = "{call VT_CREATE_BASE_TABLES}";
        try
        {
            CallableStatement storedProc = conn.prepareCall(sql);
            storedProc.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


}
