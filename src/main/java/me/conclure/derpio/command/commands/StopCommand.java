package me.conclure.derpio.command.commands;

import me.conclure.derpio.Bot;
import me.conclure.derpio.BotInfo;
import me.conclure.derpio.command.CommandExecutor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public final class StopCommand implements CommandExecutor {

  @Override
  public String getName() {
    return "stop";
  }

  @Override
  public Result execute(Bot bot, MessageReceivedEvent event, String[] args) {
    if (event.getAuthor().getIdLong() != BotInfo.OWNER_ID) {
      return Result.NO_PERMISSION;
    }
    event.getChannel().sendMessage("Shutting down...").complete();
    bot.shutdown();
    return Result.SUCCESS;
  }
}
