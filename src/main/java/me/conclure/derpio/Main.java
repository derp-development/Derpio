package me.conclure.derpio;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Main {

  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    OptionParser parser =
        new OptionParser() {
          {
            this.accepts("token", "Bot token").withRequiredArg().required();
          }
        };

    OptionSet options = null;

    try {
      options = parser.parse(args);
    } catch (OptionException e) {
      LOGGER.error("", e);
    }

    Main.start(options);
  }

  private static void start(OptionSet optionSet) {
    try {
      Bot.init(optionSet);
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }
}
