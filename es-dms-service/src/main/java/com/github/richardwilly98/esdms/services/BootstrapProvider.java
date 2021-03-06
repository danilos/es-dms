package com.github.richardwilly98.esdms.services;

import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.log4j.Logger;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.ImmutableSettings.Builder;

import com.github.richardwilly98.esdms.SettingsImpl;
import com.github.richardwilly98.esdms.api.Settings;
import com.github.richardwilly98.esdms.services.BootstrapService;

public class BootstrapProvider implements BootstrapService {

	private static Logger log = Logger.getLogger(BootstrapProvider.class);
	private Settings settings;

	@Override
	public Settings loadSettings() {
		if (settings == null) {
			Builder builder = ImmutableSettings.settingsBuilder()
					.loadFromClasspath("es-dms-settings.yml");
			checkNotNull(builder);
			checkNotNull(builder.get("library"));
			checkNotNull(builder.get("es.host"));
			checkNotNull(builder.get("es.port"));
			settings = new SettingsImpl();
			settings.setLibrary(builder.get("library"));
			settings.setEsHost(builder.get("es.host"));
			settings.setEsPort(Integer.parseInt(builder.get("es.port")));
			log.debug("settings: " + settings);
		}
		return settings;
	}

}
