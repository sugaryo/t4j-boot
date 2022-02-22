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
		this.countdownNewYear( true ); // debug=true
	}
	@Scheduled(cron = "${schedule.happy_new_year.call}")
	public void happyNewYear_call() {
		this.countdownNewYear( false ); // debug=false
	}
	private void countdownNewYear(final boolean debug) {
		final int year = LocalDateTime.now().getYear() + 1;
		final String content 
				= "HAPPY NEW YEAR [" + year + "] "
				+ "\r\n"
				+ "✧*。◝(*'▿'*)◜ ✧*。"
				+ "\r\n";
		this.nyappi.callCountDown( 3, debug, content );
	}
	
	// TODO : ねこ。
}
