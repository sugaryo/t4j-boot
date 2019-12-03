package sugaryo.t4jboot.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import sugaryo.t4jboot.common.utility.JsonMapper;

@Configuration
@PropertySource(value = "classpath:t4jboot.properties", encoding = "UTF-8")
public class NyappiConfig {
	
	public static final class SelfRetweetConfig {
		
		/** セルフリツイートするツイートID（{@code status.id}）の配列. */
		public final long[] ids;

		/** 連続処理するインターバルタイム（{@code Thread.sleep( interval );}）. */
		public final long interval;
		
		private SelfRetweetConfig( String ids, long interval ) {
			this.ids = JsonMapper.parse( ids, long[].class );
			this.interval = interval;
		}
		
		private SelfRetweetConfig( long[] ids, long interval ) {
			this.ids = ids;
			this.interval = interval;
		}
	}
	
	public final SelfRetweetConfig selfrt;
	
	public NyappiConfig(
			@Value("${nyappi_call.self_rt.ids}") String selfrtIds,
			@Value("${nyappi_call.self_rt.interval}") long selfrtInterval ) {
		this.selfrt = new SelfRetweetConfig( selfrtIds, selfrtInterval );
	}
}
