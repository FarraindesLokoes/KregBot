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


}
