package com.farraindeslokoes.kregbot.events;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class HelloEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

            String[] received = event.getMessage().getContentRaw().split("\\s+");
            if (received[0] != null && !event.getMember().getUser().isBot()) {  //TODO: FIX SECOND MEMBER OF NULL
                if (received[0].equalsIgnoreCase("viado")) {        //I THINK IF NOT BOT IT IS SHIT
                    event.getChannel().sendMessage("viado e voce, porra").queue();
                } else if (received[0].equalsIgnoreCase("fdp")) {
                    event.getChannel().sendMessage("fim da putaria, pessoal").queue();
                } else if (received[0].equalsIgnoreCase("bolsonaro")) {
                    event.getChannel().sendMessage("O MITO").queue();
                }else if (received[0].equalsIgnoreCase("haddad") ||received[0].equalsIgnoreCase("andrade")) {
                    event.getChannel().sendMessage("seu fascista").queue();
                }
            }

    }
}
