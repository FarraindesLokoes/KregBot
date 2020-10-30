package nukeologist.kregbot.commands.miniGames;

import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.CommandHelp;
import nukeologist.kregbot.api.Context;
import nukeologist.kregbot.util.MessageHelper;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;

public class Bomb implements ActionListener {

    private static final Bomb instance = new Bomb();

    private final Timer timer;
    private int explosionTime = 0;
    private boolean activated;
    private final String[] passwordArr;
    private final char[] letters = "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray();
    private final int[][] password = new int[4][2];
    private final StringBuilder passwordStr;
    private final Random rand = new Random();

    private Context context;

    private Bomb(){
        this.timer = new Timer(60000, this);
        activated = false;
        passwordArr = new String[10];
        passwordStr = new StringBuilder();
    }

    public static Bomb getInstance() {
        return instance;
    }

    private void activate(Context context) {

        this.context = context;

        this.activated = true;
        this.explosionTime = (rand.nextInt(24) + 1) * 5;

        for (int i = 0; i < 10; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < 10; j++) {
                sb.append(letters[rand.nextInt(26)]);
            }
            passwordArr[i] = sb.toString();
        }
        passwordStr.delete(0, passwordStr.length());
        for (int i = 0; i < password.length; i++) {
            password[i][0] =  rand.nextInt(10);
            password[i][1] =  rand.nextInt(10);
            passwordStr.append(passwordArr[password[i][0]].charAt(password[i][1]));
            password[i][0]++;
            password[i][1]++;
        }

        timer.start();
    }

    private boolean deactivate(String password){
        if (password.equals(this.passwordStr.toString())){
            timer.stop();
            explosionTime = 0;
            activated = false;
            return true;
        } else {
            return false;
        }
    }

    private void sendPassword(Context context){
        context.send(MessageHelper.makeCodeBlock(this.passwordArr[0] + "\n" + this.passwordArr[1] + "\n" + this.passwordArr[2] + "\n" + this.passwordArr[3] + "\n" + this.passwordArr[4] + "\n" + this.passwordArr[5] + "\n" + this.passwordArr[6] + "\n" + this.passwordArr[7] + "\n" + this.passwordArr[8] + "\n" + this.passwordArr[9] + "\n"));
        context.send(Arrays.deepToString(this.password));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (explosionTime > 0) {
            explosionTime--;
        } else { //Kaboom
            timer.stop();
            explosionTime = 0;
            context.send("@here TERRORIST WINS!");
        }
    }

    @Command("bomb")
    public static void bomb(Context context) {
        String[] words = context.getWords();

        if (words.length <= 1) {
            context.reply("Please specify an action: " + (getInstance().activated ? "defuse or password." : "plant."));
        } else {
            switch (words[1]){
                case "plant":
                    if (getInstance().activated) {
                        context.reply("Bomb already activated.\n To defuse it '!bomb defuse <password>'");
                    } else {
                        getInstance().activate(context);
                        context.send("The Bomb Has Been Planted!\n The Bomb will explode in " + getInstance().explosionTime + " minutes\n To defuse '!bomb defuse <password>'");
                    }
                    break;
                case "defuse":
                    if (!getInstance().activated) {
                        context.reply("The Bomb has not been planted.\n To plant it '!bomb plant'");
                    } else {
                        if (words.length >= 3) {
                            boolean successes = getInstance().deactivate(words[2]);
                            if (successes) {
                                context.send("The Bomb Has Been Defused!" + (context.getAuthor().getIdLong() == getInstance().context.getAuthor().getIdLong() ? "\n Are You Dumb, m8?" : "Counter Terrorist Wins!\n Nice try " + getInstance().context.getAuthor().getAsTag()));
                            } else {
                                context.reply("Password Denied!");
                            }

                        } else {
                            context.reply("Password required\n The Bomb will explode in " + getInstance().explosionTime + " minutes\n To defuse '!bomb defuse <password>'");
                        }
                    }
                    break;
                case "password":
                    if (!getInstance().activated) {
                        context.reply("The Bomb has not been planted.\n To plant it '!bomb plant'");
                    } else {
                        getInstance().sendPassword(context);
                    }
                    break;
                default:
                    context.reply("Unknown action, try " + (getInstance().activated ? "defuse or password." : "plant."));
                    break;
            }
        }

    }

    @CommandHelp("bomb")
    public static void bombHelp(Context context) {
        context.send("Make Kreg plant and defuse bombs for you. Usage: \n" +
                "'!bomb plant' to plant the bomb\n" +
                "'!bomb defuse <password>' to defuse the bomb\n" +
                "'!bomb password' to get a note to discover the password");
    }
}
