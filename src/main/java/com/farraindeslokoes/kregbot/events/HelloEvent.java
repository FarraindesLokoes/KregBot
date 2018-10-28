package com.farraindeslokoes.kregbot.events;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class HelloEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        String[] received = event.getMessage().getContentRaw().toLowerCase().split("\\s+"); //consertei esta parte.(com tolowercase!)
        if (received[0] != null && !event.getMember().getUser().isBot()) {  //TODO: FIX SECOND MEMBER OF NULL


            /**
             *          Switch napo e a resposta para tudo!!!!! -bignelli
             */
            //BIGNELI SHIT
                /*if (received[0].equalsIgnoreCase("viado")) {        //I THINK IF NOT BOT IT IS SHIT
                    event.getChannel().sendMessage("viado e voce, porra").queue();
                } else if (received[0].equalsIgnoreCase("fdp")) {
                    event.getChannel().sendMessage("fim da putaria, pessoal").queue();
                } else if (received[0].equalsIgnoreCase("bolsonaro")) {
                    event.getChannel().sendMessage("O MITO").queue();
                }*/

            switch (received[0]) {
                case "viado":
                    event.getChannel().sendMessage("viado e voce, porra").queue();
                    break;
                case "fdp":
                    event.getChannel().sendMessage("fim da putaria, pessoal").queue();
                    break;
                case "bolsonaro":
                    event.getChannel().sendMessage("O MITO").queue();
                    break;
                case "lula":
                    event.getChannel().sendMessage("O Lula ta preso, babaca!").queue();
                    break;
                case "andrade":
                    event.getChannel().sendMessage("Fascista").queue();
                    break;
                case "haddad":
                    event.getChannel().sendMessage("Fascista").queue();
                    break;
            }
        }

    }
}
