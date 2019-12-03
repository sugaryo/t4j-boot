package sugaryo.t4jboot.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import sugaryo.t4jboot.common.utility.JsonMapper;

@Configuration
@PropertySource(value = "classpath:t4jboot.properties", encoding = "UTF-8")
public class NyappiConfig {
	
	public static final class SelfRetweetConfig {
		
		/** <b>SelfRT-IDs：</b> セルフRTするツイートID（{@code status.id}）の配列. */
		public final long[] ids;
		/** <b>処理インターバル：</b> {@link #ids} を処理する際のインターバルタイム（{@code Thread.sleep(interval);}）. */
		public final long interval;
		
		private SelfRetweetConfig( String ids, long interval ) {
			this.ids = JsonMapper.parse( ids, long[].class );
			this.interval = interval;
		}
	}
	
	public static final class RandomHolderConfig {
		
		/** <b>ヒット率：</b> 抑止中は除外した場合の、乱数判定そのもののヒット率を示す。 */
		public final int hitRatio;
		/** <b>抑止レベル：</b> 一度ヒットした場合に、連続ヒットを抑止する重み付け。 */
		public final int preventLevel;
		/** <b>連続ミス限界：</b> 抑止中を含めた連続ミス時に、強制的にヒット扱いする境界値。 */
		public final int missLimit;

		private RandomHolderConfig( int hitRatio, int preventLevel, int missLimit ) {
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
