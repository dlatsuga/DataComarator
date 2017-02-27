package model.dao;

import exceptions.ConnectionRefusedException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoFactory {

    private Connection conn;
    private DaoDataBaseTable daoDataBaseTable;
    private DaoTableDescription daoTableDescription;
    private static DaoFactory instance;

//    private static String url = "jdbc:postgresql://localhost:5432/sqlexcomputers";
//    private static String user = "postgres";
//    private static String password = "Ldg131531";



    public static String getUrl() {
        return url;
    }
    public static void setUrl(String url) {
        DaoFactory.url = url;
    }
    public static void createUrl(String host, String port, String sid) {
//        DaoFactory.url = "jdbc:postgresql://" + host + ":" + port + "/" + sid;
        DaoFactory.url = "jdbc:oracle:thin:@" + host + ":" + port + "" + sid;
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

    private DaoFactory(Connection conn) {
        this.conn = conn;
    }

    public static DaoFactory getInstance() throws ConnectionRefusedException {
        if (instance == null) {
            try {
                Connection conn = DriverManager.getConnection(url, user, password);
                instance = new DaoFactory(conn);

            } catch (SQLException e) {
                throw new ConnectionRefusedException();
            }
        }
        return instance;
    }

    public static boolean testConnection() throws ConnectionRefusedException {
        boolean isValid = true;
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            conn.close();
        } catch (SQLException e) {
            isValid = false;
            throw new ConnectionRefusedException();
        }
        return isValid;
    }
}
