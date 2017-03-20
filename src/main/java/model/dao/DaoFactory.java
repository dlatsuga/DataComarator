package model.dao;

import exceptions.ConnectionRefusedException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoFactory {


    private DaoDataBaseTable daoDataBaseTable;
    private DaoTableDescription daoTableDescription;
    private DaoProcedure daoProcedure;

    private static Connection conn;
    private static DaoFactory instance;

//    private static String url = "jdbc:postgresql://localhost:5432/sqlexcomputers";
//    private static String user = "postgres";
//    private static String password = "Ldg131531";

//    private static String url = "jdbc:oracle:thin:@DK01SN7007:1521:T7007204";
//    private static String user = "TESTIMMD";
//    private static String password = "TESTIMMD";

    private static String url;
    private static String user;
    private static String password;

    public static String getUrl() {
        return url;
    }
    public static void setUrl(String url) {
        DaoFactory.url = url;
    }
    public static void createUrl(String host, String port, String sid, String connectionType) {

        DaoFactory.url = "jdbc:oracle:thin:@" + host + ":" + port + connectionType + sid; // service name
//        DaoFactory.url = "jdbc:oracle:thin:@" + host + ":" + port + "/" + sid; // service name
//        DaoFactory.url = "jdbc:oracle:thin:@" + host + ":" + port + ":" + sid; // sid

        System.out.println(url);
    }
    public static String getUser() {
        return user;
    }
    public static void setUser(String user) {
        DaoFactory.user = user;
    }
    public static String getPassword() {
        return password;
    }
    public static void setPassword(String password) {
        DaoFactory.password = password;
    }


    public DaoDataBaseTable getDaoDataBaseTable() {
        if (daoDataBaseTable == null) {
            daoDataBaseTable = new DaoDataBaseTableImpl(conn);
        }
        return daoDataBaseTable;
    }

    public DaoTableDescription getDaoTableDescription() {
        if (daoTableDescription == null) {
            daoTableDescription = new DaoTableDescriptionImpl(conn);
        }
        return daoTableDescription;
    }

    public DaoProcedure getDaoProcedure() {
        if (daoProcedure == null) {
            daoProcedure = new DaoProcedureImpl(conn);
        }
        return daoProcedure;
    }


    private DaoFactory(Connection conn) {
        this.conn = conn;
    }

    public static DaoFactory getInstance() throws ConnectionRefusedException {

        boolean isClosedConnection = true;
        try {
            isClosedConnection = conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (instance == null || isClosedConnection) {
            try {
                conn = DriverManager.getConnection(url, user, password);
                instance = new DaoFactory(conn);
            } catch (SQLException e) {
                throw new ConnectionRefusedException();
            }
        }
        return instance;
    }

    public static boolean testConnection() throws ConnectionRefusedException {
        boolean isValid = false;
//        conn = null;
        System.out.println("Inside testConnection");
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Inside testConnection getConnection");
            isValid = true;
        } catch (SQLException e) {
            System.out.println("Inside testConnection SQLException");
            e.printStackTrace();
            throw new ConnectionRefusedException(e.getMessage(), e);
        }
        finally {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new ConnectionRefusedException(e.getMessage(), e);
            }
        }
        return isValid;
    }
}
