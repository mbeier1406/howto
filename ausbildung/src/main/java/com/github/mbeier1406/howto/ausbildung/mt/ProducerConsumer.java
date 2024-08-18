package com.github.mbeier1406.howto.ausbildung.mt;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Beispiellösung des Consumer-Producer-Problems mit Hilfe von {@linkplain Semaphore}n.
 * @author mbeier
 */
public class ProducerConsumer {

	public static final Logger LOGGER = LogManager.getLogger(ProducerConsumer.class);

	/** Anzahl der Producer-Threads ist {@value} */
	public static final int NUM_PRODUCER = 3;

	/** Anzahl der Consumer-Threads ist {@value} */
	public static final int NUM_CONSUMER = 4;


	/**
	 * Das Interface zu den jeweiligen Producer/Consumer-Lösungen.
	 * Aktuell beschränkt auf {@linkplain Integer}.
	 */
	public static interface Item {
		/** Produziert ein Item */
		public void produce();
		/** Liefert ein Item */
		public Integer consume() throws IllegalAccessException;
	}


	/**
	 * Bei dieser Lösung kann immer nur <u>ein Item</u> produziert und konsumiert werden, D. h.
	 * es kann jeweils nur ein Thread gleichzeitig arbeiten.
	 */
	public static class SingleItem implements Item {
		private Semaphore consumerSemaphore = new Semaphore(1), producerSemaphore = new Semaphore(0);
		private Random random = new Random();
		private Integer item = null;

		/** {@inheritDoc} */
		@Override
		public void produce() {
			try {
				LOGGER.trace("{}: Semaphore holen...", Thread.currentThread().getName());
				this.consumerSemaphore.acquire();
				this.item = this.random.nextInt();
				LOGGER.trace("{}: Produziert: {}.", Thread.currentThread().getName(), this.item);
				try { Thread.sleep(500); } catch (InterruptedException e) { }
				this.producerSemaphore.release();
				LOGGER.trace("{}: fertig.", Thread.currentThread().getName());
			}
			catch (InterruptedException e) {
			}
		}

		/** {@inheritDoc} */
		@Override
		public Integer consume() throws IllegalAccessException {
			try {
				LOGGER.trace("{}: Semaphore holen...", Thread.currentThread().getName());
				this.producerSemaphore.acquire();
				LOGGER.trace("{}: Konsumiert: {}.", Thread.currentThread().getName(), this.item);
				this.consumerSemaphore.release();
				LOGGER.trace("{}: fertig.", Thread.currentThread().getName());
				return this.item;
			}
			catch (InterruptedException e) {
				return null;
			}
		}
	}


	/**
	 * Bei dieser Lösung können <u>zwei Items</u> parallel produziert und konsumiert werden.
	 */
	public static class MultipleItem implements Item {
		private static final int ANZ_PARALLEL = 2;
		private Semaphore consumerSemaphore = new Semaphore(ANZ_PARALLEL), producerSemaphore = new Semaphore(0);
		private Random random = new Random();
		private Queue<Integer> queue = new ArrayDeque<>();
		private Lock lock = new ReentrantLock();

		/** {@inheritDoc} */
		@Override
		public void produce() {
			try {
				LOGGER.trace("{}: Semaphore holen...", Thread.currentThread().getName());
				this.consumerSemaphore.acquire();
				var item = this.random.nextInt();
				this.lock.lock();
				try {
					this.queue.offer(item);
					LOGGER.trace("{}: Länge Queue: {}={}", Thread.currentThread().getName(), this.queue.size(), this.queue.stream().collect(Collectors.toList()));
				}
				finally {
					this.lock.unlock();
				}
				LOGGER.trace("{}: Produziert: {}.", Thread.currentThread().getName(), item);
				try { Thread.sleep(500); } catch (InterruptedException e) { }
				this.producerSemaphore.release();
				LOGGER.trace("{}: fertig.", Thread.currentThread().getName());
			}
			catch (InterruptedException e) {
			}
		}

		/** {@inheritDoc} */
		@Override
		public Integer consume() throws IllegalAccessException {
			try {
				Integer item;
				LOGGER.trace("{}: Semaphore holen...", Thread.currentThread().getName());
				this.producerSemaphore.acquire();
				this.lock.lock();
				try {
					item = this.queue.poll();
					if ( item == null ) throw new IllegalAccessException(Thread.currentThread().getName());
					LOGGER.trace("{}: Länge Queue: {}={}", Thread.currentThread().getName(), this.queue.size(), this.queue.stream().collect(Collectors.toList()));
				}
				finally {
					this.lock.unlock();
				}
				LOGGER.trace("{}: Konsumiert: {}.", Thread.currentThread().getName(), item);
				this.consumerSemaphore.release();
				LOGGER.trace("{}: fertig.", Thread.currentThread().getName());
				return item;
			}
			catch (InterruptedException e) {
				return null;
			}
		}
	}


	/** Der {@linkplain ProducerThread} erzeugt die Items in einer Endlosschleife */
	public static class ProducerThread extends Thread {
		private final ProducerConsumer.Item item;
		public ProducerThread(ProducerConsumer.Item item) {
			this.setName(getName() + " (Producer)");
			this.item = item;
		}
		@Override
		public void run() {
			while ( true ) {
				this.item.produce();
			}
		}
	}


	/** Der {@linkplain ConsumerThread} erhält die Items in einer Endlosschleife */
	public static class ConsumerThread extends Thread {
		private final ProducerConsumer.Item item;
		public ConsumerThread(ProducerConsumer.Item item) {
			this.setName(getName() + " (Consumer)");
			this.item = item;
		}
		@Override
		public void run() {
			while ( true ) {
				try {
					this.item.consume();
				} catch (IllegalAccessException e) {
					System.exit(1);
				}
			}
		}
	}


	/** Erzeugt den gewünschen Consumer/Producer-Item und startet die Threads */
	public static void main(String[] args) {
//		ProducerConsumer.Item item = new ProducerConsumer.SingleItem();
		ProducerConsumer.Item item = new ProducerConsumer.MultipleItem();
		final var consumerList = IntStream.range(0, NUM_CONSUMER).mapToObj(i -> new ConsumerThread(item)).collect(Collectors.toList());
		final var producerList = IntStream.range(0, NUM_PRODUCER).mapToObj(i -> new ProducerThread(item)).collect(Collectors.toList());
		consumerList.forEach(Thread::start);
		producerList.forEach(Thread::start);
	}

}
