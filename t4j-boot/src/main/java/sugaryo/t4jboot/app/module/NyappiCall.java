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
	
	private static final String[] BATTERI = {
			"██████ ██████", // 0
			"██████ █████▅", // 1
			"██████ █████_", // 2
			"██████ ████▅_", // 3
			"██████ ████__", // 4
			"██████ ███▅__", // 5
			"██████ ███___", // 6
			"██████ ██▅___", // 7
			"██████ ██____", // 8
			"██████ █▅____", // 9
			"██████ █_____", // 10
			"██████ ▅_____", // 11
			"██████ ______", // 12
			"█████▅ ______", // 13
			"█████_ ______", // 14
			"████▅_ ______", // 15
			"████__ ______", // 16
			"███▅__ ______", // 17
			"███___ ______", // 18
			"██▅___ ______", // 19
			"██____ ______", // 20
			"█▅____ ______", // 21
			"█_____ ______", // 22
			"▅_____ ______", // 23
			"██████ ██████", // 24
	};
	
	// 通常にゃっぴこーる。
	public void call() {
		
		final DateTimeFormatter MDH = DateTimeFormatter.ofPattern( "M月d日の HH時 " );
		final DateTimeFormatter YMDHMS = DateTimeFormatter.ISO_DATE_TIME;
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime adjust = now.plusMinutes( 1 );// cron誤差対応のため１分加算。
		
		String hour = adjust.format( MDH ); 
		String timestamp = now.format( YMDHMS );
		
		// 補正した「時」を取得
		final int h = adjust.getHour();
		final String msg;
		final String batteri = BATTERI[h];
		switch ( h ) {
			
			case 3:
				// 夜中の３時は大惨事。
				msg = this.message.ofDai3JiCall( timestamp, batteri );
				break;
			
			case 4:
				// 夜中の４時はパズドラの朝が明ける。
				msg = this.message.ofNaru4JiCall( timestamp, batteri );
				break;
			
			default:
				// それ以外は普通。
				msg = this.message.ofNyappiCall( timestamp, batteri, hour );
				break;
		}
				
		this.twitter.tweet( msg );
	}
}
