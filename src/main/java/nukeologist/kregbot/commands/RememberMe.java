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
 * @author SpicyFerret
 * <p>
 * class that will make an alarm to trigger on specified time
 */

public class RememberMe {
    private static ArrayList<Thread> clocks = new ArrayList<>();

    @Command("rememberMe")
    public static void setAlarm(Context context) {
        String[] words = context.getWords();
        if (words.length == 1) {
            context.reply("Syntax error: timer and message(optional) not specified!");
        } else if (words.length == 2) {
            long time = getTimeSeconds(words[1]);
            if (time < 0) {
                context.reply("Syntax error: first argument is not numeric!");
                return;
            }
            context.reply("Okay, I`ll remember you in " + getTime(time) + "!");
            addClock(new Clock("", time, context.getChannel(), context.getAuthor()));
        } else if (words.length > 2) {
            long time = getTimeSeconds(words[1]);
            if (time < 0) {
                context.reply("Syntax error: first argument is not numeric!");
                return;
            }
            context.reply("Okay, I`ll remember you in " + getTime(time) + "!");
            addClock(new Clock(MessageHelper.sanitizeEveryone(MessageHelper.collapse(words, 2)), time, context.getChannel(), context.getAuthor()));
        }

    }

    @CommandHelp("rememberMe")
    public static void helpRemeberMe(Context context) {
        EmbedBuilder embed = new EmbedBuilder();
        MessageBuilder msg = new MessageBuilder();
        embed.setColor((int) (Math.random() * 16777215)); // now cam be red and white, thanks to SpicyFerret
        embed.setDescription("My huge brain can remember stuff for you! Here's a little help:\n" +
                "Usage: !rememberMe <Time (with no spaces)> <Message (Optional)>\n" +
                "Here is some prefixes:\n" +
                "  Day: <d> <day> <days>\n" +
                "  Hour: <h> <hour> <hours>\n" +
                "  Minute: <m> <min> <minute> <minutes>\n" +
                "  Second: <s> <second> <seconds>\n " +
                "If you thrust me and my AWESOOOOME toad memory, you should ask me to remember stuff for you!");
        context.send(msg.setEmbed(embed.build()).build());
    }

    // Make and run a new thread with a clock
    private static void addClock(Clock clock) {
        Thread thread = new Thread(clock);
        thread.start();
        clocks.add(thread);
    }

    // Remove a clock from clocks array list
    static void clearClock(Clock clock) {
        clocks.remove(clock);
    }

    //converts a String with time prefix into a long in seconds
    private static long getTimeSeconds(String time) {
        if (!time.chars().allMatch(Character::isDigit)) {
            short days = 0, hours = 0, minutes = 0, seconds = 0;

            String[] sArr = time.split("");
            if (!sArr[0].chars().allMatch(Character::isDigit)) {
                return -1;
            }
            short num = 0;

            for (int i = 0; i < sArr.length; i++) {
                String s = sArr[i];

                if (s.chars().allMatch(Character::isDigit)) {
                    num *= 10;
                    num += Short.parseShort(s);
                } else {

                    StringBuilder type = new StringBuilder();
                    boolean exit = false;

                    do {
                        s = sArr[i];
                        type.append(s);

                        if ((i + 1 < sArr.length && sArr[i + 1].chars().allMatch(Character::isDigit)) || i + 1 == sArr.length) {
                            switch (type.toString()) {
                                case "d":
                                case "day":
                                case "days":
                                    days = num;
                                    num = 0;
                                    exit = true;
                                    break;
                                case "h":
                                case "hour":
                                case "hours":
                                    hours = num;
                                    num = 0;
                                    exit = true;
                                    break;
                                case "m":
                                case "min":
                                case "minute":
                                case "minutes":
                                    minutes = num;
                                    num = 0;
                                    exit = true;
                                    break;
                                case "s":
                                case "second":
                                case "seconds":
                                    seconds = num;
                                    num = 0;
                                    exit = true;
                                    break;
                            }
                        }

                        if (!exit) i++;
                    } while (i < sArr.length && !exit);
                }
            }
            System.out.println(days + "d" + hours + "h" + minutes + "min" + seconds + "s" + "=" + (seconds + (minutes * 60) + (hours * 3600) + (days * 86400)));
            return (long) (seconds + (minutes * 60) + (hours * 3600) + (days * 86400));
        } else {
            return Long.parseLong(time);
        }
    }

    //converts a long in seconds to a String in dd hh mm ss
    private static String getTime(long seconds) {
        int
                d = (int) (seconds / 86400),
                h = (int) ((seconds % 86400) / 3600),
                min = (int) (((seconds % 86400) % 3600) / 60),
                s = (int) (((seconds % 86400) % 3600) % 60);

        StringBuilder time = new StringBuilder((d != 0 ? d + " days " : "") + (h != 0 ? h + " hours " : "") + (min != 0 ? min + " minutes " : "") + (s != 0 || (s == min && s == h && s == d && s == 0) ? s + " seconds " : ""));
        String[] timeSpited = time.toString().split(" ");
        if (timeSpited.length > 1) {
            timeSpited[timeSpited.length - 2] += " and";
        }
        time = new StringBuilder();
        for (String str : timeSpited) {
            str += " ";
            time.append(str);
        }
        time.deleteCharAt(time.lastIndexOf(" "));
        return time.toString();
    }

}

/**
 * @author SpicyFerret
 * <p>
 * helper class
 */
class Clock implements Runnable {
    private long timer;
    private String alarm;
    private MessageChannel channel;
    private long setTime;
    private User user;

    Clock(String alarm, long timer, MessageChannel channel, User user) {
        this.alarm = alarm;
        this.timer = timer;
        this.channel = channel;
        this.setTime = System.currentTimeMillis();
        this.user = user;
    }

    private void fire() {
        if (!alarm.equals("")) {
            channel.sendMessage(user.getAsMention() + ", it`s time to: \n" + alarm).queue();
        } else {
            channel.sendMessage(user.getAsMention() + ", it`s time!").queue();
        }
    }

    @Override
    public void run() {
        try {
            timer *= 1000;
            Thread.sleep(timer);
            fire();
        } catch (Exception ignored) {
            channel.sendMessage(user.getAsMention() + "\n" + "```Alarm" + alarm + "failed! \n" +
                    (System.currentTimeMillis() - setTime) / 1000 + "seconds elapsed \n" +
                    (timer - (System.currentTimeMillis() - setTime)) / 1000 + "seconds remaining!```").queue();
        }
        RememberMe.clearClock(this);
    }

}
