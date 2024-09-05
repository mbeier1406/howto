package com.github.mbeier1406.howto.ausbildung.mt;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Im Gegensatz zum <i>Thread-per-Core-Model</i> wird beim <i>Thread-per-Request-Model</i>
 * (oder auch <i>Thread-per-Task-Model</i>) prinzipiell für jeden eingehenden Request
 * ein eigener Bearbeitungsthread gestartet.
 * <p>Dieses Beispiel soll die Probleme dieses Modells bei blockierenden Operationen zeigen.</p>
 * @author mbeier
 */
public class ThreadPerRequest {

	private static final Logger LOGGER = LogManager.getLogger(ThreadPerRequest.class);


	/** Testet die Laufzeit der einzelnen {@linkplain TaskExecuter} */
	public static void main(String[] args) {
		final Map<String, TaskExecuter> executers = new HashMap<String, TaskExecuter>() {{
			put("Tausend Tasks jeweils eine Sekunde", thousandTaskOneSecond);
			put("Tausend Tasks jeweils hundert Mal 10 Millisekunden", thousandTaskTenMillisTimesHundred);
		}};
		executers.entrySet().stream().forEach(executer -> {
			long start = System.currentTimeMillis();
			LOGGER.info("Starte '{}'...", executer.getKey());
			executer.getValue().performTask();
			LOGGER.info("Zeit: {} ms", System.currentTimeMillis() - start);
		});
	}


	/**
	 * Dieses Interface definiert die verschiedenen Executer, die die blockierende
	 * Methode {@linkplain ThreadPerRequest#blockingMethod(long)} aufrufen.
	 */
	@FunctionalInterface
	public static interface TaskExecuter {
		public void performTask();
	}


	/**
	 * Dieser Executer ruft die blockierende Methode 1000 Mal für eine Sekunde auf.
	 * Da die Tasks in etwa parallel ausgeführt werden, beträgt die Laufzeit
	 * in etwa eine Sekunde.
	 */
	private static TaskExecuter thousandTaskOneSecond = () -> {
		try ( final var es = Executors.newCachedThreadPool() ) {
			for ( int i=0; i < 1000; i++ )
				es.submit(() -> ThreadPerRequest.blockingMethod(1000));
		}
	};

	/**
	 * Nominell beträgt die blockierte Zeit dieses Executers die gleiche
	 * wie die des {@linkplain #thousandTaskOneSecond}. Aufgrund der
	 * häufigeren Kontextwechsel der blockierten Threads ist die
	 * Laufzeit aber entsprechend länger.
	 */
	private static TaskExecuter thousandTaskTenMillisTimesHundred = () -> {
		try ( final var es = Executors.newCachedThreadPool() ) {
			for ( int i=0; i < 1000; i++ )
				es.submit(() -> {
					for ( int j=0; j < 100; j++ )
						ThreadPerRequest.blockingMethod(10);	
				});
		}
	};


	/** Diese Methode simuliert eine (z. B. durch IO) blockierte Methode */
	private static void blockingMethod(long sleep) {
		// LOGGER.trace("Task: {}", Thread.currentThread()); // Zeigt an, wie groß der Threadpool inzwischen geworden ist
		try { Thread.sleep(sleep); } catch (InterruptedException e) { }
	}

}
