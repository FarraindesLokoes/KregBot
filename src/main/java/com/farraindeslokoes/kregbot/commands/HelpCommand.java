package com.farraindeslokoes.kregbot.commands;

import com.farraindeslokoes.kregbot.KregBot;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.util.Map;

/** Essa classe contem o comando "help"
 *  Modificado para funcionar melhor no novo sistema por Nukeologist
 *  Diz uma mensagem de ajuda relacionada a um comando
 *  consegue ela pela Interface ICommand
 * @see ICommand
 * @author Nukeologist
 * @since 0.2
 */
public class HelpCommand implements ICommand {

    @Override
    public String getHelpString() {
        return "Use !help <command>";
    }

    public void execute(GuildMessageReceivedEvent event, String[] toDo) {
        if(toDo.length == 1) {
            event.getChannel().sendMessage("KregBot: The Return \n!help <command> : shows more info\n!roll: Rolls a dice\n" +
                    "!wolfram: Tries to infer WolframAlpha API\n!insult: Insults...\n !insultadd: adds insult").queue();

        }else if(toDo.length > 1) {

            ICommand command = getCommand(toDo[1]);
            //System.out.println(toDo[1]);
            if(command != null){
                event.getChannel().sendMessage(command.getHelpString()).queue();
            }


        }

    }

    public void executePrivate(PrivateMessageReceivedEvent event, String[] toDo){
        if(toDo.length == 1) {
            event.getChannel().sendMessage("KregBot: The Reckoning \n!help <command> : shows more info\n!roll: Rolls a dice\n" +
                    "!wolfram: Tries to infer WolframAlpha API\n!insult: Insults...\n !insultadd: adds insult").queue();

        }else {

            ICommand command = getCommand(toDo[1]);
            if(command != null){
                event.getChannel().sendMessage(command.getHelpString()).queue();
            }


        }
    }

    private ICommand getCommand(String command){
        Map<String, ICommand> map = KregBot.getCommands().getHashMap();

        for(Map.Entry<String, ICommand> entry : map.entrySet()) {
            String key = entry.getKey();
            ICommand value = entry.getValue();
            if(key.equals(command)){
                return value;
            }
        }
        return null;
    }
}
