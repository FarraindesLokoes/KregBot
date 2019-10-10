package nukeologist.kregbot.commands;

import net.dv8tion.jda.api.entities.User;
import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.CommandHelp;
import nukeologist.kregbot.api.Context;
import nukeologist.kregbot.data.ImageEditor;
import nukeologist.kregbot.util.CommonMessagesReplays;
import nukeologist.kregbot.util.Tuple;

import java.awt.*;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ImageEditorCommand {

    private static Map<User, Tuple<Date, ImageEditor>> imageMap = new HashMap<>();

    private static final Thread CLEANER_THREAD = new Thread(()-> {
        Date curDate = new Date();
        while (Thread.currentThread().isAlive()) {

            curDate.setTime(System.currentTimeMillis());
            for (Map.Entry<User, Tuple<Date,ImageEditor>> entry: imageMap.entrySet()) {
                if (entry.getValue().a.before(curDate)) { //remove data
                    imageMap.remove(entry);
                }
            }

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
    static {
        CLEANER_THREAD.start();
    }

    @Command("Image")
    public static void imageEditor(Context context) throws IOException {
        String[] words = context.getWords();

        if (words.length < 2) {
            CommonMessagesReplays.syntaxError(context, "action not specified", () -> imageEditorHelp(context));
        } else {
            User user = context.getAuthor();
            switch (words[1].toLowerCase()) {
                case "create":
                    if (words.length < 3) { //dimensions not specified
                        CommonMessagesReplays.syntaxError(context, "missing dimensions", () -> CommonMessagesReplays.embedMessage(context, CREATE_HELP));
                    } else { // has a 3rd param
                        String[] dimensions = words[2].split("x");
                        if (dimensions.length != 2) { //has a problem
                            CommonMessagesReplays.syntaxError(context, "dimensions problem", () -> CommonMessagesReplays.embedMessage(context, CREATE_HELP));
                            return;
                        } else if (dimensions[0].chars().allMatch(Character::isDigit) && dimensions[1].chars().allMatch(Character::isDigit)) { // all Digits
                            int width = Integer.parseInt(dimensions[0]), height = Integer.parseInt(dimensions[1]);
                            if (width * height > 2073600) { // size exceeded
                                CommonMessagesReplays.syntaxError(context, "size exceeded");
                                return;
                            } else { // all good
                                user = context.getAuthor();
                                if (imageMap.get(user) != null) { // already has a image -> replace
                                    imageMap.get(user).a.setTime(System.currentTimeMillis() + 180000);

                                    imageMap.get(user).b = ImageEditor.createImage(width, height).display(context);
                                } else { // create new image
                                    imageMap.put(user, new Tuple<>(new Date(System.currentTimeMillis() + 180000), ImageEditor.createImage(width, height).display(context)));
                                }
                            }
                        } else {//has letters mixed
                            CommonMessagesReplays.syntaxError(context, "non digit parameter", () -> CommonMessagesReplays.embedMessage(context, CREATE_HELP));
                            return;
                        }
                    }
                    break;
                case "clear":
                    if (imageMap.get(user) != null) {
                        imageMap.get(user).a.setTime(System.currentTimeMillis() + 180000);
                        imageMap.get(user).b.clear().display(context);
                    } else {
                        CommonMessagesReplays.syntaxError(context, "No image found");
                        return;
                    }
                    break;
                case "draw":
                    if (words.length < 3) { // Form not specified
                        CommonMessagesReplays.syntaxError(context, "form not specified", () -> CommonMessagesReplays.embedMessage(context, DRAW_HELP));
                        return;
                    } else if (imageMap.get(user) == null) {
                        CommonMessagesReplays.syntaxError(context, "image not created", () -> CommonMessagesReplays.embedMessage(context, CREATE_HELP));
                    } else {
                        if (words.length < 4) {
                            CommonMessagesReplays.syntaxError(context, "coordinates not specified");
                        }
                        int[] coordinates;
                        int[] size;
                        int color;
                        switch (words[2].toLowerCase()) {
                            case "point":
                                coordinates = getCoordinates(words[3]);
                                if (coordinates == null) {
                                    CommonMessagesReplays.syntaxError(context, "unknown coordinates", () -> CommonMessagesReplays.embedMessage(context, DRAW_HELP));
                                    return;
                                }
                                imageMap.get(user).a.setTime(System.currentTimeMillis() + 180000);
                                if (words.length == 5) {
                                    color = Integer.parseInt(words[4].replace("#", ""), 16);
                                } else {
                                    color = 0;
                                }
                                imageMap.get(user).b.drawPoint(coordinates[0], coordinates[1], color).display(context);
                                break;
                            case "line":
                                if (words.length < 5) {
                                    CommonMessagesReplays.syntaxError(context, "second coordinates not specified");
                                    return;
                                }
                                coordinates = getCoordinates(words[3]);
                                size = getCoordinates(words[4]);
                                if (coordinates == null || size == null) {
                                    CommonMessagesReplays.syntaxError(context, "unknown coordinates", () -> CommonMessagesReplays.embedMessage(context, DRAW_HELP));
                                    return;
                                }
                                imageMap.get(user).a.setTime(System.currentTimeMillis() + 180000);
                                if (words.length == 6) {
                                    color = Integer.parseInt(words[5].replace("#", ""), 16);
                                } else {
                                    color = 0;
                                }
                                imageMap.get(user).b.drawLine(coordinates[0], coordinates[1], size[0], size[1], new Color(color)).display(context);
                                break;
                            case "rectangle":
                            case "rect":
                                if (words.length < 5) {
                                    CommonMessagesReplays.syntaxError(context, "WIDTHxHEIGHT not specified");
                                    return;
                                }
                                coordinates = getCoordinates(words[3]);
                                size = getCoordinates(words[4]);
                                if (coordinates == null || size == null) {
                                    CommonMessagesReplays.syntaxError(context, "unknown coordinates", () -> CommonMessagesReplays.embedMessage(context, DRAW_HELP));
                                    return;
                                }
                                imageMap.get(user).a.setTime(System.currentTimeMillis() + 180000);
                                if (words.length == 6) {
                                    color = Integer.parseInt(words[5].replace("#", ""), 16);
                                } else {
                                    color = 0;
                                }
                                imageMap.get(user).b.drawRect(coordinates[0], coordinates[1], size[0], size[1], new Color(color)).display(context);
                                break;
                            case "ellipse":
                                if (words.length < 5) {
                                    CommonMessagesReplays.syntaxError(context, "WIDTHxHEIGHT not specified");
                                    return;
                                }
                                coordinates = getCoordinates(words[3]);
                                size = getCoordinates(words[4]);
                                if (coordinates == null || size == null) {
                                    CommonMessagesReplays.syntaxError(context, "unknown coordinates", () -> CommonMessagesReplays.embedMessage(context, DRAW_HELP));
                                    return;
                                }
                                imageMap.get(user).a.setTime(System.currentTimeMillis() + 180000);
                                if (words.length == 6) {
                                    color = Integer.parseInt(words[5].replace("#", ""), 16);
                                } else {
                                    color = 0;
                                }
                                imageMap.get(user).b.drawEllipse(coordinates[0], coordinates[1], size[0], size[1], new Color(color)).display(context);
                                break;
                        }
                    }
                    break;
            }
        }

    }

    private static int[] getCoordinates(String str){
        String[] coordinates = str.split("x");
        if (coordinates.length != 2 || !coordinates[0].chars().allMatch(Character::isDigit) || !coordinates[1].chars().allMatch(Character::isDigit)){
            return null;
        }
        return new int[]{Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])};
    }

    @CommandHelp("Image")
    public static void imageEditorHelp(Context context) {
        CommonMessagesReplays.embedMessage(context, "Make me draw stuff for you. Usage: \n\n" +
                CREATE_HELP + "\n\n" + CLEAR_HELP + "\n\n" + DRAW_HELP + "\n\n" +
                "obs.: images will be removed from our database if no changes are made within the last 3 minutes.");
    }

    private static final String CREATE_HELP = "!Image Create [DIEMSIONS(maximum of 2.073.600 pixels)]\n" +
            "\tCreates a black image\n" +
            "\texample: [!Image Create 1920x1080]";

    private static final String CLEAR_HELP = "!Image Clear\n" +
            "\tClears image\n" +
            "\texample: [!Image Clear]";


    private static final String DRAW_HELP = "!Image Draw [FORM] [FORM PARAMETER(S)] [(optional) COLOR]\n" +
            "\tDraws a specified form on image\n" +
            "\tForms and parameters:\n" +
            "\t\tPoint [COORDINATES]\n" +
            "\t\tLine [COORDINATES1] [COORDINATES2]\n" +
            "\t\tRectangle(or Rect) [TOPLEFT_COORDINATES] [WIDTHxHEIGHT]\n" +
            "\t\tEllipse [TOPLEFT_COORDINATES] [WIDTHxHEIGHT]\n" +
            "\tColor: [Hexadecimal]\n" +
            "\texample: !Image Draw Circle 100x500 150 200x50x175";
}
