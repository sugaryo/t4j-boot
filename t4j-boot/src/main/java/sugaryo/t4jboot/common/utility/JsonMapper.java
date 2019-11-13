package sugaryo.t4jboot.common.utility;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapper {
	
	public static <T> T parse( final String json, final Class<?> type ) {
		try {
			@SuppressWarnings("unchecked")
			T obj = (T)SingletonHolder.mapper.readValue( json, type );
			return obj;
		} catch ( Exception ex ) {
			throw new RuntimeException( ex );
		}
	}
	
	public static <T> T parse( final String json, final TypeReference<T> ref ) {
		try {
			return SingletonHolder.mapper.readValue( json, ref );
		} catch ( Exception ex ) {
			throw new RuntimeException( ex );
		}
	}
	
	public static String stringify( Object obj ) {
		try {
			return SingletonHolder.mapper.writeValueAsString( obj );
		} catch ( Exception ex ) {
			throw new RuntimeException( ex );
		}
	}
	
	
	public static class MapContext {
		
		private final Map<String, Object> map = new HashMap<>();
		
		public MapContext put( String key, Object obj ) {
			this.map.put( key, obj );
			return this;
		}
		
		public String stringify() {
			return JsonMapper.stringify( this.map );
		}
		
		/** @inherit */
		@Override
		public String toString() {
			return this.map.toString();
		}
	}
	
	public static MapContext map() {
		return new MapContext();
	}
	
	
	/** Initialization-on-demand-holder idiom */
	private static final class SingletonHolder {
		
		private static final ObjectMapper mapper = new ObjectMapper();
	}
}
