package sugaryo.t4jboot.app.module;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sugaryo.t4jboot.app.api.TwitterApiCall;

@Component
public class NyappiCall {
	
	private static final Logger log = LoggerFactory.getLogger( NyappiCall.class );
	
	private final RandomHolder random;
	
	private final Message message;
	
	private final TwitterApiCall twitter;
	
	public NyappiCall(
			@Autowired RandomHolder random,
			@Autowired Message message,
			@Autowired TwitterApiCall twitter ) {
		this.random = random;
		this.message = message;
		this.twitter = twitter;
	}
	
	public enum NyappiTweetKind {
		QT_FIRE_MILLE_ILLUST,
		QT_KORONE_563K_ILLUST,
		ADVERTISE_CURRY_NOTE,
		ADVERTISE_ZENN_SPRING_BOOT,
		;
		public static NyappiTweetKind random() {

			NyappiTweetKind[] kinds = NyappiTweetKind.values();
			final int nano = LocalDateTime.now().getNano();
			final int nano13 = nano % 13;
			final int i = nano13 % kinds.length;

			log.debug( "random-{}%13-{}-[{}]", nano, nano13, i );
			
			return kinds[i]; 
		}
	}

	public String messageOf( NyappiTweetKind kind ) {

		LocalDateTime now = LocalDateTime.now();
		String timestamp = now.format( DateTimeFormatter.ISO_DATE_TIME );
		
		switch( kind ) {
			
			case ADVERTISE_CURRY_NOTE:
				return this.message.ofAdvertiseCurryNote( timestamp );
				
			case ADVERTISE_ZENN_SPRING_BOOT:
				return this.message.ofAdvertiseZennSpringBoot( timestamp );
			
			case QT_FIRE_MILLE_ILLUST:
				return this.message.ofAdvertiseFireMilleIllust( timestamp );
			
			case QT_KORONE_563K_ILLUST:
				return this.message.ofAdvertiseKorone563KiroIllust( timestamp );
				
			default:
				return null;
		}
	}
	
	public void randomcall() {
		
		// 制御乱数でヒットしたら、にゃっぴキャンセルからの特殊コール。
		if ( this.random.rand() ) {
			log.info( "特殊にゃっぴこーる。" );
			
			NyappiTweetKind kind = NyappiTweetKind.random();
			this.call( kind );
		} 
		// ヒットしなかった場合は、通常のにゃっぴこーる。
		else {
			log.info( "通常にゃっぴこーる。" );
			this.call();
		}
	}
	
	
	// 特殊にゃっぴこーる。
	public void call( NyappiTweetKind kind ) {
		
		final String msg = this.messageOf( kind );
		if ( null == msg ) {
			this.call();
		} else {
			this.twitter.tweet( msg );
		}
	}
	
	// 通常にゃっぴこーる。
	public void call() {
		
		final DateTimeFormatter MDH = DateTimeFormatter.ofPattern( "M月d日の HH時 " );
		final DateTimeFormatter YMDHMS = DateTimeFormatter.ISO_DATE_TIME;
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime adjust = now.plusMinutes( 1 );// cron誤差対応のため１分加算。
		
		String hour = adjust.format( MDH ); 
		String timestamp = now.format( YMDHMS );
		
		// なるよじ判定
		final boolean is04H = 4 == adjust.getHour();
		final String msg = is04H 
				? this.message.ofNaru4JiCall( timestamp )
				: this.message.ofNyappiCall( timestamp, hour );
				
		this.twitter.tweet( msg );
	}
}
