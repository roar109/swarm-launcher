package org.rage.zeppelin.configuration.operation;

import org.json.simple.JSONObject;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

public interface ResourceOperation {
	public void configure(JSONObject jsonObject, JAXRSArchive archive);
}
