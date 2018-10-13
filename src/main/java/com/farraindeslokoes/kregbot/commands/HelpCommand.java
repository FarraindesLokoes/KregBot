package com.farraindeslokoes.kregbot.commands;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public class HelpCommand {

    public static void execute(GuildMessageReceivedEvent event, String[] toDo) {
        if(toDo.length == 1){
            event.getChannel().sendMessage("ArchBot: The Return \n!help: Shows you this\n!roll: Rolls a dice").queue();
        }else if(toDo.length == 2){
            switch (toDo[1]){
                case "help":
                    event.getChannel().sendMessage("You really need help with help? \nDo !help <command>").queue();
                    break;
                case "roll":
                    event.getChannel().sendMessage("Usage: !roll <number><d><number> <space>...").queue();
                    break;
                case "wolfram":
                    event.getChannel().sendMessage("Usage: !wolfram <anytext>").queue();
                    break;
            }
        }
    }

    public static void executePrivate(PrivateMessageReceivedEvent event, String[] toDo){
        if(toDo.length == 1) event.getChannel().sendMessage("ArchBot: The Return \n!help: Shows you this\n!roll: Rolls a dice").queue();
    }
}
