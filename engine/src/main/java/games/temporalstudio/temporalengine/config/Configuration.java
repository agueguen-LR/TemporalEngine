package games.temporalstudio.temporalengine.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

import games.temporalstudio.temporalengine.utils.BadResourceException;

public abstract class Configuration{

	private static final String PROPERTIES_RESOURCE_DIR = "config";
	public static final String PROPERTIES_FILE_FORMAT = "%s.properties";

	private final File configFile;
	private Properties properties = new Properties();

	protected Configuration(final File configFile){
		this.configFile = configFile;

		loadDefaults();
	}

	// GETTERS
	public abstract String getDefaultFile();

	public File getConfigFile(){ return configFile; }
	protected final Properties getProperties(){ return properties; }
	public String get(final String key){ return properties.getProperty(key); }

	// SETTERS
	protected abstract void parse(String key) throws IllegalArgumentException;

	// Loading
	public void load() throws FileNotFoundException, IOException{
		load(configFile);
	}
	private void loadDefaults(){
		loadResource(
			Path.of(PROPERTIES_RESOURCE_DIR, getDefaultFile()).toString()
		);
	}

	public void load(final InputStream stream) throws IOException{
		properties = new Properties(properties);

		try{
			properties.load(stream);
		}catch(final IOException e){
			throw new IOException(
				"Cannot read properties (%s);".formatted(e.getMessage()), e
			);
		}

		for(String key : properties.stringPropertyNames()) parse(key);
	}
	public void load(final File file) throws FileNotFoundException, IOException{
		try(FileInputStream fis = new FileInputStream(file)){
			load(fis);
		}catch(final FileNotFoundException e){
			final FileNotFoundException excep = new FileNotFoundException(
				"No such configuration file at %s;".formatted(
					file.getAbsolutePath()
				)
			);
			excep.addSuppressed(e);

			throw excep;
		}catch(final IOException e){
			throw new IOException(
				"Cannot open configuration file at %s (%s);".formatted(
					file.getAbsolutePath(), e.getMessage()
				), e
			);
		}
	}
	public void loadResource(final String name){
		final Optional<InputStream> res = Optional.ofNullable(
			ClassLoader.getSystemResourceAsStream(name)
		);

		if(res.isEmpty())
			throw new BadResourceException(new FileNotFoundException(
				"No such configuration resource at %s.".formatted(name)
			));

		try(InputStream is = res.get()){
			load(is);
		}catch(final IOException e){
			throw new BadResourceException(
				"Cannot open configuration resource at %s (%s).".formatted(
					name, e.getMessage()
				), e
			);
		}
	}

	// Saving
	public void save(final String comments) throws IOException{
		try{
			configFile.createNewFile();
		}catch(final IOException e){
			throw new IOException(
				"Cannot create configuration file at %s.".formatted(
					configFile.getName()
				), e
			);
		}

		try(FileWriter fos = new FileWriter(configFile)){
			properties.store(fos, comments);
		}catch(final IOException e){
			throw new IOException(
				"Cannot write configuration file at %s.".formatted(
					configFile.getName()
				), e
			);
		}
	}
}