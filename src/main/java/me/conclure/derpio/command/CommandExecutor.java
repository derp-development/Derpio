package me.conclure.derpio.command;

import me.conclure.derpio.Bot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class CommandExecutor {
  public static Result DEFAULT_RESULT = new Result(ResultType.UNEXPECTED_ERROR);

  protected abstract String getName();

  protected abstract Result execute(Bot bot, MessageReceivedEvent event, String[] args);

  protected enum ResultType {
    UNKNOWN_ARGUMENT,
    INVALID_ARGUMENT,
    SUCCESS,
    UNEXPECTED_ERROR,
    NO_PERMISSION;

    public Result toResult(String... args) {
      return new Result(this, args);
    }
  }

  protected static final class Result {
    private final ResultType resultType;
    private final String[] args;

    private Result(ResultType resultType, String... args) {
      this.resultType = resultType;
      this.args = args;
    }

    ResultType getType() {
      return resultType;
    }

    String[] getArgs() {
      return args;
    }
  }

  static final class Exception extends RuntimeException {

    Exception(Throwable cause) {
      super(cause.getMessage(), cause);
    }
  }
}
