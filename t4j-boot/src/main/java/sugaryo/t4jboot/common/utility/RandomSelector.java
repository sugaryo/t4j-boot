package sugaryo.t4jboot.common.utility;

import java.util.Random;

public class RandomSelector {
	
	public static <T> T select( T[] values ) {
		final int n = values.length;
		final int i = new Random().nextInt( n );
		return values[i];
	}
}
