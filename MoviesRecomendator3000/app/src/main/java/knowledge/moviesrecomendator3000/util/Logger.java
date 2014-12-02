package knowledge.moviesrecomendator3000.util;

import android.util.Log;

public class Logger {
	public static final boolean DEBUG = true;
	
	public static void debug(String message){
		if(Logger.DEBUG) {
            //System.out.println(message);
            Log.i("DEBUG_TAG", message);
        }
	}
	
	public static void debug(String messageSuccess, String messageFail, boolean condition) {
		if(condition)
			Logger.debug(messageSuccess);
		else
			Logger.debug(messageFail);
	}
}
