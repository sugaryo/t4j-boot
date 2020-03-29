package sugaryo.t4jboot.app.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import sugaryo.t4jboot.app.config.ConfigSet;
import sugaryo.t4jboot.app.module.SelfRetweet;

@Component
public class AutoRetweetScheduler {

	@Autowired
	SelfRetweet self;

	@Autowired
	ConfigSet config;
	
	@Scheduled(cron = "${schedule.autort.weekday.cron}")
	public void weekday() {
		this.self.retweets( config.schedule.autoRt.counts.weekday );
	}
	
	@Scheduled(cron = "${schedule.autort.weekend.cron}")
	public void holiday() {
		this.self.retweets( config.schedule.autoRt.counts.weekend );
	}
}
