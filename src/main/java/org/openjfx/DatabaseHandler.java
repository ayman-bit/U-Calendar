package org.openjfx;

import java.sql.*;
import java.util.*;

/**
 * @author Ayman Abu Awad
 */

public class DatabaseHandler {

    private static DatabaseHandler handler = null;
    public static final String DB_URL = "jdbc:sqlite:src/main/resources/org/database/Data.db";
    private static Connection conn = null;
    private static Statement stmt = null;

    public DatabaseHandler() {

        createConnection();
        Map<String, String> login = new HashMap<>();
//        Using the createtable function, specific syntax need to be followed in order to create a new row or column as shown
        // THIS SHOULD NOT BE CHANGED
        login.put("table", "loginInfo");
        login.put("username", "VARCHAR");
        login.put("password", "VARCHAR");

        List<String> loginUnique = new ArrayList<>();
        loginUnique.add("username");


        Map<String, String> userData = new HashMap<>();
        // Developers can add new columns to the second table using the following syntax format as shown
        userData.put("table", "userData");
        userData.put("eventName", "VARCHAR");
        userData.put("date", "TEXT");
        userData.put("startTime", "TEXT");
        userData.put("endTime", "TEXT");
        userData.put("user_id", "INTEGER");

        DatabaseHandler.createTable(login, loginUnique);
//        If creating a second table that does not need a unique identifier, You will need to include the following syntax as shown
        DatabaseHandler.createTable(userData, Collections.<String>emptyList());
    }

    public static void createConnection() {

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A database is connected!.");
            }

        } catch (SQLException ex) {
            System.out.println("Exception at createConnection:dataHandler" + ex.getLocalizedMessage());
        }
    }

    public static void createTable(Map<String, String> data, List<String> unique) {
        String sql = "CREATE TABLE IF NOT EXISTS " +  data.get("table") + " (\n";
        sql += "id INTEGER PRIMARY KEY AUTOINCREMENT, \n";
        data.remove("table");

        for (Map.Entry<String, String> entry : data.entrySet()) {
            sql += entry.getKey() + " " + entry.getValue();

            if (data.keySet().toArray()[data.size() - 1] != entry.getKey()) {
                sql += ",\n";
            }
        } if (!unique.isEmpty()) {
            sql += ",\n";
            Iterator itr = unique.iterator();

            sql += "CONSTRAINT ukey UNIQUE (" + itr.next();
;           while (itr.hasNext()) {
                sql += ", " + itr.next();
            }

            sql += ")\n";
        }

        sql += ");";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException ex) {
            System.out.println("Exception at createNewTable:dataHandler" + ex.getLocalizedMessage());
        }
    }

    public static List<Map<String, Object>> execQuery(String query) {
        ResultSet result;
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()){
             result = stmt.executeQuery(query);

            ResultSetMetaData rs = result.getMetaData();
            int columnCount = rs.getColumnCount();

            while(result.next()) {
                Map<String, Object> obj = new HashMap<>();
                for (int i = 1; i <= columnCount; i++ ) {
                    obj.put(rs.getColumnName(i), result.getObject(i));
                }

                ret.add(obj);
            }
        }
        catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        }

        return ret;
    }

    public static boolean execAction(String qu) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()){
            stmt.execute(qu);
            return true;
        }
        catch (SQLException ex) {
            System.out.println("Exception at execAction:dataHandler" + ex.getLocalizedMessage());
            return false;
        }
        finally {
        }
    }

//      STILL WORKING ON IT
    public static boolean execDelete(String qu) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()){
            stmt.executeQuery(qu);
            return true;
        }
        catch (SQLException ex) {
            System.out.println("Exception at executeQuery:dataHandler" + ex.getLocalizedMessage());
            return false;
        }
        finally {
        }
    }



}