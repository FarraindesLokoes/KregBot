package com.farraindeslokoes.kregbot.util;


import com.farraindeslokoes.kregbot.KregBot;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class DatabaseUtils {


    public static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }

    public static void createTableIfNotExists(String name, String tableParameters) {
        Connection connection = KregBot.getSQLConnection();
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + name + " " + tableParameters);
        } catch (SQLException e) {
            System.out.println("Failed to connect/ connection doesnt exist / database error");
            e.printStackTrace();
        }

    }

    /** Creates a string - int - int - int table with given name.
     *  Doesnt create if it already exists.
     * @param tableName table name
     */
    public static void createTableIfNotExists(String tableName) {
        Connection connection = KregBot.getSQLConnection();

        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " " + "( name varchar(255) NOT NULL UNIQUE, number1 integer NOT NULL DEFAULT '0', number2 integer NOT NULL DEFAULT '0', number3 integer NOT NULL DEFAULT '0' );");
        } catch (SQLException e) {
            System.out.println("Failed to connect/ connection doesnt exist / database error");
            e.printStackTrace();
        }
    }

    /** Gets a map of key name - int[3] array
     *  from the database
     * @param tableName the table´s name
     * @return map bewtween key string "name" and int, array size 3
     * returns null if a problem with the database ocurred.
     */
    public static Map<String, int[]> getTable(String tableName) {
        String SQL = "SELECT name, number1, number2, number3 FROM " + tableName;

        Map<String, int[]> map = new HashMap<>();
        try {
            Connection conn = KregBot.getSQLConnection();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            // display increment information

            while (rs.next()) {

                String name = rs.getString("name");
                int[] tempArray = new int[3];
                tempArray[0] = rs.getInt("number1");
                tempArray[1] = rs.getInt("number2");
                tempArray[2] = rs.getInt("number3");

                map.put(name, tempArray);
            }

            return map;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /** Updates a row in the database.
     *
     * @param tableName name of the table
     * @param name parameter for search of the row
     * @param number1 first number to update
     * @param number2 second number to update
     * @param number3 third number to update
     */
    public static void updateTable(String tableName, String name, int number1, int number2, int number3) {
        String SQL = "UPDATE " + tableName + " SET number1 = ? , number2 = ? , number3 = ? " + "WHERE name = ?";
        try {
            Connection conn = KregBot.getSQLConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQL);

            pstmt.setInt(1, number1);
            pstmt.setInt(2, number2);
            pstmt.setInt(3, number3);
            pstmt.setString(4, name);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Inserts a row into the database.
     *  Does nothing if one with the same name already exists
     * @param tableName name of the table
     * @param name unique param, name to insert
     * @param number1 first number to insert
     * @param number2 second number to insert
     * @param number3 third number to insert
     */
    public void insertToDatabase(String tableName, String name, int number1, int number2, int number3) {

        String SQL = "INSERT INTO " + tableName + "(name, number1, number2, number3) " + "VALUES(?,?,?,?) ON CONFLICT DO NOTHING";

        try {


            Connection conn = KregBot.getSQLConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQL);

            pstmt.setString(1, name);
            pstmt.setInt(2, number1);
            pstmt.setInt(3, number2);
            pstmt.setInt(4, number3);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
