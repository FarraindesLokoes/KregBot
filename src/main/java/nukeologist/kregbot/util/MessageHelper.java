package nukeologist.kregbot.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * Utility class for messages.
 *
 * @author Nukeologist
 * @author SpicyFerret
 */
public class MessageHelper {

    /**
     * Makes the String not contain @everyone and @here
     *
     * @param message the string
     */
    public static String sanitizeEveryone(String message) {
        Matcher matcher1 = Constants.EVERYONE.matcher(message);
        Matcher matcher2 = Constants.HERE.matcher(message);
        while (matcher1.find()) {
            message = matcher1.replaceAll("everyone");
            matcher1 = Constants.EVERYONE.matcher(message);
        }
        while (matcher2.find()) {
            message = matcher2.replaceAll("here");
            matcher2 = Constants.HERE.matcher(message);
        }
        return message;
    }

    /**
     * Makes a String message italicized. This only applies to chat.
     *
     * @param message The message to format.
     * @return String The message with the formatting codes applied.
     */
    public static String makeItalic(String message) {

        return "*" + message + "*";
    }

    /**
     * Makes a String message bold. This only applies to chat.
     *
     * @param message The message to format.
     * @return String The message with the bold formatting codes applied.
     */
    public static String makeBold(String message) {

        return "**" + message + "**";
    }

    /**
     * Makes a String message scratched out. This only applies to chat.
     *
     * @param message The message to format.
     * @return String The message with the scratched out formatting codes applied.
     */
    public static String makeScratched(String message) {

        return "~~" + message + "~~";
    }

    /**
     * Makes a String message underlined. This only applies to chat.
     *
     * @param message The message to format.
     * @return String The message with the underlined formatting codes applied.
     */
    public static String makeUnderlined(String message) {

        return "__" + message + "__";
    }

    /**
     * Makes a String message appear in a code block. This only applies to chat.
     *
     * @param message The message to format.
     * @return String The message with the code block format codes applied.
     */
    public static String makeCodeBlock(String message) {

        return "`" + message + "`";
    }

    /**
     * Makes a String message appear in a multi-lined code block. This only applies to chat.
     *
     * @param message The message to format.
     * @return String The message with the multi-lined code block format codes applied.
     */
    public static String makeMultiCodeBlock(String message) {

        return "```" + message + "```";
    }

    public static String makeHyperlink(String text, String url) {

        return String.format("[%s](%s)", text, url);
    }

    /**
     * Puts a String text in quotes.
     *
     * @param text The message to format.
     * @return String The text with the quotes around it.
     */
    public static String quote(String text) {

        return "\"" + text + "\"";
    }

    /**
     * Split text into n number of characters.
     *
     * @param text the text to be split.
     * @param size the split size.
     * @return an array of the split text.
     */
    private static String[] splitToNChar(String text, int size) {
        List<String> parts = new ArrayList<>();

        int length = text.length();
        for (int i = 0; i < length; i += size) {
            parts.add(text.substring(i, Math.min(length, i + size)));
        }
        return parts.toArray(new String[0]);
    }

    /**
     * Collapse a String array into a single String
     *
     * @param stringArr String Array to collapse.
     * @param start     starting index -- 0
     * @param end       ending index -- array.length
     * @return Entire array into a single String
     */
    @SuppressWarnings("WeakerAccess")
    public static String collapse(String[] stringArr, int start, int end) {
        StringBuilder str = new StringBuilder();
        for (; start < end; start++) {
            str.append(stringArr[start]).append(" ");
        }
        return str.toString();
    }

    public static String collapse(String[] stringArr, int start) {
        return collapse(stringArr, start, stringArr.length);
    }

    public static String collapse(String[] stringArr) {
        return collapse(stringArr, 0, stringArr.length);
    }

    /**
     * Gets the longest String in the set
     *
     * @param set the set of strings
     * @return longest string
     */
    public static String getLongestString(Set<String> set) {
        return set.stream().max(Comparator.comparingInt(String::length)).get();
    }

    public static String makeSpoiler(String message) {
        return "||" + message + "||";
    }
}
