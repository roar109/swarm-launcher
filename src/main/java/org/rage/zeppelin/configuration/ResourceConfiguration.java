package org.rage.zeppelin.configuration;

import static org.rage.zeppelin.utils.FunctionUtils.toString;

import java.util.List;
import java.util.Map;

import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.rage.zeppelin.configuration.operation.ResourceOperation;
import org.rage.zeppelin.utils.Constants;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

public class ResourceConfiguration {

	private final Map<Object, Object> configuration;
	private final JAXRSArchive archive;

	public ResourceConfiguration(final Map<Object, Object> configuration, final JAXRSArchive archive) {
		this.configuration = configuration;
		this.archive = archive;
	}

	public void configureResources() {
		if (existsSectionInConfigurationMap(Constants.RESOURCES_PROPERTY)) {
			addWebResourcesToArchive(getJsonArrayFromConfiguration(Constants.RESOURCES_PROPERTY));
		}

		if (existsSectionInConfigurationMap(Constants.REST_RESOURCES_PROPERTY)) {
			addJaxRsResourcesToArchive(getJsonArrayFromConfiguration(Constants.REST_RESOURCES_PROPERTY));
		}
	}

	private boolean existsSectionInConfigurationMap(final String key) {
		return configuration.containsKey(key);
	}

	private JSONArray getJsonArrayFromConfiguration(final String propertyName) {
		return (JSONArray) configuration.get(propertyName);
	}
	
	private void addWebResourcesToArchive(final JSONArray resources) {
		addResourceOperationToArchive(resources, (jsonObject, archive)->{
			archive.addAsWebInfResource(
					new ClassLoaderAsset(toString.apply(jsonObject.get(Constants.RESOURCE_NAME_PROPERTY)), this.getClass().getClassLoader()),
					toString.apply(jsonObject.get(Constants.RESOURCE_TARGET_PATH_PROPERTY)));
		});
	}

	private void addJaxRsResourcesToArchive(final JSONArray resources) {
		addResourceOperationToArchive(resources, (jsonObject, archive)->{
			try {
				archive.addResource(getClassFromClassNameString(toString.apply(jsonObject.get(Constants.REST_RESOURCES_CLASS_PROPERTY))));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}
	
	private void addResourceOperationToArchive(final JSONArray resources, ResourceOperation operation){
		final List<JSONObject> resourcesList = (List<JSONObject>) resources;
		//TODO review if the archive.addResource & archive.addAsWebInfResource are thread-safe
		resourcesList.parallelStream().forEach(resource -> {
			operation.configure(resource, archive);
		});
	}
	
	@SuppressWarnings("rawtypes")
	private Class getClassFromClassNameString(final String className) throws ClassNotFoundException{
		return Class.forName(className);
	}
}
