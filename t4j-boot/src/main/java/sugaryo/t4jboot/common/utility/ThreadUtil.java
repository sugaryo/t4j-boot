package sugaryo.t4jboot.common.utility;

public class ThreadUtil {
	public static void sleep( long ms ) {
		try {
			Thread.sleep( ms );
		} catch ( InterruptedException ex ) {
			throw new RuntimeException( ex );
		}
	}
}
