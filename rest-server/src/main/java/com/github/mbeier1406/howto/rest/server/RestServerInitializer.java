package com.github.mbeier1406.howto.rest.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Initialisierung der Serveranwendung.
 * TODO: DB initialisieren
 * @author mbeier
 */
@WebListener
public class RestServerInitializer implements ServletContextListener {

	public static final Logger LOGGER = LogManager.getLogger(RestServerInitializer.class);

	/** {@inheritDoc} */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOGGER.info("Initialisierung Servlet Context...");
		throw new RuntimeException();
	}

	/** {@inheritDoc} */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LOGGER.info("Beenden Servlet Context...");
	}

}
