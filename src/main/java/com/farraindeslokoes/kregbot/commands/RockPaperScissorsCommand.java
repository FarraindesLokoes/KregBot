package com.farraindeslokoes.kregbot.commands;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public class RockPaperScissorsCommand implements ICommand {

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] toDo) {

        if (toDo.length > 1) {
            if (toDo[1].charAt(0) != 'r' && toDo[1].charAt(0) != 'p' && toDo[1].charAt(0) != 's') { //In case something went wrong
                event.getChannel().sendMessage("Something went wrong").queue();
            } else {//evething is okay
                byte kregPlay = choosePlay();
                event.getChannel().sendMessage(toDo[1].charAt(0) == 'r' ? (//player plays rock
                        kregPlay == 0 ? ("Kreg plays Rock!/n" + "Result: DRAW!") : (
                                kregPlay == 1 ? ("Kreg plays Paper!/n" + "Result: KREG WINS!") : (
                                        "Kreg plays Scissors!/n" + "Result: " + event.getAuthor().getName().toUpperCase() + "WINS!"
                                )
                        )
                ) : (toDo[1].charAt(0) == 'p' ? (//player plays paper
                        kregPlay == 0 ? ("Kreg plays Scissors!/n" + "Result: " + event.getAuthor().getName().toUpperCase() + "WINS!") : (
                                kregPlay == 1 ? ("Kreg plays Rock!/n" + "Result: DRAW!") : (
                                        "Kreg plays Paper!/n" + "Result: KREG WINS!"
                                )
                        )
                ) : (toDo[1].charAt(0) == 's' ? (//player plays scissors
                        kregPlay == 0 ? ("Kreg plays Paper!/n" + "Result: KREG WINS!") : (
                                kregPlay == 1 ? ("Kreg plays Scissors!/n" + "Result: " + event.getAuthor().getName().toUpperCase() + "WINS!") : (
                                        "Kreg plays Rock!/n" + "Result: DRAW!"
                                )
                        )
                ): ("Shit happens")))).queue();

            }
        } else {//play no specified
            event.getChannel().sendMessage("Must specify what you want to play").queue();
        }
    }

    @Override
    public void executePrivate(PrivateMessageReceivedEvent event, String[] toDo) {
        if (toDo.length > 1) {
            if (toDo[1].charAt(0) != 'r' && toDo[1].charAt(0) != 'p' && toDo[1].charAt(0) != 's') { //In case something went wrong
                event.getChannel().sendMessage("Something went wrong").queue();
            } else {//evething is okay
                byte kregPlay = choosePlay();
                event.getChannel().sendMessage(toDo[1].charAt(0) == 'r' ? (//player plays rock
                        kregPlay == 0 ? ("Kreg plays Rock!/n" + "Result: DRAW!") : (
                                kregPlay == 1 ? ("Kreg plays Paper!/n" + "Result: KREG WINS!") : (
                                        "Kreg plays Scissors!/n" + "Result: " + event.getAuthor().getName().toUpperCase() + "WINS!"
                                )
                        )
                ) : (toDo[1].charAt(0) == 'p' ? (//player plays paper
                        kregPlay == 0 ? ("Kreg plays Scissors!/n" + "Result: " + event.getAuthor().getName().toUpperCase() + "WINS!") : (
                                kregPlay == 1 ? ("Kreg plays Rock!/n" + "Result: DRAW!") : (
                                        "Kreg plays Paper!/n" + "Result: KREG WINS!"
                                )
                        )
                ) : (toDo[1].charAt(0) == 's' ? (//player plays scissors
                        kregPlay == 0 ? ("Kreg plays Paper!/n" + "Result: KREG WINS!") : (
                                kregPlay == 1 ? ("Kreg plays Scissors!/n" + "Result: " + event.getAuthor().getName().toUpperCase() + "WINS!") : (
                                        "Kreg plays Rock!/n" + "Result: DRAW!"
                                )
                        )
                ): ("Shit happens")))).queue();

            }
        } else {//play no specified
            event.getChannel().sendMessage("Must specify what you want to play").queue();
        }
    }

    @Override
    public String getHelpString() {
        return "Usage: !rps <what you want to to play>, could be rock(r), paper(p) or scissors(s)";
    }

    private byte choosePlay() {
        return (byte) (Math.random() * 3);
    }

}
