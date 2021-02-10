package me.conclure.derpio.command.commands.ticket;

import me.conclure.derpio.Bot;
import me.conclure.derpio.command.CommandExecutor;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public final class NewTicketCommand extends CommandExecutor {

  @Override
  protected String getName() {
    return "new";
  }

  @Override
  protected Result execute(Bot bot, GuildMessageReceivedEvent event, String[] args) {
    if (args.length == 0) {
      return ResultType.MISSING_ARGUMENT.toResult();
    }

    String ticketName = args[0];

    event.getChannel().sendMessage(ticketName).queue();
    return ResultType.SUCCESS.toResult();
  }
}
