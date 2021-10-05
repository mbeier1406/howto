package com.github.mbeier1406.howto.rest.server;

import javax.enterprise.context.ApplicationScoped;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class RestServer {

	public static final Logger LOGGER = LogManager.getLogger(RestServer.class);

	public RestServer() {
		LOGGER.info("yyyyyyyyyy");
	}

	public void log() {
		LOGGER.info("zzzzzzzzz");
	}

}
