package me.conclure.derpio.command.commands;

import me.conclure.derpio.Bot;
import me.conclure.derpio.command.CommandExecutor;
import me.conclure.derpio.model.user.UserData;
import me.conclure.derpio.model.user.UserManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public final class UserInfoCommand extends CommandExecutor {

  @Override
  public String getName() {
    return "userinfo";
  }

  @Override
  public Result execute(Bot bot, GuildMessageReceivedEvent event, String[] args) {
    User author = event.getAuthor();
    long userId = author.getIdLong();
    UserManager userManager = bot.getUserManager();
    UserData userData = userManager.getUserInfo(userId);
    MessageEmbed embed =
        new EmbedBuilder()
            .setTitle(author.getAsTag())
            .addField("XP", String.valueOf(userData.getXP()), false)
            .build();

    return ResultType.SUCCESS
        .toResult()
        .setCallback(
            () -> {
              event.getChannel().sendMessage(embed).queue();
            });
  }
}
