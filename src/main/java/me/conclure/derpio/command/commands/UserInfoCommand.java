package me.conclure.derpio.command.commands;

import me.conclure.derpio.Bot;
import me.conclure.derpio.command.CommandExecutor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public final class UserInfoCommand extends CommandExecutor {

  @Override
  public String getName() {
    return "userinfo";
  }

  @Override
  public Result execute(Bot bot, GuildMessageReceivedEvent event, String[] args) {
    var author = event.getAuthor();
    var userId = author.getIdLong();
    var userManager = bot.getUserManager();
    var userData = userManager.getUserInfo(userId);
    var embed =
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
