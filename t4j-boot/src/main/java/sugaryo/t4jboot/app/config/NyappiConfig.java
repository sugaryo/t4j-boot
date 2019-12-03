package sugaryo.t4jboot.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import sugaryo.t4jboot.common.utility.JsonMapper;

@Configuration
@PropertySource(value = "classpath:t4jboot.properties", encoding = "UTF-8")
public class NyappiConfig {
	
	public static final class SelfRetweetConfig {
		
		/** セルフリツイートするツイートのID（{@code status.id}）. */
		public final long[] ids;
		
		private SelfRetweetConfig( String ids ) {
			this.ids = JsonMapper.parse( ids, long[].class );
		}
		
		private SelfRetweetConfig( long[] ids ) {
			this.ids = ids;
		}
	}
	
	public final SelfRetweetConfig selfrt;
	
	public NyappiConfig(
			@Value("${nyappi_call.self_rt.ids}") String ids ) {
		this.selfrt = new SelfRetweetConfig( ids );
	}
}
