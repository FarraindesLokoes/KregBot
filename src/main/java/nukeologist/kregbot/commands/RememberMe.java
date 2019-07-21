package nukeologist.kregbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.CommandHelp;
import nukeologist.kregbot.api.Context;
import nukeologist.kregbot.util.MessageHelper;

import java.util.ArrayList;

/**
 * @autor SpicyFerret
 *
 * class that will make an alarm to trigger on specified time
 * */

public class RememberMe {
    private static ArrayList<Thread> clocks = new ArrayList<>();

    @Command("rememberMe")
    public static void setAlarm(Context context){
        String[] words = context.getWords();
        if (words.length == 1) {
            context.reply("Syntax error: timer and message(optional) not specified!");
        } else if (words.length == 2) {
            if (!words[1].chars().allMatch(Character::isDigit)) {
                context.reply("Syntax error: first argument is not numeric!");
                return;
            }
            context.reply("Okay, I`ll remember you!");
            addClock(new Clock("", Integer.parseInt(words[1]), context.getChannel(), context.getAuthor()));
        } else if (words.length > 2) {
            if (!words[1].chars().allMatch(Character::isDigit)) {
                context.reply("Syntax error: first argument is not numeric!");
                return;
            }
            context.reply("Okay, I`ll remember you!");
            addClock(new Clock(MessageHelper.sanitizeEveryone(MessageHelper.collapse(words, 2)), Integer.parseInt(words[1]), context.getChannel(), context.getAuthor()));
        }

    }

    @CommandHelp("rememberMe")
    public static void helpRemeberMe(Context context) {
        EmbedBuilder embed = new EmbedBuilder();
        MessageBuilder msg = new MessageBuilder();
        embed.setColor((int) (Math.random() * 16777215)); // now cam be red and white, thanks to SpicyFerret
        embed.setDescription("My huge brain can remember stuff for you! Here's a little help:\nUsage: !rememberMe <Time in seconds> <Optional message>\n If you thrust in me and my AWESOOOOME cleric memory, you should ask me to remember stuff for you!");
        context.send(msg.setEmbed(embed.build()).build());
    }

    private static void addClock(Clock clock) {
        Thread thread = new Thread(clock);
        thread.start();
        clocks.add(thread);
    }

    static void clearClock(Clock clock) {
        clocks.remove(clock);
    }
}
/**
 * @autor SpicyFerret
 *
 * helper class
 * */
class Clock implements Runnable {
    private long timer;
    private String alarm;
    private MessageChannel channel;
    private long setTime;
    private User user;

    Clock(String alarm, long timer, MessageChannel channel, User user){
        this.alarm = alarm;
        this.timer = timer * 1000;
        this.channel = channel;
        this.setTime = System.currentTimeMillis();
        this.user = user;
        System.out.println("Alarm created!");
    }

    private void fire(){
        if (!alarm.equals("")) {
            channel.sendMessage(user.getAsMention() + ", it`s time to: \n" + alarm).queue();
        } else {
            channel.sendMessage(user.getAsMention() + ", it`s time!").queue();
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(timer);
            fire();
        } catch (InterruptedException e) {
            channel.sendMessage(user.getAsMention() + "\n" +"```Alarm" + alarm + "failed! \n" +
                    (System.currentTimeMillis() - setTime) / 1000 + "seconds elapsed \n" +
                    (timer - (System.currentTimeMillis() - setTime)) / 1000 + "seconds remaining!```").queue();
        }
        RememberMe.clearClock(this);
    }

}
