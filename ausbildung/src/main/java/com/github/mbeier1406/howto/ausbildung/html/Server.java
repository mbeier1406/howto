package com.github.mbeier1406.howto.ausbildung.html;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


/**
 * Startet einen http-Server, der unter veschiedenen URLs Seiten anbietet,
 * die unterschiedliche HTML-Forms anzeigen. Folgende Pfade sind definiert:
 * <ul>
 * <li><code>/form</code>: eine einfache Eingabemaske mit Submit Button</li>
 * </ul>
 * Sie werden durch die Klasse {@linkplain Seite} ausgeliefert. Die entsprechenden
 * Dateien befinden sich im Pfad
 * <code>/ausbildung/src/main/resources/com/github/mbeier1406/howto/ausbildung/html</code>.<p/>
 * Unter der Adresse {@code /anzeige} ({@linkplain Anzeige}) werden die in der Form übermittelten
 * Parameter angezeigt.
 * @author mbeier
 */
public class Server {

	public static final Logger LOGGER = LogManager.getLogger(Server.class);

	/** Der Port, auf dem der HTTP-Server lauscht */
	public static final int PORT = 8000;


	/** Startet den HTTP-Server */
	public static final void main(String[] args) throws IOException {
		final var server = HttpServer.create(new InetSocketAddress(PORT), 0);
		server.createContext("/form", new Seite("form"));
		server.createContext("/anzeige", new Anzeige());
		final var executor = Executors.newFixedThreadPool(1);
		server.setExecutor(executor);
		LOGGER.info("Server starten (Port={})...", PORT);
		server.start();
	}

	/** Liefert die jeweilige Seite mit der Form aus */
	public static class Seite implements HttpHandler{

		private final byte[] html;
		public Seite(String seite) throws IOException {
			this.html = Files.readAllBytes(Paths.get("src/main/resources/com/github/mbeier1406/howto/ausbildung/html/"+seite+".html"));
			LOGGER.trace("seite={}: html: {}", seite, new String(html));
		}

		/** {@inheritDoc} */
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			exchange.sendResponseHeaders(200, html.length);
			try ( final var stream = exchange.getResponseBody() ) {
				stream.write(html);
			}

		}

	}

	/** Zeigt die in der Form eingegeben Parameter an */
	public static class Anzeige implements HttpHandler{

		/** {@inheritDoc} */
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			final var query = exchange.getRequestURI().getQuery();
			LOGGER.trace("{}: query: {}", Thread.currentThread(), query);
			byte[] response = String.valueOf("Query: " + query).getBytes();
			exchange.sendResponseHeaders(200, response.length);
			try ( final var stream = exchange.getResponseBody() ) {
				stream.write(response);
			}

		}

	}

}
