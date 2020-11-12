package nukeologist.kregbot.commands.drkreg;

import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.Context;
import nukeologist.kregbot.util.MessageHelper;
import nukeologist.kregbot.util.SaveHelper;
import nukeologist.kregbot.util.Tuple;

import javax.annotation.Nonnull;
import java.util.*;

public class Diagnoses {

    private static SaveHelper<Diagnoses.DiseasesStorage> SAVER = new SaveHelper<>(Diagnoses.DiseasesStorage.class);
    private static final String DISEASEDATA = "diseaseData";
    private static Diagnoses.DiseasesStorage diseasesStorage = SAVER.fromJson(DISEASEDATA);

    private static final Random random = new Random();

    @Command("diagnose")
    public static void diagnose(Context context){
        String[] words = context.getWords();

        boolean hasDisease = random.nextBoolean();

        if (words.length<=1) {
            if (hasDisease) {
                context.send("I diagnose you with " + generateDiagnose(context));
            } else {
                context.send("Looks like you're clear.");
            }
        } else {
            if (words[1].equals("add")) {
                if (words.length <= 2) {
                    context.reply("write something to add...");
                } else {
                    addDisease(context.getMember().getGuild().getIdLong(), words[2], words.length > 3 ? MessageHelper.collapse(words, 3) : "");
                    context.send("Added.");
                    saveData();
                }
                return;
            }

            if (hasDisease) {
                context.send("I diagnose " + words[1] + " with " + generateDiagnose(context));
            } else {
                context.send("Looks like "+words[1]+" is clear.");
            }
        }
    }

    private static void addDisease(long guild, String disease, String cure){
        if (!diseasesStorage.DISEASES.containsKey(guild)) {
            diseasesStorage.DISEASES.put(guild, new ArrayList<>());
        }
        diseasesStorage.DISEASES.get(guild).add(new Tuple<>(disease, cure));
    }

    private static String generateDiagnose(Context context) {
        Tuple<String, String> disease = getDisease(context);
        return disease.a + ".\n" + ((disease.b.equals("")) ? "I'm sorry to say, but it can't be cured." : "You are a lucky one, it can be cured " + disease.b);
    }

    @Nonnull
    private static Tuple<String, String> getDisease(Context context) {
        boolean empty = getDiseaseList(context.getMember().getGuild().getIdLong()).isEmpty();
        if (empty) return new Tuple<>("DUMB", "by adding something to the diagnose list. '!diagnose add <disease> <cure(optional)>'");
        return getDiseaseList(context.getMember().getGuild().getIdLong()).get(random.nextInt(getDiseaseList(context.getMember().getGuild().getIdLong()).size()));
    }

    private static List<Tuple<String, String>> getDiseaseList(long guild){
        if (!diseasesStorage.DISEASES.containsKey(guild)) {
            diseasesStorage.DISEASES.put(guild, new ArrayList<>());
        }
        return diseasesStorage.DISEASES.get(guild);
    }

    private static void saveData(){
        SAVER.saveJson(diseasesStorage, DISEASEDATA);
    }

    public static class DiseasesStorage {
        private final Map<Long, List<Tuple<String, String>>> DISEASES = new HashMap<>();
    }

}
