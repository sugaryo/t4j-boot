package sugaryo.t4jboot.app.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import sugaryo.t4jboot.app.module.SelfRetweet;

@Component
public class SelfRtScheduler {

	@Autowired
	SelfRetweet self;

	@Scheduled(cron = "0 0 9 * 1-5 *")
	public void weekday() {
		// 平日は９時に。
		this.self.retweets( 10 ); // FIXME：全RTが死ぬほどウザかったので取り敢えず１０件にしとく。
	}
	@Scheduled(cron = "0 0 12 * 6-7 *")
	public void holiday() {
		// 土日は１２時に。
		this.self.retweets( 10 ); // FIXME：全RTが死ぬほどウザかったので取り敢えず１０件にしとく。
	}
}
