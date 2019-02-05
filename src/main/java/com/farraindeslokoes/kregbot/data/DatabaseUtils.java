package com.farraindeslokoes.kregbot.data;


import com.farraindeslokoes.kregbot.KregBot;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;


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

    public static ResultSet getResultSet(String tableName, String Collumn1name, String Collumn2name, String Collumn2) {
        Connection connection = KregBot.getSQLConnection();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + Collumn1name + ", " + Collumn2name + " FROM " + tableName + " WHERE " + Collumn2name + " = '" + Collumn2 + "';");
            return rs;
        } catch (SQLException e) {
            System.out.println("Failed to connect/ connection doesnt exist / database error when getting result");
            e.printStackTrace();
        }
        return null;
    }

    public static void insertRowIntoTable(String table, String row, String Column1, String Column2) {
        Connection connection = KregBot.getSQLConnection();

        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("INSERT INTO " + table + "(" + Column1 + ", " + Column2 +  ") VALUES " + row);
        } catch (SQLException e) {
            System.out.println("Failed to connect/ connection doesnt exist / database error when inserting");
            e.printStackTrace();
        }
    }

    public static void updateRowInTable(String table, String whereCollumn, String where, String updateCollumn, String update) {
        Connection connection = KregBot.getSQLConnection();

        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("UPDATE " + table + " SET " + updateCollumn + " = " + update + " WHERE " + whereCollumn + " = '" + where + "';");
        } catch (SQLException e) {
            System.out.println("Failed to connect/ connection doesnt exist / database error when updating");
            e.printStackTrace();
        }
    }

}
