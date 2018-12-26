package com.farraindeslokoes.kregbot.commands;


import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

/**Essa e a interface pessoas deveriam usar para
 * Suas classes de comando. Implemente, e depois
 * Coloque no HashMap a sua implementacao.
 *
 * @author Nukeologist
 * @see Commands
 * @since 0.2
 */
public interface ICommand {

    /**
     * Executa o comando em servidores.
     * @param event o evento de mensagem em servidor.
     * @param toDo array de strings, veio do comando, porem SEPARADO POR ESPACOS.
     *
     */
    void execute(GuildMessageReceivedEvent event, String[] toDo);

    /**
     *  Executa o comando em DM (mensagens privadas)
     * @param event o evento de mensagem privada.
     * @param toDo array de strings, veio do comando, porem SEPARADO POR ESPACOS.
     */
    void executePrivate(PrivateMessageReceivedEvent event, String[] toDo);
}
