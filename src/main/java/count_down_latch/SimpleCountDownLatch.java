package count_down_latch;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

public class SimpleCountDownLatch {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch waitLatch = new CountDownLatch(5);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch workLatch = new CountDownLatch(5);

        System.out.println("Waiting for all processors are ready...");

        List<FileProcessor> processors = Stream.generate(() -> new FileProcessor(waitLatch, startLatch, workLatch))
                .limit(5)
                .toList();

        processors.forEach(FileProcessor::start);

        waitLatch.await();

        System.out.println("All processors are ready!");
        startLatch.countDown();

        workLatch.await();
        System.out.println("All processors finished their job!");

    }
}


class FileProcessor extends Thread {
    private CountDownLatch waitLatch;
    private CountDownLatch startLatch;
    private CountDownLatch workLatch;

    public FileProcessor(CountDownLatch waitLatch, CountDownLatch startLatch, CountDownLatch workLatch) {
        this.waitLatch = waitLatch;
        this.startLatch = startLatch;
        this.workLatch = workLatch;
    }

    @Override
    public void run() {
        try {
            processFile();
        } catch (InterruptedException e) {
            System.out.println("Processor " + Thread.currentThread().threadId() + " has an exception " + e.getMessage());
        }
    }

    private void processFile() throws InterruptedException {
        System.out.println("Processor " + Thread.currentThread().threadId() + " is waiting for other processors");
        waitLatch.countDown();

        startLatch.await();

        //do some work
        System.out.println("Processor " + Thread.currentThread().threadId() + " has processed the file!");
        workLatch.countDown();
    }
}
