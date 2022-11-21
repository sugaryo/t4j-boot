package sugaryo.t4jboot.app.scheduler;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import sugaryo.t4jboot.app.module.DisplayNameExtendTypeChanger;
import sugaryo.t4jboot.app.module.DisplayNameHttpStatusChanger;
import sugaryo.t4jboot.app.module.NyappiCall;

@Component
public class NyappiScheduler {
	
	private static final Logger log = LoggerFactory.getLogger( NyappiScheduler.class );
	
	@Autowired
	NyappiCall nyappi;
	
	
	// FIXME: ここなんか上手い切り替え方法無いかな。
//	@Autowired
//	DisplayNameHttpStatusChanger displayname;
	@Autowired
	DisplayNameExtendTypeChanger displayname;
	
	@Scheduled(cron = "${schedule.nyappi_call.cron}")
	public void cron() {
		log.info( "にゃっぴ。" );
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

	@Scheduled(cron = "${schedule.happy_neco_nyan.test}")
	public void happyNecoNyaaaaaan_debug() {
		this.countdownNyaaaaaaaaaaan( true ); // debug=true
	}
	@Scheduled(cron = "${schedule.happy_neco_nyan.call}")
	public void happyNecoNyaaaaaan_call() {
		this.countdownNyaaaaaaaaaaan( false ); // debug=false
	}
	private void countdownNyaaaaaaaaaaan(final boolean debug) {
		final String neco = "₍˄ ·͈ ༝ ·͈ ˄₎◞ ̑̑";    // ねこ。
		final String content 
				= "Nyaaaaaaaaaaaaaaaaaaaaaaaaaaaan!!!!"
				+ "\r\n"
				+ neco
				+ "\r\n"
				+ "\r\n";
		this.nyappi.callCountDown( 3, debug, content );
	}
	

	@Scheduled(cron = "${schedule.shuffle_name.cron}")
	private void shuffleDisplayName() {
		final String name = this.displayname.shuffle();
		log.info( "★ shuffle display-name : [{}]", name );
	}
	
}
