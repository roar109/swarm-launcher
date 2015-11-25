package org.rage.zeppelin.configuration;

import java.util.Iterator;
import java.util.Map;

import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.rage.zeppelin.configuration.reader.AppFile;
import org.rage.zeppelin.utils.Constants;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

/**
 * Read the configuration file and configure the Swarm container with the found
 * configuration.
 * 
 * @author hector.mendoza
 *
 */
public class ApplicationConfiguration {

	private final Map<Object, Object> configuration;
	private Container weldContainer;
	private JAXRSArchive archive;

	/**
	 * @param container
	 * @param archive
	 * @param appFileHandler
	 */
	public ApplicationConfiguration(final Container container, final JAXRSArchive archive,
			final AppFile appFileHandler) {
		this.weldContainer = container;
		this.archive = archive;
		this.configuration = appFileHandler.readFileFromParameter();
	}

	/**
	 * @throws Exception
	 */
	public void setupContainerAndDeployApp() throws Exception {
		configureProperties();
		setupContainerConfiguration();
		weldContainer.deploy(archive);
	}

	private void setupContainerConfiguration() throws Exception{
		if (archive != null) {
			addPackage();
			addResources();
			archive.addAllDependencies();
		}
	}
	
	/**
	 * Configure available data sources
	 */
	public void configureDatasources() {
		if (configuration.containsKey("datasources")) {
			final JSONObject datasources = (JSONObject) configuration.get("datasources");
			System.out.println(datasources);
		}
	}

	/**
	 * Register properties from the config file as system properties
	 */
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

	/**
	 * Add packages to the archive
	 */
	public void addPackage() {
		if (configuration.containsKey(Constants.PACKAGE_PROPERTY)) {
			archive.addPackages(Boolean.TRUE, String.valueOf(configuration.get(Constants.PACKAGE_PROPERTY)));
		}
	}

	/**
	 * Add additional files as resources
	 */
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

	/**
	 * @param key
	 * @param properties
	 */
	public void ifKeyExistAddItAsSystemProperty(final Object key, final Map<Object, Object> properties) {
		if (properties.containsKey(key)) {
			System.setProperty(String.valueOf(key), String.valueOf(properties.get(key)));
		}
	}
}
