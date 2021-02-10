package me.conclure.derpio.command.commands;

import me.conclure.derpio.Bot;
import me.conclure.derpio.command.CommandExecutor;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public final class HelpCommand extends CommandExecutor {

  @Override
  public String getName() {
    return "help";
  }

  @Override
  public Result execute(Bot bot, GuildMessageReceivedEvent event, String[] args) {
    event.getChannel().sendMessage("niffa").queue();
    return ResultType.SUCCESS.toResult();
  }
}
