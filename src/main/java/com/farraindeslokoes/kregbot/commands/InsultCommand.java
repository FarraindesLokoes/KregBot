package com.farraindeslokoes.kregbot.commands;

import com.farraindeslokoes.kregbot.util.Compacter;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.util.ArrayList;

/** Essa classe contem o comando "insulto" Originalmente, criado por Spicy Ferret.
 *  Modificado para funcionar melhor no novo sistema por Nukeologist
 *  Originalmente se chamava apenas "Insult". Modificado para se adequar.
 *  *Nao sei se funciona - Nukeologist *
 * @author Spicy Ferret
 * @author Nukeologist
 * @since 0.2
 */
public class InsultCommand implements ICommand{

    private static ArrayList<String> insults = new ArrayList<>();

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] toDo) {
        if(toDo[0].equals("insult")) insult(event, toDo);
        else if(toDo[0].equals("insultadd")) addInsult(event, toDo);
    }

    @Override
    public void executePrivate(PrivateMessageReceivedEvent event, String[] toDo) {
        if(toDo[0].equals("insult")) insult(event, toDo);
        else if(toDo[0].equals("insultadd")) addInsult(event, toDo);
    }

    //trows a insult
    public static void insult(GuildMessageReceivedEvent event, String[] strings) {
        //just trow a insult
        if (strings[0].equalsIgnoreCase("")) {
            event.getChannel().sendMessage(insults.get((int) (Math.random() * insults.size()))).queue();
        }
        //insult someone or something
        else {
            event.getChannel().sendMessage(Compacter.compactString(strings) + ", " + insults.get((int) (Math.random() * insults.size()))).queue();
        }
    }
    //add a insult
    public static void addInsult(GuildMessageReceivedEvent event, String[] newInsult) {
        insults.add(Compacter.compactString(newInsult));
        event.getChannel().sendMessage("Insult added.").queue();
    }

    ///Same stuff, but private
    public static void insult(PrivateMessageReceivedEvent event, String[] strings) {
        if (strings[0].equalsIgnoreCase("")) {
            event.getChannel().sendMessage(insults.get((int) (Math.random() * insults.size())));
        }
        else {
            event.getChannel().sendMessage(Compacter.compactString(strings) + ", " + insults.get((int) (Math.random() * insults.size()))).queue();
        }
    }

    public static void addInsult(PrivateMessageReceivedEvent event, String[] newInsult) {
        insults.add(Compacter.compactString(newInsult));
        event.getChannel().sendMessage("Insult added.").queue();
    }
}
