package games.temporalstudio.temporalengine;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import games.temporalstudio.temporalengine.config.Configuration;
import games.temporalstudio.temporalengine.log.DualSystemHandler;
import games.temporalstudio.temporalengine.log.LoggerFormatter;
import games.temporalstudio.temporalengine.utils.BadResourceException;
import games.temporalstudio.temporalengine.utils.I18n;

public abstract class App{

	private static final String NAME_PROP_NAME = "name";
	private static final String VERSION_PROP_NAME = "version";

	private final Properties props = new Properties();
	private final I18n language;
	private final Logger logger;

	public App(){
		try(final InputStream r = ClassLoader.getSystemResourceAsStream(
			Configuration.PROPERTIES_FILE_FORMAT.formatted(getIdentifier())
		)){
			props.load(r);
		}catch(final IOException e){
			throw new BadResourceException(e);
		}

		// I18n generals and app specifics localized.
		language = new I18n();
		language.loadGenerals();

		// Logger.
		logger = Logger.getLogger(getIdentifier());
		logger.setUseParentHandlers(false);
		logger.addHandler(new DualSystemHandler(new LoggerFormatter()));

		// Configuration.
		language.loadApp(this);
	}

	// GETTERS
	public abstract String getIdentifier();

	public String getName(){ return props.getProperty(NAME_PROP_NAME); }
	public String getVersion(){ return props.getProperty(VERSION_PROP_NAME); }
	public I18n getI18n(){ return language; }
	public Logger getLogger(){ return logger; }

	// FUNCTIONS
	public abstract void run(String[] args);
	public abstract void run(Console console, String[] args);
}