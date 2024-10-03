package com.github.mbeier1406.howto.ausbildung.mt;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VirtualThreads {

	public static final Logger LOGGER = LogManager.getLogger(VirtualThreads.class);

	public static void main(String[] args) throws InterruptedException {
		final Runnable r = () -> LOGGER.info("Thread: "+Thread.currentThread());
		LOGGER.info("Namen von Plattform- und virtual Theads anzeigen!");
		Stream
			.of(
				new Thread(r),						// Thread: Thread[#25,Thread-0,5,main]
				Thread.ofPlatform().unstarted(r),	// Thread: Thread[#26,Thread-1,5,main]
				Thread.ofVirtual().unstarted(r))	// VirtualThread[#27]/runnable@ForkJoinPool-1-worker-1
			.forEach(t -> {
				t.start();
				try { t.join(); } catch (InterruptedException e) {}				
			});
		LOGGER.info("Anzeigen, wie groß der ThreadPool wird.");
		var listOfVirtualThreads = new ArrayList<Thread>();
		// Bis max. ForkJoinPool-1-worker-4 auf meinen 4-Core System
		for ( int i=0; i < 1000; i++ ) listOfVirtualThreads.add(Thread.ofVirtual().unstarted(r));
		for ( Thread t : listOfVirtualThreads ) t.start();
		for ( Thread t : listOfVirtualThreads ) t.join();
		LOGGER.info("Unterbrochene, virtuelle Threads werden evtl. auf anderen Workern ausgeführt.");
		listOfVirtualThreads = new ArrayList<Thread>();
		for ( int i=0; i < 2; i++ ) listOfVirtualThreads.add(Thread.ofVirtual().unstarted(new BlockingTask()));
		for ( Thread t : listOfVirtualThreads ) t.start();
		for ( Thread t : listOfVirtualThreads ) t.join();
		LOGGER.info("Fertig!");
	}

	public static class BlockingTask implements Runnable {
		@Override
		public void run() {
			LOGGER.info("Before sleep(); Thread: "+Thread.currentThread());
			try { Thread.sleep(1000); } catch (InterruptedException e) { }
			LOGGER.info("After sleep(); Thread: "+Thread.currentThread());
		}
		
	}

}
