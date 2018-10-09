package com.nukeologist.archbot.commands;

import com.nukeologist.archbot.constants.Constants;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.web.util.UriUtils;

import java.time.Instant;


//import java.awt.*;


public class WolframCommand {

    private static EmbedBuilder eb = new EmbedBuilder();
    //Image image = null;
    private static String urlstring = "https://api.wolframalpha.com/v1/simple?appid=" + Constants.wolframAPIKey + "&i=";
    //how to encode url


    //OR eb.setimage


    public static void execute(GuildMessageReceivedEvent event, String[] toDo){


    }

    public static void executePrivate(PrivateMessageReceivedEvent event, String[] toDo){
        if(toDo.length == 1){
            event.getChannel().sendMessage("Wolfram what?").queue();
            return;
        }

        eb.setTitle("Wolfram|Alpha");
        eb.setDescription("");
        eb.setTimestamp(Instant.now());
        String temp = "";
        for(String welp: toDo){
            if(!welp.equals("wolfram")){
                temp = temp + " " + welp;
            }
        }

        urlstring = urlstring + UriUtils.encodeQuery(temp, "UTF-8");
        MessageBuilder message = new MessageBuilder();
        eb.setImage(urlstring);
        message.setEmbed(eb.build());
        event.getChannel().sendMessage(message.build()).queue();
    }
}
