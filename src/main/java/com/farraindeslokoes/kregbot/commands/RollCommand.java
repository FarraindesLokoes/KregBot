package com.farraindeslokoes.kregbot.commands;


import com.farraindeslokoes.kregbot.util.RollStringUtil;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.util.Random;


/** Essa classe contem o comando "roll"
 *  Modificado para funcionar melhor no novo sistema por Nukeologist
 *  Usa Pseudo-Random para jogar dados virtuais de d&d.
 * @author Nukeologist
 * @since 0.1
 */
public class RollCommand implements ICommand  {


    public void execute(GuildMessageReceivedEvent event, String[] toDo) {
        Random rand = new Random();
        RollStringUtil util;
        String name = event.getMember().getNickname();
        if(name == null)name = event.getMember().getUser().getName();
        if(toDo.length == 1) {
            util = doSums(1, 20);
            event.getChannel().sendMessage(name + " rolled " + util.numString + " = " + util.num).queue();
        }else if(toDo.length>1){
            event.getChannel().sendMessage(name + " rolled " + parseRolls(toDo)).queue();
        }

    }

    public void executePrivate(PrivateMessageReceivedEvent event, String[] toDo){
        Random rand = new Random();
        RollStringUtil util;
        //Mesma coisa, porem sem o nome no comeco.
        if(toDo.length == 1) {
            util = doSums(1, 20);
            event.getChannel().sendMessage("You" + " rolled " + util.numString + " = " + util.num).queue();
        }else if(toDo.length>1){
            event.getChannel().sendMessage("You" + " rolled " + parseRolls(toDo)).queue();
        }
    }

    private String parseRolls(String[] toDo){    //returns final string with parentheses and shit
        String finalString = "";
        int total = 0;
        for(String opa : toDo) {
            if (opa.matches("\\d+d\\d+")) {
                String temp = opa;
                int dice = Integer.parseInt(temp.replaceAll("^\\D*?(-?\\d+).*$", "$1")); //parses int before d
                int diceNumber = Integer.parseInt(opa.replaceFirst("\\d+d", ""));
                RollStringUtil tempVar = doSums(dice, diceNumber);
                finalString = finalString + tempVar.numString + " + ";
                total += tempVar.num;
            } else if (opa.matches("d\\d+")) {
                String temp = opa;
                int diceNumber = Integer.parseInt(temp.replaceAll("^\\D*?(-?\\d+).*$", "$1"));
                RollStringUtil tempVar = doSums(1, diceNumber);
                finalString = finalString + tempVar.numString + " + ";
                total += tempVar.num;
            }else if(opa.matches("\\+")){

            }else if(opa.matches("-?\\d+")) { //checks if there is a whole number in string
                int num = Integer.parseInt(opa);
                finalString = finalString + " (" + num + ") + ";
                total+= num;
            }else if(!opa.equalsIgnoreCase("roll")){ //if this is true then idk wtf is in the string
                finalString = finalString + " NULL + ";
            }
        }
        StringBuilder sb = new StringBuilder(finalString);
        sb.deleteCharAt(sb.length() -1);
        sb.deleteCharAt(sb.length() -1);
        finalString = sb.toString();
        return finalString + " = " + total;
    }

    private static RollStringUtil doSums(int numberOfDice, int diceType){   //actually does the rolling
        String resultString;
        Random rand = new Random();
        int roll, total = 0;

        resultString = "(";

        for(int i = 0; i<numberOfDice; i++){
            roll = rand.nextInt(diceType) + 1;
            resultString = resultString + roll + " + ";
            total+= roll;
        }

        StringBuilder builder = new StringBuilder(resultString);
        builder.deleteCharAt(builder.length() - 1);
        builder.deleteCharAt(builder.length() - 1);
        builder.deleteCharAt(builder.length() - 1);
        resultString = builder.toString();

        return new RollStringUtil(total, resultString + ")");

    }

    @Override
    public String getHelpString() {
        return "Usage: !roll <ndN>, where n = dice amount and N = number of sides.";
    }
}
