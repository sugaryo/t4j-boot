package sugaryo.t4jboot.app.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import sugaryo.t4jboot.app.module.NyappiCall;

@Component
public class NyappiScheduler {
	
	@Autowired
	NyappiCall nyappi;
	
	@Scheduled(cron = "${schedule.nyappi_call.cron}")
	public void cron() {
		this.nyappi.randomcall();
	}
}
