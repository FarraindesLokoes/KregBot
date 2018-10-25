package com.farraindeslokoes.kregbot.commands;

import com.farraindeslokoes.kregbot.util.Compacter;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.util.ArrayList;

public class Insult {

    private static ArrayList<String> insults = new ArrayList<>();

    //trows a insult
    public static void insult(GuildMessageReceivedEvent event, String[] strings) {
        //just trow a insult
        if (strings[0].equalsIgnoreCase("")) {
            event.getChannel().sendMessage(insults.get((int) (Math.random() * insults.size()))).queue();
        }
        //insult someone or something
        else {
            event.getChannel().sendMessage(Compacter.compactString(strings) + ", " + insults.get((int) (Math.random() * insults.size()))).queue();
        }
    }
    //add a insult
    public static void addInsult(GuildMessageReceivedEvent event, String[] newInsult) {
        insults.add(Compacter.compactString(newInsult));
        event.getChannel().sendMessage("Insult added.").queue();
    }

    ///Same stuff, but private
    public static void insult(PrivateMessageReceivedEvent event, String[] strings) {
        if (strings[0].equalsIgnoreCase("")) {
            event.getChannel().sendMessage(insults.get((int) (Math.random() * insults.size())));
        }
        else {
            event.getChannel().sendMessage(Compacter.compactString(strings) + ", " + insults.get((int) (Math.random() * insults.size()))).queue();
        }
    }

    public static void addInsult(PrivateMessageReceivedEvent event, String[] newInsult) {
        insults.add(Compacter.compactString(newInsult));
        event.getChannel().sendMessage("Insult added.").queue();
    }
}
