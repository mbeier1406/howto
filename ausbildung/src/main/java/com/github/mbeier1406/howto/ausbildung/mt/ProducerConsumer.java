package com.github.mbeier1406.howto.ausbildung.mt;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProducerConsumer {

	public static final Logger LOGGER = LogManager.getLogger(ProducerConsumer.class);

	public static final int NUM_PRODUCER = 3;

	public static final int NUM_CONSUMER = 4;


	public static class SingleItem {
		private Semaphore consumerSemaphore = new Semaphore(1), producerSemaphore = new Semaphore(0);
		private Random random = new Random();
		private Integer item = null;

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

		public Integer consume() {
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

	public static class ProducerThread extends Thread {
		private final ProducerConsumer.SingleItem singeItem;
		public ProducerThread(ProducerConsumer.SingleItem singeItem) {
			this.singeItem = singeItem;
		}
		@Override
		public void run() {
			while ( true ) {
				this.singeItem.produce();
			}
		}
	}

	public static class ConsumerThread extends Thread {
		private final ProducerConsumer.SingleItem singeItem;
		public ConsumerThread(ProducerConsumer.SingleItem singeItem) {
			this.singeItem = singeItem;
		}
		@Override
		public void run() {
			while ( true ) {
				this.singeItem.consume();
			}
		}
	}

	public static void main(String[] args) {
		ProducerConsumer.SingleItem singleItem = new ProducerConsumer.SingleItem();
		final var consumerList = IntStream.range(0, NUM_CONSUMER).mapToObj(i -> new ConsumerThread(singleItem)).collect(Collectors.toList());
		final var producerList = IntStream.range(0, NUM_PRODUCER).mapToObj(i -> new ProducerThread(singleItem)).collect(Collectors.toList());
		consumerList.forEach(Thread::start);
		producerList.forEach(Thread::start);
	}

}
