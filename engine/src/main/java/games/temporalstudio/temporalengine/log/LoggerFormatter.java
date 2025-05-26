package games.temporalstudio.temporalengine.log;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LoggerFormatter extends Formatter{

	private static final String LOG_FORMAT = "(%1$tDT%1$tT){P:%2$d}[%3$s] %4$s";
	
	@Override
	public String format(LogRecord record){
		return LOG_FORMAT.formatted(
			record.getMillis(),
			record.getLongThreadID(),
			record.getLevel().getName(),
			record.getMessage()
		);
	}
}