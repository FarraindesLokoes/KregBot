package nukeologist.kregbot;

import javax.security.auth.login.LoginException;
import java.io.IOException;

/**
 * @author Nukeologist
 */
public class Main {
    public static void main(String[] args) throws LoginException, IOException {
        KregBot.INSTANCE.init();
    }

}
