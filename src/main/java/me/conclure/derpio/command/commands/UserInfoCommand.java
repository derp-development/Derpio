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
  protected String[] getAliases() {
    return new String[] {"userdata", "user"};
  }

  @Override
  public Result execute(Bot bot, GuildMessageReceivedEvent event, String[] args) {
    bot.awaitReady();

    User author = event.getAuthor();
    long userId = author.getIdLong();
    UserManager userManager = bot.userManager();
    UserData userData = userManager.getUserData(userId);
    MessageEmbed embed =
        new EmbedBuilder()
            .setTitle(author.getAsTag())
            .addField("Exp", String.valueOf(userData.getExp()), false)
            .build();

    return ResultType.SUCCESS
        .toResult()
        .setCallback(
            () -> {
              event.getChannel().sendMessage(embed).queue();
            });
  }
}
