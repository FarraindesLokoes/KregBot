package com.nukeologist.archbot.commands;

import com.nukeologist.archbot.events.CommandEvent;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public class Commands extends CommandEvent {


    @Override
    protected void doCommand(Event event, String[] toDo) {
        if(event instanceof GuildMessageReceivedEvent) {
            switch (toDo[0]) {
                case "roll":
                    RollCommand.execute((GuildMessageReceivedEvent) event, toDo);
                    break;
                case "help":
                    HelpCommand.execute((GuildMessageReceivedEvent) event, toDo);
                    break;
                case "wolfram":
                    WolframCommand.execute((GuildMessageReceivedEvent)event, toDo);
                    break;
            }
        }else if(event instanceof PrivateMessageReceivedEvent){
            switch (toDo[0]) {
                case "roll":
                    RollCommand.executePrivate((PrivateMessageReceivedEvent) event, toDo);
                    break;
                case "help":
                    HelpCommand.executePrivate((PrivateMessageReceivedEvent) event, toDo);
                    break;
                case "wolfram":
                    WolframCommand.executePrivate((PrivateMessageReceivedEvent)event, toDo);
                    break;
            }
        }

    }


}
