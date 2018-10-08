package com.nukeologist.archbot;

import com.nukeologist.archbot.commands.Commands;
import com.nukeologist.archbot.constants.Constants;
import com.nukeologist.archbot.events.HelloEvent;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;

import javax.security.auth.login.LoginException;

@SpringBootApplication
public class ArchBot {

    private static JDA bot;

    public static void main (String[] args) {
        SpringApplication.run(ArchBot.class, args);
        Discord ();
    }

    private static void Discord(){
        try {

            bot = new JDABuilder(AccountType.BOT).setToken(Constants.discordToken).setGame(Game.playing("!help for help")).build().awaitReady();
            bot.addEventListener(new HelloEvent());
            bot.addEventListener(new Commands());


        }catch (LoginException e) {
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return (container -> {
            container.setContextPath("");
            container.setPort(Integer.valueOf(System.getenv("PORT")));
        });
    }



}
