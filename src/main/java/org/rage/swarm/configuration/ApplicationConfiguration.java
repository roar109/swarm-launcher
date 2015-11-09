package org.rage.swarm.configuration;

import java.util.Iterator;
import java.util.Map;

import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.rage.swarm.utils.Constants;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

/**
 * @author hector.mendoza
 *
 */
public class ApplicationConfiguration {

	private final Map<Object, Object> configuration;
	private Container weldContainer;
	private JAXRSArchive archive;

	public ApplicationConfiguration(final Container container, final JAXRSArchive archive, final AppFile appFileHandler) {
		this.weldContainer = container;
		this.archive = archive;
		this.configuration = appFileHandler.readFileFromParameter();
	}

	public void setupContainerAndDeployApp() throws Exception {
		configureProperties();
		
		if(archive != null){
			addPackage();
			addResources();
			archive.addAllDependencies();
		}
		
		weldContainer.deploy(archive);
	}

	public void configureDatasources() {
		if (configuration.containsKey("datasources")) {
			final JSONObject datasources = (JSONObject) configuration.get("datasources");
			System.out.println(datasources);
		}
	}

	@SuppressWarnings("unchecked")
	public void configureProperties() {
		if (configuration.containsKey(Constants.SYSTEM_PROPERTIES)) {
			final Map<Object, Object> systemProperties = (Map<Object, Object>) configuration
					.get(Constants.SYSTEM_PROPERTIES);

			for (Object key : systemProperties.keySet()) {
				ifKeyExistAddItAsSystemProperty(key, systemProperties);
			}
		}
	}
	
	public void addPackage() {
		if (configuration.containsKey(Constants.PACKAGE_PROPERTY)) {
			archive.addPackages(Boolean.TRUE, String.valueOf(configuration.get(Constants.PACKAGE_PROPERTY)));
		}
	}

	@SuppressWarnings("rawtypes")
	public void addResources() {
		if (configuration.containsKey(Constants.RESOURCES_PROPERTY)) {
			final JSONArray resources = (JSONArray) configuration.get(Constants.RESOURCES_PROPERTY);
			final Iterator it = resources.iterator();

			while (it.hasNext()) {
				final JSONObject resource = (JSONObject) it.next();
				archive.addAsWebInfResource(
						new ClassLoaderAsset(String.valueOf(resource.get(Constants.RESOURCE_NAME_PROPERTY)),
								this.getClass().getClassLoader()),
						String.valueOf(resource.get(Constants.RESOURCE_TARGET_PATH_PROPERTY)));
			}
		}
	}

	public void ifKeyExistAddItAsSystemProperty(final Object key, final Map<Object, Object> properties) {
		if (properties.containsKey(key)) {
			System.setProperty(String.valueOf(key), String.valueOf(properties.get(key)));
		}
	}
}
