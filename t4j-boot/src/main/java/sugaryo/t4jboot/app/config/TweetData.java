package sugaryo.t4jboot.app.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.type.TypeReference;

import sugaryo.t4jboot.common.utility.Java.Primitive.Int64;
import sugaryo.t4jboot.common.utility.JsonMapper;
import sugaryo.t4jboot.data.values.NamedIds;


public class TweetData {
	
	private static final Logger log = LoggerFactory.getLogger( TweetData.class );

	public static long[] of(final String name) {
		long[] ids = SingletonHolder.datasource.containsKey( name ) 
				? SingletonHolder.datasource.get( name ).ids 
				: new long[] {}; 
		return ids;
	}
	
	public static long[] union() {
		
		// 同一IDはここで畳み込む。
		HashSet<Long> ids = new HashSet<>();
		
		for ( NamedIds itor : SingletonHolder.datasource.values() ) {
			for ( long id : itor.ids ) {
				ids.add( id );
			}
		}
		
		return Int64.of( ids );
	}
	
	

	/** Initialization-on-demand holder idiom */
	private static class SingletonHolder {
		
		private static final Map<String, NamedIds> datasource = initialize( load() );
		
		private static Map<String, NamedIds> initialize( final String json ) {
			
			// JSONを一旦普通にパース。
			HashMap<String, List<Long>> map = JsonMapper.parse( json, new TypeReference<HashMap<String, List<Long>>>() {} );
			
			
			// 綺麗にプリミティブのマップに変換。
			Map<String, NamedIds> instance =new HashMap<>();
			for ( Entry<String, List<Long>> entry : map.entrySet() ) {
				
				final String name = entry.getKey();
				final Long[] list = entry.getValue()
						.stream()
						.filter( (Long x) -> { return x != 0; } )
						.toArray( Long[]::new );
				final long[] ids = Int64.of( list );
				
				// NamedIds のインスタンスを作ってマップに登録。
				NamedIds nids = new NamedIds( name, ids );
				instance.put( nids.name, nids );
			}
			
			return instance;
		}
		
		// C# の File.ReadAllText みたいな
		private static String load() {
			
			log.info( "★ data/tweet.json 読み込み★" );
			try {
				File file = ResourceUtils.getFile( "classpath:data/tweet.json" ); // src/resource/data/tweet.json
				return Files
						// json ファイルを取り敢えず単なるUTF8のテキストとして読み込み。
						.lines( file.toPath(), StandardCharsets.UTF_8 )
						// 改行維持しておく必要はないので空文字で繋いで済ませる。
						.collect( Collectors.joining( "" ) ); 
			} catch ( IOException ex ) {
				throw new RuntimeException( ex );
			}
		}
	}
}
