package nukeologist.kregbot.util;

import java.util.regex.Pattern;

/**
 * Shamelessly copied from https://github.com/tterrag1098/K9/blob/master/src/main/java/com/tterrag/k9/util/Patterns.java
 *
 * @author Nukeologist
 */
public class Constants {

    public static final Pattern INCREMENT_DECREMENT = Pattern.compile("^(\\S+)(\\+\\+|--)$");

    public static final Pattern ONEROLL = Pattern.compile("d\\d+");
    public static final Pattern ANYROLL = Pattern.compile("\\d+d\\d+");
    public static final Pattern GETINTEGER = Pattern.compile("^\\D*?(-?\\d+).*$");

    public static final Pattern HERE = Pattern.compile("@here");
    public static final Pattern EVERYONE = Pattern.compile("@everyone");

    public static final Pattern PUNCTUATION = Pattern.compile("[!,.*]");


    public static class Emotes {
        public static class FarraindesLokoes {
            public static final String ItsApproved = "ItsApproved:233361278372937728";
            public static final String TopKek = "TopKek:370670500600479744";
            public static final String Jebaited = "Jebaited:395648740222042112";
        }
        public static class Normal {
            public static final String ThumbsUp = "U+1F44D";
            public static final String ThumbsDown = "U+1F44E";
        }
    }

    public static class Symbols {
        public static final char DOUBLE_VERTICAL_BAR = '║';
        public static final char DOUBLE_HORIZONTAL_BAR = '═';


        public static final char DOUBLE_TOP_LEFT_CORNER = '╔';
        public static final char DOUBLE_TOP_RIGHT_CORNER = '╗';
        public static final char DOUBLE_BOTTOM_LEFT_CORNER = '╚';
        public static final char DOUBLE_BOTTOM_RIGHT_CORNER = '╝';
    }
}
