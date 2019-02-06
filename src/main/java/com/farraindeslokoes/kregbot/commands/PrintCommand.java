package com.farraindeslokoes.kregbot.commands;

import com.farraindeslokoes.kregbot.KregBot;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PrintCommand implements ICommand {

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] toDo) {

        event.getChannel().sendMessage(makePrintString()).queue();
    }

    @Override
    public void executePrivate(PrivateMessageReceivedEvent event, String[] toDo) {

    }

    @Override
    public String getHelpString() {
        return "prints the '++' table, works only on the server (for now)";
    }

    private String makePrintString() {

        String SQL = "SELECT message, number FROM increments";
        String returnString = "Messages\tvalue";
        try {
            Connection conn = KregBot.getSQLConnection();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            // display increment information

            while (rs.next()) {

                returnString += rs.getString("message") + "\t" + rs.getInt("number") + "\n";
            }

            return returnString;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return "SQL problem ocurred";
        }
    }
}
