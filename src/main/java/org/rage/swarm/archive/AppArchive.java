package org.rage.swarm.archive;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.container.LibraryContainer;
import org.jboss.shrinkwrap.api.container.ResourceContainer;
import org.jboss.shrinkwrap.api.container.ServiceProviderContainer;
import org.jboss.shrinkwrap.api.container.WebContainer;
import org.wildfly.swarm.container.DependenciesContainer;
import org.wildfly.swarm.container.JBossDeploymentStructureContainer;
import org.wildfly.swarm.undertow.JBossWebContainer;
import org.wildfly.swarm.undertow.StaticContentContainer;

//TODO in progress...
public interface AppArchive extends Archive<AppArchive>, LibraryContainer<AppArchive>, WebContainer<AppArchive>,
		ResourceContainer<AppArchive>, ServiceProviderContainer<AppArchive>,
		JBossDeploymentStructureContainer<AppArchive>, JBossWebContainer<AppArchive>,
		DependenciesContainer<AppArchive>, StaticContentContainer<AppArchive> {

}
