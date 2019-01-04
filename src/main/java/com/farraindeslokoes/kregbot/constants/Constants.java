package com.farraindeslokoes.kregbot.constants;

/** Contem constantes, tanto do ambiente como possivelmente outras.
 * @since 0.1
 * @author Nukeologist
 */
public class Constants {

    /*IMPORTANTISSIMO. Token conecta o bot ao discord */
    public static final String discordToken = System.getenv("BOT_TOKEN");

    /*Util para o comando !wolfram. */
    public static final String wolframAPIKey = System.getenv("WOLFRAM_KEY");
}
