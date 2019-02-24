package com.farraindeslokoes.kregbot.events;

import com.farraindeslokoes.kregbot.KregBot;
import com.farraindeslokoes.kregbot.util.DatabaseUtils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.sql.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloEvent extends ListenerAdapter {

    private static final Pattern INCREMENT_DECREMENT = Pattern.compile("^(\\S+)(\\+\\+|--)$");

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String[] received = event.getMessage().getContentRaw().toLowerCase().split("\\s+"); //consertei esta parte.(com tolowercase!)

            switch (received[0]) {
                case "viado":
                    event.getChannel().sendMessage("viado e voce, porra").queue();
                    break;
                case "fdp":
                    event.getChannel().sendMessage("fim da putaria, pessoal").queue();
                    break;
                case "bolsonaro":
                    event.getChannel().sendMessage("O MITO presidente").queue();
                    break;
                case "lula":
                    event.getChannel().sendMessage("O Lula ta preso, babaca!").queue();
                    break;
                case "andrade":
                case "haddad":
                    event.getChannel().sendMessage("Fascista").queue();
                    break;
            }
        if (!event.getChannelType().isGuild()) return;

        //WARNING: EXPERIMENTAL CODE
        //TODO: SANITIZE USER INPUT
        Matcher matcher = INCREMENT_DECREMENT.matcher(received[0]);


        if (matcher.matches()) {
            String key = matcher.group(1);
            int incr = matcher.group(2).equals("++") ? 1 : -1;
            DatabaseUtils.createTableIfNotExists("increments", "( message varchar(45) NOT NULL UNIQUE, number integer NOT NULL DEFAULT '0' );");
            //getIncrements();
            String SQL = "SELECT message, number " + "FROM increments " + "WHERE message = ?";

            try {
                Connection conn = KregBot.getSQLConnection();
                PreparedStatement pstmt = conn.prepareStatement(SQL);

                pstmt.setString(1, key);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {

                    if (rs.getString("message").equals(key)) {
                        incr = rs.getInt("number") + incr;

                        updateDabase(key, incr);
                    }
                }
                insertToDatabase(key, incr);
                event.getChannel().sendMessage(key + " == " + incr ).queue();

                getIncrements();
            } catch (SQLException ex) {
                System.out.println("failed to query");
                ex.printStackTrace();
            }

        } else if (received.length > 1) {
            Matcher matcher2 = INCREMENT_DECREMENT.matcher(received[0] + " " +received[1]);
            if (matcher2.matches()) {
                String key = matcher2.group(1);
                int incr = matcher2.group(2).equals("++") ? 1 : -1;
                DatabaseUtils.createTableIfNotExists("increments", "( message varchar(45) NOT NULL UNIQUE, number integer NOT NULL DEFAULT '0' );");
                //getIncrements();
                String SQL = "SELECT message, number " + "FROM increments " + "WHERE message = ?";

                try {
                    Connection conn = KregBot.getSQLConnection();
                    PreparedStatement pstmt = conn.prepareStatement(SQL);

                    pstmt.setString(1, key);
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {

                        if (rs.getString("message").equals(key)) {
                            incr = rs.getInt("number") + incr;

                            updateDabase(key, incr);
                        }
                    }
                    insertToDatabase(key, incr);
                    event.getChannel().sendMessage(key + " == " + incr ).queue();

                    getIncrements();
                } catch (SQLException ex) {
                    System.out.println("failed to query");
                    ex.printStackTrace();
                }
            }
        }
        if (event.getChannelType() == ChannelType.TEXT) {

            List<Permission> perms = event.getMember().getPermissions();

            if (perms.contains(Permission.ADMINISTRATOR)) {
                if (received.length > 2 && received[1].equals("==")) {

                    if (isNumeric(received[2])) {
                        //now we set the number in the database.
                        try {
                            insertToDatabase(received[0], Integer.parseInt(received[2]));
                            updateDabase(received[0], Integer.parseInt(received[2]));
                            event.getChannel().sendMessage(received[0] + " == " + received[2]).queue();
                        } catch (SQLException e) {
                            event.getChannel().sendMessage("SQL error ocurred").queue();
                            e.printStackTrace();
                        }
                    }
                }
            }

        }


    }

    private static boolean isNumeric(String str) {
        return str.chars().allMatch( Character::isDigit );
    }

    public void getIncrements() {

        String SQL = "SELECT message, number FROM increments";

        try {
            Connection conn = KregBot.getSQLConnection();

             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL);
            // display increment information
            displayIncrement(rs);
            } catch (SQLException ex) {
                ex.printStackTrace();
        }
    }

    private void displayIncrement(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.println(rs.getString("message") + "\t"
                    + rs.getInt("number"));

        }
    }

    private void insertToDatabase(String message, int number) throws SQLException {
        String SQL = "INSERT INTO increments(message, number) " + "VALUES(?,?) ON CONFLICT DO NOTHING";

        Connection conn = KregBot.getSQLConnection();
        PreparedStatement pstmt = conn.prepareStatement(SQL);

        pstmt.setString(1, message);
        pstmt.setInt(2, number);
        pstmt.executeUpdate();
    }

    private void updateDabase(String message, int number) throws SQLException {
        String SQL = "UPDATE increments " + "SET number = ? " + "WHERE message = ?";
        Connection conn = KregBot.getSQLConnection();
        PreparedStatement pstmt = conn.prepareStatement(SQL);

        pstmt.setInt(1, number);
        pstmt.setString(2, message);
        pstmt.executeUpdate();
    }


}
