package nukeologist.kregbot.util;

/**
 * Utility class for messages.
 *
 * @author Nukeologist
 */
public class MessageHelper {

    /**
     * Makes the String not contain @everyone and @here
     *
     * @param message the string
     */
    public static String sanitizeEveryone(String message) {
        return message.replaceAll("@here", "everyone").replaceAll("@everyone", "everyone");
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
     * Collapse a String array into a single String
     *
     * @param stringArr String Array to collapse.
     * @param start starting index -- 0
     * @param end ending index -- array.length
     * @return Entire array into a single String
     * */
    public static String collapse(String[] stringArr, int start, int end){
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



}
