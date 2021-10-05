package com.github.mbeier1406.howto.jse.jndi;

import java.util.Properties;
import java.util.Queue;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Listing {

	public static final Logger LOGGER = LogManager.getLogger(Listing.class);

	public static final void main(String[] args) throws NamingException {
		Properties properties = new Properties();
		properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
		properties.put(Context.PROVIDER_URL, "http://localhost:8080");//		props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
//		props.setProperty("jnp.socket.Factory", "org.jnp.interfaces.TimedSocketFactory");
		Context ctx = new InitialContext(properties);
		Queue myTestQueue = (Queue) ctx.lookup("jms/queue/myTestQueue");
		System.out.println( "\nJNDI-Context-Listing:\n" );
	      showJndiContext( ctx, "", "" );
		ctx.close();

	}

	   public static void showJndiContext( Context ctx, String name, String space )
	   {
	      if( null == name  ) name  = "";
	      if( null == space ) space = "";
	      try {
	         NamingEnumeration<NameClassPair> en = ctx.list( name );
	         while( en != null && en.hasMoreElements() ) {
	            String delim = ( name.length() > 0 ) ? "/" : "";
	            NameClassPair ncp = en.next();
	            System.out.println( space + name + delim + ncp );
	            if( space.length() < 40 )
	               showJndiContext( ctx, ncp.getName(), "    " + space );
	         }
	      } catch( javax.naming.NamingException ex ) {
	         LOGGER.error("name: {}", name, ex);
	      }
	   }

}
