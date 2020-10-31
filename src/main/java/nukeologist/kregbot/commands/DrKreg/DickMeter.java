package nukeologist.kregbot.commands.DrKreg;

import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.Context;
import nukeologist.kregbot.commands.EchoCommand;
import nukeologist.kregbot.data.DickInfo;
import nukeologist.kregbot.util.MessageHelper;
import nukeologist.kregbot.util.SaveHelper;
import nukeologist.kregbot.util.Tuple;
import org.jetbrains.annotations.NotNull;

import java.sql.Array;
import java.sql.Time;
import java.util.*;

public class DickMeter {

    private static SaveHelper<DickMeter.DickStorage> SAVER = new SaveHelper<>(DickMeter.DickStorage.class);
    private static final String DICKDATA = "dickInfo";
    private static DickMeter.DickStorage dickStorage = SAVER.fromJson(DICKDATA);

    private static final Random random = new Random();
    private static final Calendar calendar = Calendar.getInstance();

    private static int generateDickSize(){
        int size = (int) ((random.nextGaussian() + 2) * 7);
        return Math.max(size, 0);
    }

    private static String getDate(){
        calendar.setTimeInMillis(System.currentTimeMillis());

        return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
    }

    private static void sendDicks(Context context, List<Tuple<String, Integer>> data) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Tuple<String, Integer> sData : data) {
            stringBuilder.append("8").append("=".repeat(Math.max(0, sData.b))).append("D  ").append(sData.a);
        }

        context.send(MessageHelper.makeCodeBlock(stringBuilder.toString()));
    }


    @Command("dick")
    public static void dick(Context context){
        String[] words = context.getWords();

        if (words.length <= 1) {
            context.reply("What you want to know about your dick? Measure or history?");
            return;
        }
        if (words[1].toLowerCase().equals("measure")) {
            DickInfo dickInfo = searchUser(context);

            String date = getDate();
            if (dickInfo.getDickData().containsKey(date)) {
                context.reply("Already measured today!");
                return;
            }
            Tuple<String, Integer> data = new Tuple<>(date, generateDickSize());
            dickInfo.addToDickData(data);
            sendDicks(context, List.of(data));
        } else if (words[1].toLowerCase().equals("history")) {
            DickInfo dickInfo = searchUser(context);

            if (dickInfo.getDickData().isEmpty()) {
                context.reply("It looks like I never saw your dick...\n '!dick measure' to show him to me.");
                return;
            }
            ArrayList<Tuple<String, Integer>> history = new ArrayList<>();

            for (Map.Entry<String, Integer> entry: dickInfo.getDickData().entrySet()){
                history.add(new Tuple<>(entry.getKey(), entry.getValue()));
            }

            context.reply("Here's your history:");
            sendDicks(context, history);
        }
        saveData();
    }




    private static DickInfo searchUser(Context context){
        long guild = context.getMember().getGuild().getIdLong();
        long user = context.getMember().getIdLong();

        if (!dickStorage.DICKS.containsKey(guild)) {
            dickStorage.DICKS.put(guild, new ArrayList<>());
        }

        for (DickInfo dickInfo : dickStorage.DICKS.get(guild)) {
            if (user == dickInfo.getUSER()) {
                return dickInfo;
            }
        }
        DickInfo newDickInfo = new DickInfo(user);
        dickStorage.DICKS.get(guild).add(newDickInfo);
        return newDickInfo;
    }

    private static void saveData(){
        SAVER.saveJson(dickStorage, DICKDATA);
    }

    public static class DickStorage{
        private final Map<Long, List<DickInfo>> DICKS = new HashMap<>();
    }
}
