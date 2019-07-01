package com.farraindeslokoes.kregbot.commands;

import com.farraindeslokoes.kregbot.KregBot;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DeleteCommand implements ICommand {

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] toDo) {
        if (event.getAuthor().isBot()) return;

        if (toDo.length == 1) {
            event.getChannel().sendMessage("delete what?").queue();
            return;
        }

        List<Permission> perms = event.getMember().getPermissions();

        if (perms.contains(Permission.ADMINISTRATOR)) {
            deleteRowInDatabase(toDo[1]);
            event.getChannel().sendMessage("Done.").queue();
        } else {
            event.getChannel().sendMessage("You dont have admin privileges.").queue();
        }


    }

    @Override
    public void executePrivate(PrivateMessageReceivedEvent event, String[] toDo) {
        event.getChannel().sendMessage("Does not work in private.").queue();
    }

    @Override
    public String getHelpString() {
        return "Deletes from the counter database. Works only in the server, and for admins only";
    }

    private void deleteRowInDatabase(String message) {
        String SQL = "DELETE FROM increments WHERE message = ?";

        Connection conn = KregBot.getSQLConnection();

        try {

            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, message);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
