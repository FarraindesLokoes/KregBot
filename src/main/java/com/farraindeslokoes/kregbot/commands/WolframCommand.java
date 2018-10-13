package com.farraindeslokoes.kregbot.commands;

import com.farraindeslokoes.kregbot.constants.Constants;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.web.util.UriUtils;

import java.time.Instant;





public class WolframCommand {

    private static EmbedBuilder eb = new EmbedBuilder();
    private static String urlstring;



    public static void execute(GuildMessageReceivedEvent event, String[] toDo){
        urlstring = "https://api.wolframalpha.com/v1/simple?appid=" + Constants.wolframAPIKey + "&i=";
        if(toDo.length == 1){
            event.getChannel().sendMessage("Wolfram what?").queue();
            return;
        }

        eb.setTitle("Wolfram|Alpha");
        eb.setDescription("");
        eb.setTimestamp(Instant.now());
        eb.setAuthor(event.getAuthor().getName());
        String temp = "";
        for(String welp: toDo){
            if(!welp.equals("wolfram")){
                temp = temp + welp + " ";
            }
        }

        urlstring = urlstring + UriUtils.encodeQuery(temp, "UTF-8");
        MessageBuilder message = new MessageBuilder();
        eb.setImage(urlstring);
        message.setEmbed(eb.build());
        event.getChannel().sendMessage(message.build()).queue();
    }




    public static void executePrivate(PrivateMessageReceivedEvent event, String[] toDo){
        urlstring = "https://api.wolframalpha.com/v1/simple?appid=" + Constants.wolframAPIKey + "&i=";
        if(toDo.length == 1){
            event.getChannel().sendMessage("Wolfram ...?").queue();
            return;
        }

        eb.setTitle("Wolfram|Alpha");
        eb.setDescription("");
        eb.setTimestamp(Instant.now());
        String temp = "";
        for(String welp: toDo){
            if(!welp.equals("wolfram")){
                temp = temp + welp + " ";
            }
        }


        //urlstring = urlstring + UriUtils.encode(temp, "UTF-8");
        urlstring = urlstring + UriUtils.encodeQueryParam(temp, "UTF-8");
        MessageBuilder message = new MessageBuilder();
        eb.setImage(urlstring);
        message.setEmbed(eb.build());
        event.getChannel().sendMessage(message.build()).queue();
    }
}
