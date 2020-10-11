package sugaryo.t4jboot.common.utility;

import java.util.Collection;
import java.util.List;

public class Java {
	public static class Primitive {
		
		public static class Int64 {
			public static long[] of( Collection<Long> longs ) {
				long[] array = new long[longs.size()];
				int i = 0;
				for ( long x : longs ) {
					array[i++] = x;
				}
				return array;
			}
			public static long[] of( Long[] list ) {
				long[] array = new long[list.length];
				for ( int i = 0; i < list.length; i++ ) {
					array[i] = list[i];
				}
				return array;
			}
		}
		
		public static class Int32 {
			public static int[] of( Collection<Integer> ints ) {
				int[] array = new int[ints.size()];
				int i = 0;
				for ( int x : ints ) {
					array[i++] = x;
				}
				return array;
			}
			public static int[] of( Integer[] list ) {
				int[] array = new int[list.length];
				for ( int i = 0; i < list.length; i++ ) {
					array[i] = list[i];
				}
				return array;
			}
		}
	}
}
