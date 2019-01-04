package com.farraindeslokoes.kregbot.commands;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

/** Essa classe contem o comando "help"
 *  Modificado para funcionar melhor no novo sistema por Nukeologist
 *  Diz uma mensagem de ajuda relacionada a um comando
 * @author Nukeologist
 * @since 0.2
 */
public class HelpCommand implements ICommand {

    public void execute(GuildMessageReceivedEvent event, String[] toDo) {
        if(toDo.length == 1){
            event.getChannel().sendMessage("ArchBot: The Return \n!help <command> : shows more info\n!roll: Rolls a dice\n" +
                    "!wolfram: Tries to infer WolframAlpha API\n!insult: Insults...\n !insultadd: adds insult"          ).queue();
        }else if(toDo.length == 2){
            switch (toDo[1]){
                /*Nuke commands */
                case "help":
                    event.getChannel().sendMessage("You really need help with help? \nDo !help <command>").queue();
                    break;
                case "roll":
                    event.getChannel().sendMessage("Usage: !roll <number><d><number> <space>...").queue();
                    break;
                case "wolfram":
                    event.getChannel().sendMessage("Usage: !wolfram <anytext>").queue();
                    break;
                /*Spicy Ferret Commands. */
                case "insult":
                    event.getChannel().sendMessage("Usage: !insult").queue();
                    break;
                case "insultadd":
                    event.getChannel().sendMessage("Usage: !insultadd <params>").queue();
                    break;
            }
        }
    }

    public void executePrivate(PrivateMessageReceivedEvent event, String[] toDo){
        if(toDo.length == 1) event.getChannel().sendMessage("ArchBot: The Return \n!help: Shows you this\n!roll: Rolls a dice").queue();
    }
}
