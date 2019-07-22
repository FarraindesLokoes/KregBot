package nukeologist.kregbot.util;

import java.util.Random;

public class KregEffectSentences {

    private static String[] sentences = {};
    private static Random random = new Random();

    public static String getRandonSentance(){
        return sentences[random.nextInt(sentences.length)];
    }

    public static String[] getAllSentences(){
        return sentences;
    }

}
