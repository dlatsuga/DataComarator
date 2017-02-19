package model.dao;

import exceptions.ConnectionRefusedException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoFactory {

    private Connection conn;
    private DaoDataBaseTable daoDataBaseTable;
    private static DaoFactory instance;

    public DaoDataBaseTable getDaoDataBaseTable() {
        if (daoDataBaseTable == null) {
            daoDataBaseTable = new DaoDataBaseTableImpl(conn);
        }
        return daoDataBaseTable;
    }

    private DaoFactory(Connection conn) {
        this.conn = conn;
    }

    public static DaoFactory getInstance() throws ConnectionRefusedException {
        if (instance == null) {
            String url = "jdbc:postgresql://localhost:5432/sqlexcomputers";

            try {
                Connection conn = DriverManager.getConnection(url, "postgres", "Ldg131531");
                instance = new DaoFactory(conn);

            } catch (SQLException e) {
                throw new ConnectionRefusedException();
            }
        }
        return instance;
    }
}
