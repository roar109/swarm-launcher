package org.rage.zeppelin.configuration;

import static org.rage.zeppelin.utils.Constants.*;

import org.json.simple.JSONObject;
import org.rage.zeppelin.utils.FunctionUtils;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.jpa.JPAFraction;

public class DatasourceConfiguration {

	private Container weldContainer;
	private final String DATASOURCE_PREFIX = "jboss/datasources/";
	private final JSONObject datasource;

	public DatasourceConfiguration(final Container weldContainer, final JSONObject datasource) {
		this.weldContainer = weldContainer;
		this.datasource = datasource;
	}

	public void setupDatasource() {
		setupDatasourceFraction();
		setupDefaultDsFraction();
	}

	private void setupDatasourceFraction() {
		final DatasourcesFraction dsFraction = new DatasourcesFraction();

		dsFraction.jdbcDriver("mysql", (d) -> {
			d.driverDatasourceClassName("");
			d.xaDatasourceClass("");
			d.driverModuleName("");
		});
		
		dsFraction.dataSource(FunctionUtils.getStringValueFromJsonObject(DATASOURCE_NAME_PROPERTY, datasource),
				(ds) -> {
					ds.driverName(
							FunctionUtils.getStringValueFromJsonObject(DATASOURCE_DRIVERNAME_PROPERTY, datasource));
					ds.connectionUrl(
							FunctionUtils.getStringValueFromJsonObject(DATASOURCE_CONNURL_PROPERTY, datasource));
					ds.userName("sa");
					ds.password("sa");
				});

		weldContainer.fraction(dsFraction);
	}

	private void setupDefaultDsFraction() {
		weldContainer.fraction(new JPAFraction().inhibitDefaultDatasource().defaultDatasource(
				DATASOURCE_PREFIX + FunctionUtils.getStringValueFromJsonObject(DATASOURCE_NAME_PROPERTY, datasource)));
	}
}
