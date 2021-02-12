package me.conclure.derpio.command.commands;

import me.conclure.derpio.Bot;
import me.conclure.derpio.BotInfo;
import me.conclure.derpio.command.CommandExecutor;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public final class RestartCommand extends CommandExecutor {

  @Override
  protected String getName() {
    return "restart";
  }

  @Override
  protected Result execute(Bot bot, GuildMessageReceivedEvent event, String[] args) {
    if (!bot.isReady()) {
      return ResultType.BAD_TIMING.toResult();
    }

    if (event.getAuthor().getIdLong() != BotInfo.OWNER_ID) {
      return ResultType.NO_PERMISSION.toResult();
    }

    event.getChannel().sendMessage("Restarting...").complete();
    long channelId = event.getChannel().getIdLong();

    return ResultType.SUCCESS
        .toResult()
        .setCallback(
            () ->
                bot.restart(
                    jda -> {
                      TextChannel channel = jda.getTextChannelById(channelId);

                      if (channel == null) {
                        return;
                      }

                      channel.sendMessage("Restart complete!").queue();
                    }));
  }
}
