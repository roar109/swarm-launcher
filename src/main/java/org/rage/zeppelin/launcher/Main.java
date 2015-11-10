package org.rage.zeppelin.launcher;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.rage.zeppelin.configuration.ApplicationConfiguration;
import org.rage.zeppelin.configuration.reader.JsonAppFileImpl;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

/**
 * @author hector.mendoza
 *
 */
public class Main {

	/**
	 * Main class called by the swarm plug in, it reads the needed configuration
	 * from the passes json file.
	 * 
	 * Takes by default a JAXWS app, with ejb, logger and datasources enabled.
	 * 
	 */
	public static void main(String[] args) {
		System.out.println("<<<<<<Main>>>>>>");
		try {
			final Container container = new Container();
			container.start();
			new ApplicationConfiguration(container, ShrinkWrap.create(JAXRSArchive.class), new JsonAppFileImpl(args[0]))
					.setupContainerAndDeployApp();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

}
