package sugaryo.t4jboot.app.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import sugaryo.t4jboot.app.module.NyappiCall;

@Component
public class NyappiScheduler {
	
	@Autowired
	NyappiCall nyappi;
	
	@Scheduled(cron = "0 0 * * * *")
	public void cron() {
		// 毎時間０分ちょうどでランダムにゃっぴこーる。
		this.nyappi.randomcall();
	}
	
}