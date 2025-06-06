package games.temporalstudio.temporalengine.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;
import java.util.TimeZone;

import games.temporalstudio.temporalengine.App;
import games.temporalstudio.temporalengine.config.Configuration;

public class I18n{

	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone(
		ZoneOffset.UTC
	);

	private static final String PROPERTIES_DIR = "i18n";
	private static final String GENERAL_PROPERTIES_NAME = "general";
	private static final String UNDEFINED_TEXT = "#UNDEFINED";

	private Locale locale;
	private TimeZone zone;
	private final Properties sentences = new Properties();

	public I18n(Locale locale, TimeZone zone){
		if(!isSupportLocale(locale))
			throw new IllegalArgumentException(
				"Unsupported %s locale;".formatted(locale)
			);

		this.locale = locale;
		this.zone = zone;

		loadGenerals();
	}
	public I18n(){
		this(DEFAULT_LOCALE, DEFAULT_TIMEZONE);
	}

	// GETTERS
	private static String getPropertiesFile(String name){
		return Configuration.PROPERTIES_FILE_FORMAT.formatted(name);
	}
	public static boolean isSupportLocale(Locale locale){
		return ClassLoader.getSystemResource(
			Path.of(PROPERTIES_DIR, locale.getLanguage(),
				getPropertiesFile(GENERAL_PROPERTIES_NAME)
			).toString()
		) != null;
	}

	public String getSentence(String identifier, Object ...args){
		return sentences.getProperty(identifier,UNDEFINED_TEXT)
			.formatted(args);
	}

	public String getLocalDateTime(String identifier, LocalDateTime localDate){
		return DateTimeFormatter.ofPattern(getSentence(identifier), locale)
			.format(localDate);
	}
	public String getLocalInstant(String identifier, Instant instant){
		return getLocalDateTime(identifier,
			LocalDateTime.ofInstant(instant, zone.toZoneId())
		);
	}
	public String getLocalDate(String identifier, Date date){
		return getLocalInstant(identifier, date.toInstant());
	}

	// SETTERS
	public void setTimeZone(TimeZone zone){
		this.zone = zone;
	}
	public void reloadWith(Locale locale){
		this.locale = locale;

		// TODO: Save loads and literally reloads them over.
		loadGenerals();
	}

	// FUNCTIONS
	private void loadResource(String name, Locale locale){
		Optional<InputStream> res = Optional.ofNullable(
			ClassLoader.getSystemResourceAsStream(Path.of(
				PROPERTIES_DIR, locale.getLanguage(), getPropertiesFile(name)
			).toString())
		);

		// TODO: Fallback to ENGLISH when not found.
		if(res.isEmpty())
			throw new BadResourceException(
				"No such language file for %s.".formatted(locale.getLanguage())
			);

		try(BufferedInputStream is = new BufferedInputStream(res.get())){
			sentences.load(is);
		}catch(IOException e){
			throw new BadResourceException(e);
		}
	}
	public void loadResource(String name){ loadResource(name, locale); }
	public void loadGenerals(){ loadResource(GENERAL_PROPERTIES_NAME); }
	public void loadApp(App app){ loadResource(app.getIdentifier()); }
}