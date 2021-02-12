package me.conclure.derpio.command.commands;

import me.conclure.derpio.Bot;
import me.conclure.derpio.BotInfo;
import me.conclure.derpio.command.CommandExecutor;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public final class StopCommand extends CommandExecutor {

  @Override
  public String getName() {
    return "stop";
  }

  @Override
  public Result execute(Bot bot, GuildMessageReceivedEvent event, String[] args) {
    if (!bot.isReady()) {
      return ResultType.BAD_TIMING.toResult();
    }

    if (event.getAuthor().getIdLong() != BotInfo.OWNER_ID) {
      return ResultType.NO_PERMISSION.toResult();
    }

    event.getChannel().sendMessage("Shutting down...").complete();

    return ResultType.SUCCESS.toResult().setCallback(bot::shutdown);
  }
}
