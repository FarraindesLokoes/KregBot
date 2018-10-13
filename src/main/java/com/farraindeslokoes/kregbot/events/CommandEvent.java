package com.farraindeslokoes.kregbot.events;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public abstract class CommandEvent extends ListenerAdapter {

    public String prefix = "!"; //may change later

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] received = event.getMessage().getContentRaw().split("\\s+"); //makes the array be strings separated by spaces
        if(received[0].startsWith(prefix) && !event.getMember().getUser().isBot()) {
            StringBuilder sb = new StringBuilder(received[0]);
            for(int i = 0; i<prefix.length(); i++) {
                sb.deleteCharAt(0); //we delete the prefix
            }
            received[0] = sb.toString();
            doCommand(event, received); //sending the command
        }
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        String[] received = event.getMessage().getContentRaw().split("\\s+");
        if(received[0].startsWith(prefix)) {
            event.getChannel().sendMessage("You do not need a prefix.\n").queue();
            StringBuilder sb = new StringBuilder(received[0]);
            sb.deleteCharAt(0); //we delete the prefix
            received[0] = sb.toString();


        }
        doCommand(event, received);
    }

    protected abstract void doCommand(Event event, String[] toDo);



}
