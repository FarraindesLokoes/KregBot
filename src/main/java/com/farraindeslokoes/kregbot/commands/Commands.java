package com.farraindeslokoes.kregbot.commands;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;


/** Essa classe contem o hashmap de todos os comandos possiveis e suas strings equivalentes.
 * @author Nukeologist
 * @see ICommand
 * @since 0.1
 */
public class Commands extends ListenerAdapter {

    private String prefix;

    private Map<String, ICommand> commands;

    /*Construtor default */
    public void Commands(){
        prefix = "!";
        commands = new HashMap<>();
        initializeHashMap();
    }


    /*PARTE IMPORTANTE!!!!! */
    private void initializeHashMap(){
        /*Comandos do Nuke */
        commands.put("help", new HelpCommand());
        commands.put("wolfram", new WolframCommand());
        commands.put("roll", new RollCommand());

        /*Comandos do SpicyFerret */
        commands.put("insult", new InsultCommand());
        commands.put("insultadd", new InsultCommand());
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] received = event.getMessage().getContentRaw().split("\\s+"); //makes the array be strings separated by spaces
        if(received[0].startsWith(prefix) && !event.getMember().getUser().isBot()) {
            StringBuilder sb = new StringBuilder(received[0]);
            for(int i = 0; i<prefix.length(); i++) {
                sb.deleteCharAt(0); //we delete the prefix
            }
            received[0] = sb.toString();
            findAndExecute(received, event);
            // doCommand(event, received); //sending the command
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
        findAndExecute(received, event);
        //doCommand(event, received);
    }

    /** Encontra o comando pelo seu identificador(string[0])
     *
     * @param com identificador do comando
     * @param event evento generico, checado se de servidor ou DMs
     */
    private void findAndExecute(String[] com, Event event){

        for(Map.Entry<String, ICommand> entry : commands.entrySet()){
            String key = entry.getKey();
            ICommand value = entry.getValue();
            if(key.equals(com[0])){
                if(event instanceof GuildMessageReceivedEvent){
                    value.execute((GuildMessageReceivedEvent) event, com);
                    return;

                }else if(event instanceof PrivateMessageReceivedEvent){
                    value.executePrivate((PrivateMessageReceivedEvent) event, com);
                    return;
                }
            }
        }

    }


}
