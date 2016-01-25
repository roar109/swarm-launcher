package org.rage.zeppelin.configuration;

import static org.rage.zeppelin.utils.FunctionUtils.toString;

import java.util.Map;

import org.json.simple.JSONObject;
import org.rage.zeppelin.configuration.reader.AppFile;
import static org.rage.zeppelin.utils.Constants.*;
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

	public ApplicationConfiguration(final Container container, final JAXRSArchive archive,
			final AppFile appFileHandler) {
		this.weldContainer = container;
		this.archive = archive;
		this.configuration = appFileHandler.readFileFromParameter();
	}

	public void setupContainerAndDeployApp() throws Exception {
		configureProperties();
		configureContainer();
		configureDatasources();
		weldContainer.deploy(archive);
	}

	private void configureContainer() throws Exception {
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
		if (existsSectionInConfigurationMap(DATASOURCE_PROPERTY)) {
			final JSONObject datasources = (JSONObject) configuration.get(DATASOURCE_PROPERTY);
			System.out.println(datasources);
			new DatasourceConfiguration(weldContainer, datasources).setupDatasource();
		}
	}

	/**
	 * Register properties from the configuration file as system properties
	 */
	@SuppressWarnings("unchecked")
	public void configureProperties() {
		if (existsSectionInConfigurationMap(SYSTEM_PROPERTIES)) {
			final Map<Object, Object> systemProperties = (Map<Object, Object>) configuration
					.get(SYSTEM_PROPERTIES);

			systemProperties.forEach((key, value) -> {
				addPropertyAsSystemProperty(toString.apply(key), toString.apply(systemProperties.get(key)));
			});
		}
	}

	public void addPropertyAsSystemProperty(final String key, final String value) {
		System.setProperty(key, value);
	}

	/**
	 * Add packages to the archive
	 */
	public void addPackage() {
		if (existsSectionInConfigurationMap(PACKAGE_PROPERTY)) {
			archive.addPackages(Boolean.TRUE, toString.apply(configuration.get(PACKAGE_PROPERTY)));
		}
	}

	/**
	 * Add additional files as resources
	 */
	public void addResources() {
		new ResourceConfiguration(configuration, archive).configureResources();
	}

	private boolean existsSectionInConfigurationMap(final String key) {
		return configuration.containsKey(key);
	}

}
