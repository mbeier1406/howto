package de.gmxhome.golkonda.howto.rest.server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationPath("/api")
public class RestServerConfig extends Application {

	public static final Logger LOGGER = LogManager.getLogger(RestServerConfig.class);

	public RestServerConfig() {
		LOGGER.info("Path: ");
	}

}
