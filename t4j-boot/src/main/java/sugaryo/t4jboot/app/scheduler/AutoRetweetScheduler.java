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
	ConfigSet conf;
	

	@Scheduled(cron = "${schedule.autort.weekday.cron}")
	public void weekday() {
		this.self.retweets( 5 ); // FIXME：全RTが死ぬほどウザかったので取り敢えず5件にしとく。
	}

	@Scheduled(cron = "${schedule.autort.holiday.cron}")
	public void holiday() {
		this.self.retweets( 5 ); // FIXME：全RTが死ぬほどウザかったので取り敢えず5件にしとく。
	}
}
