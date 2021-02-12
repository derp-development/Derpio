package me.conclure.derpio.command.commands;

import java.util.concurrent.TimeUnit;
import me.conclure.derpio.Bot;
import me.conclure.derpio.command.CommandExecutor;
import me.conclure.derpio.model.user.UserData;
import me.conclure.derpio.model.user.UserManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public final class DailyCommand extends CommandExecutor {

  private String format(long durationMillis) {
    long hours = TimeUnit.MILLISECONDS.toHours(durationMillis);
    long hoursMillis = TimeUnit.HOURS.toMillis(hours);

    long minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis - hoursMillis);
    long minutesMillis = TimeUnit.MINUTES.toMillis(minutes);

    long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis - minutesMillis - hoursMillis);

    return String.format("%dh %dm %ds", hours, minutes, seconds);
  }

  @Override
  protected String getName() {
    return "daily";
  }

  @Override
  protected Result execute(Bot bot, GuildMessageReceivedEvent event, String[] args) {
    bot.awaitReady();

    User sender = event.getAuthor();
    UserManager userManager = bot.getUserManager();
    long userId = sender.getIdLong();
    UserData userData = userManager.getUserData(userId);
    TextChannel channel = event.getChannel();
    long timeUntil = userData.claimDailyExp();

    if (timeUntil == 0L) {
      channel.sendMessage("xp").queue();
    } else {
      channel.sendMessage(this.format(timeUntil)).queue();
    }

    return ResultType.SUCCESS.toResult();
  }
}
