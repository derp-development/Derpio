package me.conclure.derpio.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;

public final class ExecutorServiceUtil {

  private ExecutorServiceUtil() {
    throw new AssertionError();
  }

  public static void awaitTermination(ExecutorService executorService) throws InterruptedException {
    executorService.shutdown();
    if (!executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
      executorService.shutdownNow();
    }
  }

  public static void awaitTermination(ExecutorService executorService, Logger logger) {
    try {
      ExecutorServiceUtil.awaitTermination(executorService);
    } catch (InterruptedException e) {
      logger.error(e.getMessage(), e);
    }
  }
}
