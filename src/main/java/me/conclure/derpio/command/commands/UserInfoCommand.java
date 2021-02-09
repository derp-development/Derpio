package me.conclure.derpio.command.commands;

import me.conclure.derpio.Bot;
import me.conclure.derpio.command.CommandExecutor;
import me.conclure.derpio.storage.UserInfo;
import me.conclure.derpio.storage.UserManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public final class UserInfoCommand implements CommandExecutor {

  @Override
  public String getName() {
    return "userinfo";
  }

  @Override
  public Result execute(Bot bot, MessageReceivedEvent event, String[] args) {
    User author = event.getAuthor();
    long userId = author.getIdLong();
    UserManager userManager = bot.getUserManager();
    UserInfo userInfo = userManager.getUserInfo(userId);
    MessageEmbed embed =
        new EmbedBuilder()
            .setTitle(author.getAsTag())
            .addField("XP", String.valueOf(userInfo.getXP()), false)
            .build();

    event.getChannel().sendMessage(embed).queue();
    return Result.SUCCESS;
  }
}
