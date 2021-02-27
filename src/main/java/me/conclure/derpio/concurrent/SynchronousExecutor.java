package me.conclure.derpio.concurrent;

import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import me.conclure.derpio.util.functional.ExceptionalRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SynchronousExecutor implements Executor {
  private static final Logger LOGGER = LoggerFactory.getLogger(SynchronousExecutor.class);

  private final Queue<Runnable> queue;
  private final DelegatedRunnable delegatedRunnable;

  public SynchronousExecutor() {
    this.queue = new LinkedBlockingQueue<>();
    this.delegatedRunnable = new DelegatedRunnable(this.queue);
    Thread thread = new Thread(this.delegatedRunnable);
    thread.setName("Bot Thread");
    thread.start();
  }

  @Override
  public void execute(Runnable command) {
    this.queue.add(command);
  }

  public void execute(ExceptionalRunnable command) {
    this.queue.add(command);
  }

  public void shutdown() {
    this.delegatedRunnable.shutdown();
  }

  public boolean isTerminated() {
    return this.delegatedRunnable.isTerminated();
  }

  public void awaitTermination() throws InterruptedException {
    this.delegatedRunnable.latch.await();
  }

  private static final class DelegatedRunnable implements Runnable {
    private static final int RUNNING = 0;
    private static final int SHUTTING_DOWN = -1;
    private static final int TERMINATED = 1;
    private final CountDownLatch latch;
    private final Queue<Runnable> queue;
    private final Object mutex = new Object();
    private volatile int terminationCode = RUNNING;

    private DelegatedRunnable(Queue<Runnable> queue) {
      this.latch = new CountDownLatch(1);
      this.queue = queue;
    }

    private void shutdown() {
      synchronized (this.mutex) {
        if (this.terminationCode == SHUTTING_DOWN || this.terminationCode == TERMINATED) {
          return;
        }
        this.terminationCode = SHUTTING_DOWN;
      }
    }

    private boolean isTerminated() {
      return this.terminationCode == TERMINATED;
    }

    @Override
    public void run() {
      while (true) {
        synchronized (this.mutex) {
          Runnable runnable = this.queue.poll();

          if (runnable == null) {
            if (this.terminationCode == SHUTTING_DOWN) {
              this.latch.countDown();
              break;
            }
            continue;
          }

          if (this.isTerminated()) {
            throw new RejectedExecutionException("Executor terminated");
          }

          try {
            runnable.run();
          } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
          }
        }
      }

      synchronized (this.mutex) {
        this.terminationCode = TERMINATED;
      }
    }
  }
}
