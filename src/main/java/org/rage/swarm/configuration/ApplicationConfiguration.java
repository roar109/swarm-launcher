package org.rage.swarm.configuration;

import java.util.Iterator;
import java.util.Map;

import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.rage.swarm.utils.Constants;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

/**
 * @author hector.mendoza
 *
 * @TODO validate values from json
 */
public class ApplicationConfiguration {

	private final Map<Object, Object> configuration;

	public ApplicationConfiguration(final Map<Object, Object> configuration) {
		this.configuration = configuration;
	}

	public void setupContainer(final JAXRSArchive deployment) {
		try {
			addPackage(deployment);
			addResources(deployment);
			deployment.addAllDependencies();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
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

	@SuppressWarnings("rawtypes")
	public void addResources(final JAXRSArchive deployment) {
		if (configuration.containsKey(Constants.RESOURCES_PROPERTY)) {
			final JSONArray resources = (JSONArray) configuration.get(Constants.RESOURCES_PROPERTY);
			final Iterator it = resources.iterator();

			while (it.hasNext()) {
				final JSONObject resource = (JSONObject) it.next();
				deployment.addAsWebInfResource(
						new ClassLoaderAsset(String.valueOf(resource.get(Constants.RESOURCE_NAME_PROPERTY)),
								this.getClass().getClassLoader()),
						String.valueOf(resource.get(Constants.RESOURCE_TARGET_PATH_PROPERTY)));
			}
		}
	}

	public void addPackage(final JAXRSArchive deployment) {
		if (configuration.containsKey(Constants.PACKAGE_PROPERTY)) {
			deployment.addPackages(Boolean.TRUE, String.valueOf(configuration.get(Constants.PACKAGE_PROPERTY)));
		}
	}

	public void ifKeyExistAddItAsSystemProperty(final Object key, final Map<Object, Object> properties) {
		if (properties.containsKey(key)) {
			System.setProperty(String.valueOf(key), String.valueOf(properties.get(key)));
		}
	}
}
