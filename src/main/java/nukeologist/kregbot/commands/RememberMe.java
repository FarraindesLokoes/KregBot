package nukeologist.kregbot.commands;

import net.dv8tion.jda.api.entities.User;
import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.CommandHelp;
import nukeologist.kregbot.api.Context;
import nukeologist.kregbot.util.CommonMessagesReplays;
import nukeologist.kregbot.util.MessageHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author SpicyFerret
 * <p>
 * class that will make an alarm to trigger on specified time
 */

public class RememberMe {

    @Command("rememberMe") //Command to set the alarm
    public static void setAlarm(Context context) {
        String[] messages = context.getWords();

        if (messages.length == 1) {
            CommonMessagesReplays.syntaxError(context, "time not specified");
        } else {
            Alarm alarm = Alarm.instantiate(context);
            if (alarm == null) {
                CommonMessagesReplays.syntaxError(context, "time not specified or time format not recognized", () -> helpRememberMe(context));
                return;
            }
            boolean confirmed = Schedule.addSchedule(context.getAuthor(), alarm);
            if (confirmed) {
                context.reply(", Okay! I'll notify you at " + alarm.time.hour + "h " + alarm.time.minute + "min, " + alarm.time.day + ".");
            } else {
                context.reply(", max alarms per person exceed: " + Schedule.maxAlarms + ".");
            }
        }

        // In case alarm checker is not running, make it stat running
        if (!running) {
            alarmCheck.start();
        }
    }

    @CommandHelp("rememberMe")
    public static void helpRememberMe(Context context) {
        CommonMessagesReplays.embedMessage(context, "My huge brain can remember stuff for you! Here's a little help:\n" +
                "Usage: !rememberMe <Time (with no spaces)> <Message (Optional)>\n" +
                "Here is some prefixes:\n" +
                "  Day: <d> <day> <days>\n" +
                "  Hour: <h> <hour> <hours>\n" +
                "  Minute: <m> <min> <minute> <minutes>\n" +
                "If you trust me and my AWESOOOOME toad memory, you should ask me to remember stuff for you!");
    }


    private static boolean running = false;
    private static Date date = new Date();
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd:HH:mm");

    private static Thread alarmCheck = new Thread(() -> {
        running = true;
        Time time = new Time(0, 0, 0);
        int lastDay = 0;


        //region Run this code while there`s an alarm to trigger
        while (Schedule.getSchedules().size() > 0) {
            date.setTime(System.currentTimeMillis());
            time.update(date, sdf);
            //case a month has passed, shit dont happen
            if (time.day == 1 && lastDay != time.day) {
                for (Schedule schedule : Schedule.schedules) {
                    for (Alarm alarm : schedule.getAlarms()) {
                        alarm.time.nextMonth(lastDay);
                    }
                }
            }
            lastDay = time.day;
            //Check if is time to trigger an alarm
            for (Schedule schedule : Schedule.getSchedules()) {
                for (Alarm alarm : schedule.getAlarms()) {
                    if (alarm.onTime(time)) {
                        String message = MessageHelper.collapse(alarm.context.getWords(), 2);
                        if (!message.equals("")) {
                            alarm.context.reply(", is time to:\n" +
                                    message);
                        } else {
                            alarm.context.reply(", Ding Dong!");
                        }
                        Schedule.removeSchedule(schedule.user, alarm);
                    }

                }
            }
            //sleep
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //endregion


        running = false;
    });


    private static class Schedule {
        private static int maxAlarms = 3;

        private static ArrayList<Schedule> schedules = new ArrayList<>();

        static List<Schedule> getSchedules() {
            return schedules;
        }

        static boolean addSchedule(User user, Alarm alarm) {
            for (Schedule schedule : schedules) {
                if (schedule.user.getId().equals(user.getId())) {
                    if (schedule.alarms.size() >= maxAlarms) {
                        return false;
                    }
                    schedule.alarms.add(alarm);
                    return true;
                }
            }
            schedules.add(new Schedule(user));
            return addSchedule(user, alarm);
        }

        static void removeSchedule(User user, Alarm alarm) {
            for (Schedule schedule : schedules) {
                if (schedule.user.getId().equals(user.getId())) {
                    schedule.alarms.remove(alarm);
                    if (schedule.alarms.size() == 0) schedules.remove(schedule);
                    return;
                }
            }
        }

        private User user;
        private List<Alarm> alarms;

        private Schedule(User user) {
            this.user = user;
            alarms = new ArrayList<>();
        }

        List<Alarm> getAlarms() {
            return alarms;
        }

    }

    private static class Alarm {

        private Time time;
        private Context context;

        private Alarm(Time time, Context context) {
            this.time = time;
            this.context = context;
        }

        static Alarm instantiate(Context context) {
            Time curTime = new Time();
            curTime.update(date, sdf);
            Time time = Time.getTimeFromString(context.getWords()[1]);
            if (time == null) return null;
            return new Alarm(time.addTime(curTime), context);
        }

        private boolean onTime(Time alarm) {
            return time.day * 1440 + time.hour * 60 + time.minute <= alarm.day * 1440 + alarm.hour * 60 + alarm.minute;
        }
    }

    private static class Time {
        int day, hour, minute;

        Time() {
            day = 0;
            hour = 0;
            minute = 0;
        }

        Time(int day, int hour, int minute) {
            update(day, hour, minute);
        }

        void update(int day, int hour, int minute) {
            int plusH = minute / 60;
            this.minute = minute % 60;

            hour += plusH;
            int plusD = hour / 24;
            this.hour = hour % 24;

            this.day = day + plusD;
        }

        void update(Date date, SimpleDateFormat sdf) {
            String[] strings = sdf.format(date).split(":");

            this.day = Integer.parseInt(strings[0]);
            this.hour = Integer.parseInt(strings[1]);
            this.minute = Integer.parseInt(strings[2]);
        }

        void nextMonth(int daysToSubtract) {
            if (day - daysToSubtract > 0)
                day -= daysToSubtract;
        }

        Time addTime(Time time) {
            this.day += time.day;
            this.hour += time.hour;
            this.minute += time.minute;
            update(this.day, this.hour, this.minute);
            return this;
        }

        //converts a String with time prefix into a Time object
        private static Time getTimeFromString(String time) {
            if (!time.chars().allMatch(Character::isDigit)) {
                short days = 0, hours = 0, minutes = 0;

                String[] sArr = time.split("");
                if (!sArr[0].chars().allMatch(Character::isDigit)) {
                    return null;
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
                                }
                            }

                            if (!exit) i++;
                        } while (i < sArr.length && !exit);
                    }
                }
                return new Time(days, hours, minutes);
            } else {
                return new Time(0, 0, Integer.parseInt(time));
            }
        }
    }

}
