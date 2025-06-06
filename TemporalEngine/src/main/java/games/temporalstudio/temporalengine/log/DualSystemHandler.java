package games.temporalstudio.temporalengine.log;

import java.io.PrintStream;
import java.util.logging.ErrorManager;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class DualSystemHandler extends Handler{

	public static final PrintStream outputStream = System.out;
	public static final PrintStream errorStream = System.err;

	public DualSystemHandler(Formatter formatter){
		setFormatter(formatter);
	}

	// Functions
	@Override
	public void publish(LogRecord record){
		try{
			String msg = getFormatter().format(record);

			if(record.getLevel().intValue() < Level.WARNING.intValue())
				outputStream.println(msg);
			else
				errorStream.println(msg);
		}catch(Exception e){
			reportError(e.getMessage(), e, ErrorManager.FORMAT_FAILURE);
		}
	}
	@Override
	public void flush(){ }
	@Override
	public void close() throws SecurityException{ }
}
