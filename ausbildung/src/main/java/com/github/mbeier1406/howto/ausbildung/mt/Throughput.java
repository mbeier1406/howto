package com.github.mbeier1406.howto.ausbildung.mt;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


/**
 * Beispiel zur Performancemessung: hier der Durchsatz<p/>
 * Es wird ein HTTP-Server gestartet, der je nach Parameter des Requests eine
 * zufällige Anzahl von Millisekunden wartet. Als Client kann für Einzeltests
 * {@code curl} bzw.{@code wget} oder auch ein Browser verwendet
 * werden. Für echte Durchsatzmessungen wird JMeter verwendet.<p/>
 * Aufrufbeispiel:
 * <pre><code>
 * $ curl http://localhost:8000/auftrag?zeit=999
 * </code></pre>
 * Der JMeter Lasttest befindet sich in
 * Das Erzeugen der Testdatei erfolgt mittels
 * <i>/ausbildung/src/main/resources/com/github/mbeier1406/howto/ausbildung/mt/Throughput Test.jmx</i>.<br/>
 * Die Testdatei mit den Wartezeiten kann so erzeugt werden:
 * <pre><code>
 * $ > throughput_test.txt; for i in {1..200}; do echo "${RANDOM} 1000 % p" | dc >> throughput_test.txt; done
 * </code></pre>
 * @author mbeier
 */
public class Throughput {

	public static final Logger LOGGER = LogManager.getLogger(Throughput.class);

	/** Der Port, auf dem der HTTP-Server lauscht */
	public static final int PORT = 8000;

	/** Anzahl parallel arbeitender Threads wenn nichts anderes angegeben ist */
	public static final int ANZAHL_THREADS = 1;


	/** Startet den HTTP-Server */
	public static final void main(String[] args) throws IOException {
		final var server = HttpServer.create(new InetSocketAddress(PORT), 0);
		server.createContext("/auftrag", new Auftragsbearbeitung());
		final var anzahlThreads = Integer.parseInt(System.getProperty("anzahlThreads", String.valueOf(ANZAHL_THREADS)));
		LOGGER.debug("anzahlThreads={}", anzahlThreads);
		final var executor = Executors.newFixedThreadPool(anzahlThreads);
		server.setExecutor(executor);
		server.start();
	}


	/** Klasse zur "<i>Bearbeitung</i>" eines Requests */
	public static class Auftragsbearbeitung implements HttpHandler {

		/** {@inheritDoc} */
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			final var query = exchange.getRequestURI().getQuery();
			LOGGER.trace("{}: query={}", Thread.currentThread(), query);
			String[] params = query.split("=");
			if ( params.length != 2 || !params[0].equals("zeit") ) {
				LOGGER.debug("{}: exchange={}", Thread.currentThread(), exchange);
				exchange.sendResponseHeaders(400, 0); // Bad Request
				return;
			}
			try {
				final var zeit = Integer.parseInt(params[1]);
				if ( zeit < 0 || zeit > 1000 )
					throw new NumberFormatException("Wertefehler: "+zeit);
				final var warten = new Random().nextInt(zeit);
				LOGGER.trace("{}: warten={}", Thread.currentThread(), warten);
				Thread.sleep(warten);
				byte[] response = String.valueOf(warten).getBytes();
				exchange.sendResponseHeaders(200, response.length);
				try ( final var stream = exchange.getResponseBody() ) {
					stream.write(response);
				}
			}
			catch ( NumberFormatException | InterruptedException e ) {
				LOGGER.debug("{}: exchange={}", Thread.currentThread(), exchange, e);
				exchange.sendResponseHeaders(400, 0); // Bad Request
			}
		}
		
	}

}
