package games.temporalstudio.temporalengine.utils;

public class BadResourceException extends InternalAppException{
	
	public BadResourceException(Throwable cause){
		super(cause);
	}
	public BadResourceException(String message, Throwable cause){
		super(message, cause);
	}
	public BadResourceException(String message){
		super(message);
	}
}