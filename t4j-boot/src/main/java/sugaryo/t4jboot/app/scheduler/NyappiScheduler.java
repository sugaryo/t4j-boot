package sugaryo.t4jboot.app.scheduler;

import java.time.LocalDateTime;

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
		this.nyappi.callRandom();
	}
	
	
	@Scheduled(cron = "${schedule.happy_new_year.test}")
	public void happyNewYear_debug() {
		final int NEW_YEAR = LocalDateTime.now().getYear() + 1;
		this.nyappi.callCountDown( 3, true, 
				"TEST [" + NEW_YEAR + "] " ); // debug=true
	}
	@Scheduled(cron = "${schedule.happy_new_year.call}")
	public void happyNewYear_call() {
		final int NEW_YEAR = LocalDateTime.now().getYear() + 1; 
		this.nyappi.callCountDown( 3, false, 
				"HAPPY NEW YEAR [" + NEW_YEAR + "] " ); // debug=false
	}
}
