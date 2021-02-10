package me.conclure.derpio.command;

final class CommandException extends RuntimeException {

  CommandException(Throwable cause) {
    super(cause.getMessage(), cause);
  }
}
