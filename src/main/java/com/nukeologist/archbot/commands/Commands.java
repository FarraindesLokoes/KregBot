package com.nukeologist.archbot.commands;

import com.nukeologist.archbot.events.CommandEvent;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public class Commands extends CommandEvent {


    @Override
    protected void doCommand(Event event, String[] toDo) {
        if(event instanceof GuildMessageReceivedEvent) {
            GuildMessageReceivedEvent newEvent = (GuildMessageReceivedEvent) event;
            switch (toDo[0]) {
                case "roll":
                    RollCommand.execute(newEvent, toDo);
                    break;
                case "help":
                    HelpCommand.execute(newEvent, toDo);
                    break;
                case "wolfram":
                    WolframCommand.execute(newEvent, toDo);
                    break;
            }
        }else if(event instanceof PrivateMessageReceivedEvent){
            PrivateMessageReceivedEvent newEvent = (PrivateMessageReceivedEvent) event;
            switch (toDo[0]) {
                case "roll":
                    RollCommand.executePrivate(newEvent, toDo);
                    break;
                case "help":
                    HelpCommand.executePrivate(newEvent, toDo);
                    break;
                case "wolfram":
                    WolframCommand.executePrivate(newEvent, toDo);
                    break;
            }
        }

    }


}
