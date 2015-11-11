# zeppelin

[![Build Status](https://drone.io/github.com/roar109/zeppelin/status.png)](https://drone.io/github.com/roar109/zeppelin/latest)

Small launcher project that reads a json and configures your [Wildfly Swarm](http://wildfly.org/swarm/ "Wildfly Swarm") container with the given configuration.

Maven (need to compile locally, not in maven central repos)

    <dependency>
    	<groupId>org.rage</groupId>
    	<artifactId>zeppelin</artifactId>
    	<version>1.0.0-SNAPSHOT</version>
    </dependency>

Example of configuration in your app as a plug in in the pom:

	<properties>
		<wilfy.swarm.version>1.0.0.Alpha5</wilfy.swarm.version>
	</properties>


	<plugin>
		<groupId>org.wildfly.swarm</groupId>
		<artifactId>wildfly-swarm-plugin</artifactId>
		<version>${wilfy.swarm.version}</version>
		<configuration>
			<mainClass>org.rage.zeppelin.launcher.Main</mainClass>
		</configuration>
		<executions>
			<execution>
				<goals>
					<goal>package</goal>
				</goals>
			</execution>
		</executions>
	</plugin>

Run it like

    java -jar myapp-swarm.jar app.json


See an example [here](https://github.com/roar109/ignite-server "Ignite Server")