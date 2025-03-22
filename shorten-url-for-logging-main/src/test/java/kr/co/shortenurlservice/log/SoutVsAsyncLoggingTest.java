package kr.co.shortenurlservice.log;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SoutVsAsyncLoggingTest {

    private static final int THREAD_COUNT = 10;
    private static final int LOG_COUNT = 100_000;

    public static void main(String[] args) throws InterruptedException {
        // 1. System.out.println() 테스트
        long sysoutTime = runTest(i ->
            System.out.println("[SYSOUT][" + Thread.currentThread().getName() + "] " + i)
        );

        // 2. log.info() (AsyncAppender + Console) 테스트
        long asyncLogTime = runTest(i ->
            log.info("[LOGGER][{}] {}", Thread.currentThread().getName(), i)
        );

        // 결과 출력
        Thread.sleep(5000);
        System.out.printf("System.out: %,d ms%n", sysoutTime);
        System.out.printf("Async log : %,d ms%n", asyncLogTime);
    }

    private static long runTest(StringConsumer task) throws InterruptedException {
        long start = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        for (int t = 0; t < THREAD_COUNT; t++) {
            executor.submit(() -> {
                for (int i = 0; i < LOG_COUNT; i++) {
                    task.accept(i);
                }
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        long end = System.currentTimeMillis();
        return end - start;
    }

    @FunctionalInterface
    interface StringConsumer {
        void accept(int value);
    }
}
