package com.farraindeslokoes.kregbot.events;

import com.farraindeslokoes.kregbot.data.DatabaseUtils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloEvent extends ListenerAdapter {

    private static final Pattern INCREMENT_DECREMENT = Pattern.compile("^(\\S+)(\\+\\+|--)$");

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

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
            DatabaseUtils.createTableIfNotExists("increments", "( message varchar(45) NOT NULL, number integer NOT NULL DEFAULT '0' );");
            ResultSet set = DatabaseUtils.getResultSet("increments", "number", "message", key );
            if (set == null) return;
            try {
                if ( set.next() ) {
                    
                    int number = set.getInt(1) + incr;
                    DatabaseUtils.updateRowInTable("increments", "message", key, "number", Integer.toString(number) );
                    event.getChannel().sendMessage(key +  " == " + number).queue();
                    System.out.println("tried to update table");
                }else {
                    DatabaseUtils.insertRowIntoTable("increments", "(" + key + ", " + incr + ");", "message", "number");
                    event.getChannel().sendMessage(key + " == " + incr).queue();
                    System.out.println("created new row");
                }
            } catch (SQLException e) {
                    event.getChannel().sendMessage("SQL problem...").queue();
                    e.printStackTrace();
            }
        }


    }
}
