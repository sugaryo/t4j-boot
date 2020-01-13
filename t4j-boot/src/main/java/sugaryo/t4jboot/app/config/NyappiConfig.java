package sugaryo.t4jboot.app.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.fasterxml.jackson.core.type.TypeReference;

import sugaryo.t4jboot.common.utility.JsonMapper;
import sugaryo.t4jboot.data.values.NamedIds;

@Configuration
@PropertySource(value = "classpath:t4jboot.properties", encoding = "UTF-8")
public class NyappiConfig {
	
	public static final class SelfRetweetConfig {
		
		public final Map<String, NamedIds> namedIdsMap;
		
		/** <b>処理インターバル：</b> {@link #ids} を処理する際のインターバルタイム（{@code Thread.sleep(interval);}）. */
		public final long interval;

		public SelfRetweetConfig( String namedIdsMap, long interval ) {
			
			HashMap<String, Long[]> map = JsonMapper.parse( namedIdsMap, new TypeReference<HashMap<String, Long[]>>() {} );
			this.namedIdsMap = new HashMap<>();
			for ( Entry<String, Long[]> entry : map.entrySet() ) {
				NamedIds nids = new NamedIds( 
						entry.getKey(), 
						array(entry.getValue()) );
				this.namedIdsMap.put( nids.name, nids );
			}
			this.interval = interval;
		}
		public SelfRetweetConfig( Map<String, NamedIds> namedIdsMap, long interval ) {
			this.namedIdsMap = namedIdsMap;
			this.interval = interval;
		}
		
		public long[] of(final String name) {
			long[] ids = this.namedIdsMap.containsKey( name ) 
					? this.namedIdsMap.get( name ).ids 
					: new long[] {}; 
			return ids;
		}
		
		public long[] all() {
			
			List<Long> ids = new ArrayList<>();
			
			for ( NamedIds itor : this.namedIdsMap.values() ) {
				for ( long id : itor.ids ) {
					ids.add( id );
				}
			}
			
			return array( ids );
		}
		
		
		// TODO：ユーティリティ整理。
		
		private static long[] array( List<Long> longs ) {
			long[] array = new long[longs.size()];
			for ( int i = 0; i < array.length; i++ ) {
				array[i] = longs.get( i );
			}
			return array;
		}
		
		private static long[] array( Long[] longs ) {
			long[] array = new long[longs.length];
			for ( int i = 0; i < array.length; i++ ) {
				array[i] = longs[i];
			}
			return array;
		}
	}
	
	public static final class RandomHolderConfig {
		
		/** <b>ヒット率：</b> 抑止中は除外した場合の、乱数判定そのもののヒット率を示す。 */
		public final int hitRatio;
		/** <b>抑止レベル：</b> 一度ヒットした場合に、連続ヒットを抑止する重み付け。 */
		public final int preventLevel;
		/** <b>連続ミス限界：</b> 抑止中を含めた連続ミス時に、強制的にヒット扱いする境界値。 */
		public final int missLimit;

		public RandomHolderConfig( int hitRatio, int preventLevel, int missLimit ) {
			this.hitRatio = hitRatio;
			this.preventLevel = preventLevel;
			this.missLimit = missLimit;
		}
	}
	
	public final SelfRetweetConfig selfrt;

	public final RandomHolderConfig random;
	
	public NyappiConfig(
			// SelfRetweetConfig
			@Value("${nyappi_call.self_rt.ids}")      String selfrtIds,
			@Value("${nyappi_call.self_rt.interval}") long selfrtInterval,
			// RandomHolderConfig
			@Value("${nyappi_call.random.hit_ratio}")     int randomHitRatio,
			@Value("${nyappi_call.random.prevent_level}") int randomPreventLevel,
			@Value("${nyappi_call.random.miss_limit}")    int randomMissLimit
			) {
		this.selfrt = new SelfRetweetConfig( selfrtIds, selfrtInterval );
		this.random = new RandomHolderConfig( randomHitRatio, randomPreventLevel, randomMissLimit );
	}
}
