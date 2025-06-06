package games.temporalstudio.temporalengine.log;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LoggerFormatter extends Formatter{

	private static final String LOG_FORMAT = "(%1$tDT%1$tT){P:%2$d}[%3$s] %4$s";
	private static final String PARAMETER_FORMAT = "\\{%d\\}";

	@Override
	public String format(LogRecord record){
		String msg = record.getMessage();

		if(record.getParameters() != null)
			for(int i = 0; i < record.getParameters().length; i++){
				msg = msg.replaceFirst(
					PARAMETER_FORMAT.formatted(i),
					record.getParameters()[i].toString()
				);
			}

		return LOG_FORMAT.formatted(
			record.getMillis(),
			record.getLongThreadID(),
			record.getLevel().getName(),
			msg
		);
	}
}