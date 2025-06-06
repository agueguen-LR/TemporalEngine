package games.temporalstudio.temporalengine.utils;

public class InternalAppException extends RuntimeException{
	
	public InternalAppException(Throwable cause){
		super(cause);
	}
	public InternalAppException(String message, Throwable cause){
		super(message, cause);
	}
	protected InternalAppException(String message){
		super(message);
	}
}
