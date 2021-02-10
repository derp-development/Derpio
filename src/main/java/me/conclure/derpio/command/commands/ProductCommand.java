package me.conclure.derpio.command.commands;

import me.conclure.derpio.Bot;
import me.conclure.derpio.command.CommandExecutor;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public final class ProductCommand extends CommandExecutor {

  @Override
  protected String getName() {
    return "product";
  }

  @Override
  protected Result execute(Bot bot, GuildMessageReceivedEvent event, String[] args) {
    if (args.length == 0) {
      return ResultType.INVALID_ARGUMENT.toResult();
    }

    String productLabel = args[0];


    return null;
  }
}
