package thread_methods;

import java.util.List;
import java.util.stream.Stream;

/**
 * Simple counter with concurrent access example
 */
public class ConcurrentCounter {
	public static void main(String[] args) {
		Counter counter = new Counter();

		List<ThreadCounter> threads = Stream.generate(() -> new ThreadCounter(counter))
				.limit(30)
				.toList();

		threads.forEach(ThreadCounter::start);

//		AtomicInteger c = new AtomicInteger(0);
//		for (int i = 0; i < 10; i++) {
//			new Thread(() -> {
//				for (int j = 0; j < 10; j++) {
//					System.out.println(Thread.currentThread().getName() + ", counter=" + c.incrementAndGet());
//					try {
//						Thread.sleep(100);
//					} catch (InterruptedException e) {
//						throw new RuntimeException(e);
//					}
//				}
//			}).start();
//		}

	}
}


class Counter {
	int number = 0;
//	AtomicInteger number = new AtomicInteger(0);

	public void increment() {
		synchronized (this) {
			number++;
		}
//		number.addAndGet(1);
//		return number.incrementAndGet();
	}
}

class ThreadCounter extends Thread {
	private Counter counter;

	public ThreadCounter(Counter counter) {
		this.counter = counter;
	}


	@Override
	public void run() {
//		synchronized (counter) {
		for (int i = 0; i < 10; i++) {
//				counter.increment();
			counter.increment();
			System.out.println("Thread " + Thread.currentThread().threadId() + ", number=" + counter.number);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
//		}
	}
}
