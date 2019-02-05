package com.farraindeslokoes.kregbot.data;


import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;


public class DatabaseUtils {


    public static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));
        System.out.println(dbUri);
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        System.out.println(dbUrl);
        return DriverManager.getConnection(dbUrl, username, password);
    }

}
