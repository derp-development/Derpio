package me.conclure.derpio.util.functional;

@FunctionalInterface
public interface ExceptionalRunnable extends Runnable {

  void runExceptionally() throws Exception;

  default void run() {
    try {
      this.runExceptionally();
    } catch (Exception e) {
      throw new ForwardingException(e);
    }
  }

  final class ForwardingException extends RuntimeException {
    private ForwardingException(Throwable cause) {
      super(cause.getMessage(), cause);
    }
  }
}
