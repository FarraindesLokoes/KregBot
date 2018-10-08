package com.nukeologist.archbot;

import com.nukeologist.archbot.commands.Commands;
import com.nukeologist.archbot.constants.BotTokens;
import com.nukeologist.archbot.events.HelloEvent;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.security.auth.login.LoginException;

@SpringBootApplication
public class ArchBot {

    private static JDA bot;

    public static void main (String[] args) throws LoginException, InterruptedException {
        try {

            bot = new JDABuilder(AccountType.BOT).setToken(BotTokens.discordToken).setGame(Game.playing("!help for help")).build().awaitReady();
            bot.addEventListener(new HelloEvent());
            bot.addEventListener(new Commands());


        }catch (LoginException e) {
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
