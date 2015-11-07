package org.rage.swarm.launcher;

import java.io.FileReader;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.rage.swarm.configuration.ApplicationConfiguration;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.ejb.EJBFraction;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.weld.WeldFraction;

/**
 * @author hector.mendoza
 *
 */
public class Main {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			System.out.println("<<<<<<Main>>>>>>");
			
			Container container = new Container();
	        container.start();
	        
	        //Add Fractions
			container.fraction(EJBFraction.createDefaultFraction());
			container.fraction(new WeldFraction());
	        
			obj = parser.parse(new FileReader(args[0]));
			final JSONObject jsonObject = (JSONObject) obj;
			
			final ApplicationConfiguration applicationConfig = new ApplicationConfiguration(jsonObject);
			applicationConfig.configureProperties();
			
			final JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class);
			
			// Configure deployment archive
			applicationConfig.setupContainer(deployment);
			
			container.deploy(deployment);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} 
	}

}
