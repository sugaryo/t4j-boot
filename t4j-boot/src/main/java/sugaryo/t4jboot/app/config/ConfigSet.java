package sugaryo.t4jboot.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConfigSet {
	
	public final NyappiConfig nyappi;
	public final ScheduleConfig schedule;
	
	public ConfigSet( 
			@Autowired NyappiConfig nyappi,
			@Autowired ScheduleConfig schedule ) {
		this.nyappi = nyappi;
		this.schedule = schedule;
	}
}
