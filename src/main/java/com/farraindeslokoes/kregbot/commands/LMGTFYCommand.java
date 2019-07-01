package com.farraindeslokoes.kregbot.commands;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.web.util.UriUtils;

public class LMGTFYCommand implements ICommand {

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] toDo) {

        if(toDo.length == 1){
            event.getChannel().sendMessage("Need moar").queue();
            return;
        }


        String temp = "";
        for(String welp: toDo){
            if(!welp.equals("lmgtfy")){
                temp = temp + welp + " ";
            }
        }
        //TODO: IMPROVE SANITIZATION
        if (event.getAuthor().getName().equals("KregBot")) return;
        //is bad
        StringBuilder url = new StringBuilder("<http://lmgtfy.com/?iie=").append(1).append("&q=");
        event.getChannel().sendMessage(url.append(UriUtils.encode(temp, "UTF-8")).append(">").toString()).queue();

    }

    @Override
    public void executePrivate(PrivateMessageReceivedEvent event, String[] toDo) {

        if(toDo.length == 1){
            event.getChannel().sendMessage("Need Context").queue();
            return;
        }


        String temp = "";
        for(String welp: toDo){
            if(!welp.equals("lmgtfy")){
                temp = temp + welp + " ";
            }
        }
        //TODO: IMPROVE SANITIZATION
        if (event.getAuthor().getName().equals("KregBot")) return;

        StringBuilder url = new StringBuilder("<http://lmgtfy.com/?iie=").append(1).append("&q=");
        event.getChannel().sendMessage(url.append(UriUtils.encode(temp, "UTF-8")).append(">").toString()).queue();
        event.getChannel().sendMessage("You doing this for yourself?").queue();

    }


    @Override
    public String getHelpString() {
        return "!lmgtfy <something> will help someone...";
    }
}
