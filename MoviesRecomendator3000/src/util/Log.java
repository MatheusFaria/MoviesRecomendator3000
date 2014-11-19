package util;

public class Log {
	public static final boolean DEBUG = true;
	
	public static void debug(String message){
		if(Log.DEBUG)
			System.out.println(message);
	}
	
	public static void debug(String messageSuccess, String messageFail, boolean condition) {
		if(condition)
			Log.debug(messageSuccess);
		else
			Log.debug(messageFail);
	}
}
